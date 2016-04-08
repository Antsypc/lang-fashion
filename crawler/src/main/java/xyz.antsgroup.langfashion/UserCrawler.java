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
 * Crawler user from github.com.
 *
 * @author ants_ypc
 * @version 1.0 4/7/16
 */
public class UserCrawler {
    private static final Logger logger = LogManager.getLogger("UserCrawler");
    private static final String usersUrlPrefix = "https://api.github.com/users?since=";
    private static final String userUrlPrefix = "https://api.github.com/users/";
    private static final String token = "token 08d6a152b9f329b88753b8dacce5d1448b258c12";

    private SqlSessionFactory sessionFactory;
    private String since = "";
    private ArrayList<String> userList = new ArrayList<>();
    private boolean running;


    /**
     * constructor UserCrawler.
     * Initialize the properties for mybatis and other works.It'll crawler user on github.com from the next one which
     * is not existing in the database.So, it read the max id from user table and initialize `since`.
     */
    public UserCrawler() {
        logger.info("UserCrawler constructor.");
        String resource = "mybatis-config.xml";
        InputStream is = UserCrawler.class.getClassLoader().getResourceAsStream(resource);
        sessionFactory = new SqlSessionFactoryBuilder().build(is, "development");
        try (SqlSession session = sessionFactory.openSession()){
            since = String.valueOf(((Integer) session.selectOne("User.getMaxUserId")).intValue());
        }
        running = true;
        System.out.println(since);
        logger.info("UserCrawler has initialized");
    }

    /**
     * Control crawler, meanwhile this is the only way supposed to running UserCrawler.
     *
     * @return The number of users this crawler got finally.
     */
    public int crawlUsers() {
        int crawlNum = 0;
        while (!since.isEmpty() && since != null && running) {
            System.out.println(crawlNum);
            System.out.println(System.currentTimeMillis());
            System.out.println("since:" + since);
            getUsersByUrl(since);
            crawlNum += crawlSaveUsers();
            System.out.println(System.currentTimeMillis());
        }
        return crawlNum;
    }

    /**
     * Get since which is next id of user.We need to get it from HTTP response header Link.
     *
     * @param link HTTP response header Link.
     */
    private void setSinceFromLink(String link) {
        Pattern pattern = Pattern.compile("<https://api.github.com/users\\?since=([0-9]*)>.*");
        Matcher matcher = pattern.matcher(link);
        since = matcher.matches() ? matcher.group(1) : null;
        System.out.println("parse since:" + since);
    }


    /**
     * Get login name of user.Because we cannot get enough information of users
     * by the api https://api.github.com/users?since= , we have to get login name and save to list temporarily.
     *
     * @param since The user id of this time to start.
     */
    public void getUsersByUrl(String since) {
        try {
            URLConnection connection = new URL(usersUrlPrefix + since).openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestProperty("Authorization", token);
            connection.connect();

            // check http response status code
//            System.out.println(connection.getHeaderField("Status"));    // 200 OK
//            System.out.println(connection.getHeaderField("Link"));      // <https://api.github.com/users?since=368>; rel="next", <https://api.github.com/users{?since}>; rel="first"
//            System.out.println(connection.getHeaderField("X-RateLimit-Remaining"));
//            System.out.println(connection.getHeaderField("X-RateLimit-Reset"));

            setSinceFromLink(connection.getHeaderField("Link"));

            try (InputStream inStream = connection.getInputStream();
                 BufferedReader reader = new BufferedReader(new InputStreamReader(inStream))) {

                ObjectMapper mapper = new ObjectMapper();
                User[] users = mapper.readValue(reader, User[].class);

                // test if json parse ok
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
    public int crawlSaveUsers() {
        int crawlNum;
        ArrayList<User> list = new ArrayList<>();
        for (String username : userList) {
            try {
                URLConnection connection = new URL(userUrlPrefix + username).openConnection();
                connection.setConnectTimeout(3000);
                connection.setRequestProperty("Authorization", token);
                connection.connect();

                // check http response status code
//                System.out.println(connection.getHeaderField("Status"));    // 200 OK
//                System.out.println(connection.getHeaderField("X-RateLimit-Remaining"));
//                System.out.println(connection.getHeaderField("X-RateLimit-Reset"));

                try (InputStream inStream = connection.getInputStream();
                     BufferedReader reader = new BufferedReader(new InputStreamReader(inStream))) {

                    ObjectMapper mapper = new ObjectMapper();
                    User user = mapper.readValue(reader, User.class);
                    list.add(user);
                    System.out.println(user.getLogin() + " " + user.getId());
                }
            } catch (IOException e) {
                logger.catching(e);
            }
        }

        try (SqlSession session = sessionFactory.openSession()) {
            crawlNum = session.insert("User.insertBatch", list);
            session.commit();
        } finally {
            userList.clear();
        }
        return crawlNum;
    }

    public boolean isRunning() {
        return running;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }
}
