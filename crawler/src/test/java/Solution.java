import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import xyz.antsgroup.langfashion.UserCrawler;
import xyz.antsgroup.langfashion.entity.User;

import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * @author ants_ypc
 * @version 1.0 4/8/16
 */
public class Solution {
    public static void main(String[] args) {
        String resource = "mybatis-config.xml";
        InputStream is = UserCrawler.class.getClassLoader().getResourceAsStream(resource);
        SqlSessionFactory sessionFactory = new SqlSessionFactoryBuilder().build(is, "development");
        SqlSession session = sessionFactory.openSession();
        List<User> list = new ArrayList<>();
        User a = new User();
        a.setLogin("ypc");
        a.setId(13);
        User b = new User();
        b.setLogin("ypc");
        b.setId(12);
        list.add(a);
        list.add(b);

//        session.insert("User.insertUser", a);
        for (User u : list) {
            System.out.println(u);
        }
        System.out.println(session.insert("User.insertBatch", list));
        session.commit();
        System.out.println("finish.........");
    }
}
