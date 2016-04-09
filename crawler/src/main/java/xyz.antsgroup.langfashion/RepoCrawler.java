package xyz.antsgroup.langfashion;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import xyz.antsgroup.langfashion.entity.Repo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
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

    private String url = "https://api.github.com/repositories?since=";
    private String since = "";
    private String token = "token 08d6a152b9f329b88753b8dacce5d1448b258c12";


    private void setSinceFromLink(String link) {
        Pattern pattern = Pattern.compile("<https://api.github.com/repositories\\?since=([0-9]*)>.*");
        Matcher matcher = pattern.matcher(link);
        since = matcher.matches() ? matcher.group(1) : null;
    }


    public void getRepoByUrl(String since) {
//        StringBuilder sb = new StringBuilder();
        try {
            URLConnection connection = new URL(url + since).openConnection();
            connection.setConnectTimeout(3000);
//            connection.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//            connection.setRequestProperty("User-Agent", "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36" +
//                    " (KHTML, like Gecko) Chrome/49.0.2623.87 Safari/537.36");
            connection.setRequestProperty("Authorization", token);
            connection.connect();

            // get http request headers
//            Map<String, List<String>> headerFields = connection.getHeaderFields();
//            for (Map.Entry<String, List<String>> entry : headerFields.entrySet()) {
//                String key = entry.getKey();
//                List<String> value = entry.getValue();
//                System.out.println("key:" + key);
//                for (String s : value) {
//                    System.out.println(s);
//                }
//                System.out.println();
//            }

            // check http response status code
            System.out.println(connection.getHeaderField("Status"));    // 200 OK
            System.out.println(connection.getHeaderField("Link"));      // <https://api.github.com/repositories?since=368>; rel="next", <https://api.github.com/repositories{?since}>; rel="first"
            setSinceFromLink(connection.getHeaderField("Link"));
            System.out.println(connection.getHeaderField("X-RateLimit-Remaining"));
            System.out.println(connection.getHeaderField("X-RateLimit-Reset"));


            try (InputStream inStream = connection.getInputStream()) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inStream));
                ObjectMapper mapper = new ObjectMapper();
                mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);  // to prevent exception when encountering unknown property
                Repo[] repos = mapper.readValue(reader, Repo[].class);

                // test if json parse ok
                for (Repo r : repos) {
                    System.out.println(r);
                }

                String line;
                while ((line = reader.readLine()) != null) {
                    logger.info(line + "\n");
                }
            }
        } catch (IOException e) {
            logger.catching(e);
        }

//        return sb;
    }
}
