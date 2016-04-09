import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import xyz.antsgroup.langfashion.UserCrawler;
import xyz.antsgroup.langfashion.entity.Repo;
import xyz.antsgroup.langfashion.entity.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author ants_ypc
 * @version 1.0 4/8/16
 */
public class Solution {
    public static void main(String[] args) {
        try {
            URLConnection connection = new URL("https://api.github.com/users/Antsypc/repos?page=1&per_page=2").openConnection();
            connection.setConnectTimeout(3000);
            connection.setRequestProperty("Authorization", "token 08d6a152b9f329b88753b8dacce5d1448b258c12");
            connection.connect();

            // get http request headers
            Map<String, List<String>> headerFields = connection.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
                String key = entry.getKey();
                List<String> value = entry.getValue();
                System.out.println("key:" + key);
                for (String s : value) {
                    System.out.println(s);
                }
                System.out.println();
            }

            // check http response status code
            System.out.println(connection.getHeaderField("Status"));    // 200 OK
            System.out.println(connection.getHeaderField("Link"));      // <https://api.github.com/repositories?since=368>; rel="next", <https://api.github.com/repositories{?since}>; rel="first"
            System.out.println(connection.getHeaderField("X-RateLimit-Remaining"));
            System.out.println(connection.getHeaderField("X-RateLimit-Reset"));


            try (InputStream inStream = connection.getInputStream()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
//            logger.catching(e);
        }
    }
}

