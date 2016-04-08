package xyz.antsgroup.langfashion;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

/**
 * @author ants_ypc
 * @version 1.0 3/31/16
 */
public class OAuthServlet extends HttpServlet {

    private static final Logger logger = LogManager.getLogger("OAuthServlet");
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Enumeration<String> headers = request.getAttributeNames();
        String head;
        logger.entry();
        while (headers.hasMoreElements()) {
            head = headers.nextElement();
            logger.info(head + ":" + request.getHeader(head));

        }
        logger.exit();

        String code = request.getParameter("code");
        if (code == null)
            response.sendRedirect("https://github.com/login/oauth/authorize?client_id=278c8bd9ee30db811671");
        else {
//            request.setAttribute("client_id","278c8bd9ee30db811671");
//            request.setAttribute("client_secret", "2d9efc2b18728b8dceffd31b95a9f3dcdf7e80ed");
//            request.setAttribute("code", code);
//            request.getRequestDispatcher("https://github.com/login/oauth/access_token").forward(request, response);

            URLConnection connection = new URL("https://github.com/login/oauth/access_token").openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            httpConnection.setRequestMethod("POST");
            httpConnection.setDoInput(true);
            httpConnection.setDoOutput(true);
            httpConnection.setUseCaches(false);
            httpConnection.connect();

            logger.entry("\naccess_code:");
            try (PrintWriter out = new PrintWriter(httpConnection.getOutputStream())) {
                out.print("client_id=278c8bd9ee30db811671" +
                        "&client_secret=2d9efc2b18728b8dceffd31b95a9f3dcdf7e80ed" +
                        "&code=" + code);
                out.flush();


                try (BufferedReader in = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()))){
                    String line;
                    while((line = in.readLine()) != null) {
                        logger.info(line);
                    }
                } catch (IOException e) {
                    logger.catching(e);
                }
            } catch (IOException e) {
                logger.catching(e);
            }
            logger.exit("\n");

        }
    }
}
