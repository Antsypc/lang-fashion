package xyz.antsgroup.langfashion; 

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After; 


/** 
 * UserCrawler Tester. 
 * 
 * @author <Authors name> 
 * @since <pre>Apr 8, 2016</pre> 
 * @version 1.0 
 */ 
public class UserCrawlerTest { 

    @Before
    public void before() throws Exception { 
    } 

    @After
    public void after() throws Exception { 
    }

    /**
     * Method: crawlUsers() 
     */
    @Test
    public void testCrawlUsers() throws Exception {
        new UserCrawler().crawlUsers();
    }
    
    /** 
     * Method: getUserByUrl(String since) 
     */ 
    @Test
    public void testGetUsersByUrl() throws Exception {
        new UserCrawler().getUsersByUrl("1");
    }

    /**
     * Method: crawlSaveUsers()
     */
    @Test
    public void testGetSpecialUser() throws Exception {
    }
    
    /** 
     * Method: setSinceFromLink(String link) 
     */ 
    @Test
    public void testSetSinceFromLink() throws Exception { 
        /* 
        try { 
           Method method = UserCrawler.getClass().getMethod("setSinceFromLink", String.class); 
           method.setAccessible(true); 
           method.invoke(<Object>, <Parameters>); 
        } catch(NoSuchMethodException e) { 
        } catch(IllegalAccessException e) { 
        } catch(InvocationTargetException e) { 
        } 
        */ 
        
    } 
    
} 
