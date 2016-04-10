package xyz.antsgroup.langfashion;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.antsgroup.langfashion.entity.Repo;
import xyz.antsgroup.langfashion.entity.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Crawl repositories on github.com.
 *
 * @author ants_ypc
 * @version 1.0 4/7/16
 */
public class RepoCrawler {

    private static final Logger logger = LogManager.getLogger("RepoCrawler");
    private static final String token = "token 08d6a152b9f329b88753b8dacce5d1448b258c12";
    private static final Pattern pattern = Pattern.compile("<(https://api.github.com/user/\\d+/repos\\?page=\\d+&per_page=\\d+)>; rel=\"next\".*");

    private String reposOfUserPrefix = "https://api.github.com/user/";
    private String reposOfUserSuffix = "/repos?page=1&per_page=30";
    private SqlSessionFactory sessionFactory;

    private LinkedList<User> userQueue = new LinkedList<>();
    private int since;
    private volatile boolean running;

    public RepoCrawler() {
        logger.info("RepoCrawler constructor.");
        String resource = "mybatis-config.xml";
        InputStream is = UserCrawler.class.getClassLoader().getResourceAsStream(resource);
        sessionFactory = new SqlSessionFactoryBuilder().build(is, "development");
        try (SqlSession session = sessionFactory.openSession()){
            User u = session.selectOne("Repo.getMaxIdUser");
            if (u == null) since = 1;
            else since = u.getId();
        }
        running = true;
        logger.info("RepoCrawler has initialized");
    }

    public int crawlRepos() {

        int reposNum = 0;
        while (running) {
            System.out.println(since);
            retrieveUser(since + 1, since + 10);
            since += 10;

            // check whether there is no more user
            if (userQueue.isEmpty()) {
                try (SqlSession session = sessionFactory.openSession()) {
                    int max = session.selectOne("User.getMaxUserId");
                    if (max > since - 101) continue;
                    else break;
                }
            }

            User tmp;
            int saveRepos;
            while (!userQueue.isEmpty() && running) {
                tmp = userQueue.poll();
                saveRepos = crawlUserReops(tmp);
                if(saveRepos == -1)
                    userQueue.offerFirst(tmp);
                else
                    reposNum += saveRepos;
            }

        }
        return reposNum;
    }

    /**
     * Retrive users from database where id is between idFrom and idTo, then save to userQueue variable.
     *
     * @param idFrom positive integer
     * @param idTo positive integer
     * @return the number of uses successfully got.
     */
    private int retrieveUser(int idFrom, int idTo) {
        int retrieveNum;
        try (SqlSession session = sessionFactory.openSession()) {
            List<User> list = session.selectList("User.getRangeUsers", new HashMap<String, Integer>() {
                {
                    put("idFrom", idFrom);
                    put("idTo", idTo);
                }
            });
            retrieveNum = userQueue.addAll(list) ? list.size() : 0;
        }

        return retrieveNum;
    }


    /**
     * Crawl repositories of one user and save to local database.
     *
     * @param user whose repositories need to be crawled.
     * @return the number of repositories successfully got. If failed return -1.
     */
    public int crawlUserReops(User user) {
        int reposNum;
        ArrayList<Repo> list = new ArrayList<>();
        String link = null;
        String reposUrl = reposOfUserPrefix + user.getId() + reposOfUserSuffix;

        do {
            try {
                URLConnection connection = new URL(reposUrl).openConnection();
                connection.setConnectTimeout(3000);
                connection.setRequestProperty("Authorization", token);
                connection.connect();

                // check http response headers and handle exception
                if (!connection.getHeaderField("Status").equals("200 OK")) {
                    httpDenyHandler(connection);
                    return -1;
                }

                // If user have so many repositories, server paged json, so we have to send another request
                link = connection.getHeaderField("Link");
                if (link != null) {
                    System.out.println(link);
                    Matcher matcher = pattern.matcher(link);
                    if (matcher.matches()) {
                        reposUrl = matcher.group(1);
                    } else {
                        link = null;
                    }
                }

                try (InputStream inStream = connection.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inStream))) {

                    ObjectMapper mapper = new ObjectMapper();
                    Repo[] repos = mapper.readValue(reader, Repo[].class);
                    if (repos.length == 0) return 0;    // If there is no repositories, we cannot insert into database

                    list.addAll(Arrays.asList(repos));
                }
            } catch (IOException e) {
                logger.catching(e);
            }
        } while (link != null);

        int id = user.getId();
        String login = user.getLogin();
        for (Repo r : list) {
            r.setOwnerId(id);
            r.setOwnerLogin(login);
        }

        try (SqlSession session = sessionFactory.openSession()) {
            reposNum = session.insert("Repo.insertList", list);
            session.commit();
        }
        return reposNum;
    }

    /**
     * If access denied by the server,or something else, we have to sleep for waiting or stop crawl.
     *
     * @param connection The connetion which came across error.
     */
    private void httpDenyHandler(URLConnection connection) {
        String status = connection.getHeaderField("Status");
        String resetStr = connection.getHeaderField("X-RateLimit-Reset");
        String remain = connection.getHeaderField("X-RateLimit-Remaining");

        // If the rate is limited, sleep for a while and continue to work again.
        if (status.equals("403 Forbidden") && remain.equals("0")) {
            long resetTime = Integer.valueOf(resetStr) * 1000L;
            long current = System.currentTimeMillis();
            try {
                System.out.println("X-RateLimit-Remaining is limited.Sleep now on... it'll last for " + (resetTime - current + 2000) + " millisecond");
                logger.info("X-RateLimit-Remaining is limited.Sleep now on...");
                Thread.sleep(resetTime - current + 2000);
                logger.info("Sleep over,continue to work.");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            // otherwise, log the error and stop this UserCrawler.
            try (InputStream inStream = connection.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inStream))) {

                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                }
                System.out.println("UserCrawl have to stop : " + status + "\n"+ sb.toString());
                logger.error("UserCrawl have to stop : " + status + "\n"+ sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                running = false;
            }
        }

    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

}
