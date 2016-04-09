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
        try {
            if(1 == 1) {
                System.out.println(System.currentTimeMillis());
                return;
            }
        } finally {
            System.out.println("finally");
        }
        System.out.println("last");
    }
}

