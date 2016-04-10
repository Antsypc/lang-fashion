package xyz.antsgroup.langfashion;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.antsgroup.langfashion.entity.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Crawl users on github.com.
 *
 * @author ants_ypc
 * @version 1.0 4/7/16
 */
public class UserCrawler {
    private static final Logger logger = LogManager.getLogger("UserCrawler");
    private static final String usersUrlPrefix = "https://api.github.com/users?since=";
    private static final String userUrlPrefix = "https://api.github.com/users/";
    private static final String token = "token 08d6a152b9f329b88753b8dacce5d1448b258c12";
    private static final Pattern pattern = Pattern.compile("<https://api.github.com/users\\?since=([0-9]*)>.*");

    private SqlSessionFactory sessionFactory;
    private volatile String since = "";                              // crawl from since which is github recommended
    private ArrayList<String> userList = new ArrayList<>(); // crawl username and add to userList variable firstly and then crawl every user special info in turn.
    private volatile boolean running;                       // control variable


    /**
     * constructor UserCrawler.
     * Initialize the properties for mybatis and other works.It'll crawl users on github.com from the one who
     * is not existing in the database.So, it read the max id from user table and initialize since variable.
     */
    public UserCrawler() {
        logger.info("UserCrawler constructor.");
        String resource = "mybatis-config.xml";
        InputStream is = UserCrawler.class.getClassLoader().getResourceAsStream(resource);
        sessionFactory = new SqlSessionFactoryBuilder().build(is, "development");
        try (SqlSession session = sessionFactory.openSession()){
            Integer maxId = session.selectOne("User.getMaxUserId");
            if (maxId != null) {
                since = String.valueOf(maxId.intValue());
            } else {
                since = "0";
            }
        }
        running = true;
        logger.info("UserCrawler has initialized");
    }

    /**
     * Control crawler, meanwhile this is the only way supposed to running UserCrawler.
     *
     * @return The number of users this crawler got finally.
     */
    public int crawlUsers() {
        int crawlNum = 0;

        // If there is no more users to crawl on github, well, since variable may be empty or null,
        // and then stop crawling.
        while (!since.isEmpty() && since != null && running) {
            crawlUserLoginName(since);
            crawlNum += crawlSaveUsers();
        }
        logger.info("UserCrawl Finished, totally crawled :" + crawlNum);
        return crawlNum;
    }

    /**
     * Get login name of user.Because we cannot get enough information of users
     * by the api 'https://api.github.com/users?since=' , we have to get login name and save to userList temporarily.
     *
     * @param since The user id to start from.
     */
    private void crawlUserLoginName(String since) {
        try {
            URLConnection connection = new URL(usersUrlPrefix + since).openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestProperty("Authorization", token);
            connection.connect();

            // check http response headers. If we can't get message successfully,
            // we handle the exception and set since variable to right value according to local database.
            if (!connection.getHeaderField("Status").equals("200 OK")) {
                httpDenyHandler(connection);
                try (SqlSession session = sessionFactory.openSession()){
                    this.since = String.valueOf(((Integer) session.selectOne("User.getMaxUserId")).intValue());
                }
                return;
            }

            Matcher matcher = pattern.matcher(connection.getHeaderField("Link"));
            this.since = matcher.matches() ? matcher.group(1) : null;

            try (InputStream inStream = connection.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inStream))) {

                ObjectMapper mapper = new ObjectMapper();
                User[] users = mapper.readValue(reader, User[].class);

                for (User u : users) {
                    userList.add(u.getLogin());
                }

            }
        } catch (IOException e) {
            logger.catching(e);
        }
    }

    /**
     * Crawl every thing of users and save to local database.
     * We have get user name and save to list variable,now we get every user specific information.
     *
     * @return The number of users saved to local database successfully.
     */
    private int crawlSaveUsers() {
        int crawlNum = 0;
        ArrayList<User> list = new ArrayList<>();
        try {
            for (String username : userList) {
                try {
                    URLConnection connection = new URL(userUrlPrefix + username).openConnection();
                    connection.setConnectTimeout(3000);
                    connection.setRequestProperty("Authorization", token);
                    connection.connect();

                    // check http response headers and handle exception
                    if (!connection.getHeaderField("Status").equals("200 OK")) {
                        httpDenyHandler(connection);
                        return 0;
                    }

                    try (InputStream inStream = connection.getInputStream();
                         BufferedReader reader = new BufferedReader(new InputStreamReader(inStream))) {

                        ObjectMapper mapper = new ObjectMapper();
                        User user = mapper.readValue(reader, User.class);
                        list.add(user);
                    }
                } catch (IOException e) {
                    logger.catching(e);
                }
            }

            try (SqlSession session = sessionFactory.openSession()) {
                crawlNum = session.insert("User.insertList", list);
                session.commit();
            }
        } finally {
            userList.clear();
            logger.info(crawlNum + " crawled,since variable(next id): " + since);
        }

        return crawlNum;
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
                logger.info("X-RateLimit-Remaining is limited.Sleep now on...it'll last for \" + (resetTime - current + 2000) + \" millisecond\"");
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
                logger.error("UserCrawl have to stop : " + status + "\n"+ sb.toString());
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                running = false;
                userList.clear();
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
