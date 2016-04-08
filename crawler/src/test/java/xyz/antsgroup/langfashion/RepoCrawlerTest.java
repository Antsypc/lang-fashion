package xyz.antsgroup.langfashion; 

import org.junit.Test; 
import org.junit.Before; 
import org.junit.After;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static org.junit.Assert.*;

/** 
 * RepoCrawler Tester. 
 * 
 * @author <Authors name> 
 * @since <pre>Apr 7, 2016</pre> 
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
     * Method: getRepoByUrl(String url) 
     */ 
    @Test
    public void testGetRepoByUrl() throws Exception {
        new RepoCrawler().getRepoByUrl("1");
    } 
    
    
    /** 
     * Method: setSinceFromLink(String link) 
     */ 
    @Test
    public void testSetSinceFromLink() throws Exception { 

        RepoCrawler repoCrawler = new RepoCrawler();
        Method method = RepoCrawler.class.getDeclaredMethod("setSinceFromLink", String.class);
        method.setAccessible(true);
        method.invoke(repoCrawler, "<https://api.github.com/repositories?since=368>; rel=\"next\", <https://api.github.com/repositories{?since}>; rel=\"first\"");
        Field field = RepoCrawler.class.getDeclaredField("since");
        field.setAccessible(true);
        System.out.println((String)field.get(repoCrawler));
    } 
    
} 
