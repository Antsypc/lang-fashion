package xyz.antsgroup.langfashion; 

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After;
import xyz.antsgroup.langfashion.entity.User;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

/** 
 * RepoCrawler Tester. 
 * 
 * @author <Authors name> 
 * @since <pre>Apr 9, 2016</pre> 
 * @version 1.0 
 */ 
public class RepoCrawlerTest { 

    @Before
    public void before() throws Exception { 
    } 

    @After
    public void after() throws Exception { 
    } 

    /** 
     * Method: crawlRepos() 
     */ 
    @Test
    public void testCrawlRepos() throws Exception { 
    } 

    
    /** 
     * Method: retrieveUser(int idFrom, int idTo) 
     */ 
    @Test
    public void testRetrieveUser() throws Exception { 

        try {
            RepoCrawler repoCrawler = new RepoCrawler();
           Method method = RepoCrawler.class.getDeclaredMethod("retrieveUser", int.class, int.class);
           method.setAccessible(true); 
           method.invoke(repoCrawler, 46, 70);
        } catch(NoSuchMethodException e) { 
        } catch(IllegalAccessException e) { 
        } catch(InvocationTargetException e) {
        } 

        
    } 
    
    /** 
     * Method: crawlUserReops(User user) 
     */ 
    @Test
    public void testCrawlUserReops() throws Exception {
        RepoCrawler repoCrawler = new RepoCrawler();
        User user = new User();
        user.setId(31);

        try {
            Method method = RepoCrawler.class.getDeclaredMethod("crawlUserReops", User.class);
            method.setAccessible(true);
            method.invoke(repoCrawler, user);
        } catch (NoSuchMethodException e) {
        } catch (IllegalAccessException e) {
        } catch (InvocationTargetException e) {
        }


    } 
    
} 
