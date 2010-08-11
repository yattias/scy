/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.localtoolbroker;

import java.io.File;
import java.util.List;

import junit.framework.Assert;
import eu.scy.toolbrokerapi.LoginFailedException;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import roolo.api.search.ISearchResult;
import static org.junit.Assert.*;

/**
 *
 * @author SikkenJ
 */
public class LocalToolBrokerLoginTest
{
   private final String localToolBrokerLoginConfigFile = "/config/localScyServices.xml";
   private final String enableLocalLoggingKey = "enableLocalLogging";
   private LocalToolBrokerLogin localToolBrokerLogin;

   public LocalToolBrokerLoginTest()
   {
   }

   @BeforeClass
   public static void setUpClass() throws Exception
   {
   }

   @AfterClass
   public static void tearDownClass() throws Exception
   {
   }

   @Before
   public void setUp()
   {
      localToolBrokerLogin = new LocalToolBrokerLogin();
      localToolBrokerLogin.prepare();
      localToolBrokerLogin.setSpringConfigFile(localToolBrokerLoginConfigFile);
      System.getProperties().put(enableLocalLoggingKey, "false");
   }

   @After
   public void tearDown()
   {
   }
   
   @Test(expected = IllegalStateException.class)
   public void testPrepare1()
   {
      System.setProperty(localToolBrokerLogin.eloStoreDirectoryKey, new File("not existing").getAbsolutePath());
      localToolBrokerLogin.prepare();
   }
  
   @Test(expected = IllegalStateException.class)
   public void testPrepare2()
   {
      System.setProperty(localToolBrokerLogin.eloStoreDirectoryKey, new File("pom.xml").getAbsolutePath());
      localToolBrokerLogin.prepare();
   }
  
   @Test(expected = IllegalStateException.class)
   public void testPrepare3()
   {
      System.setProperty(localToolBrokerLogin.loggingDirectoryKey, new File("not existing").getAbsolutePath());
      localToolBrokerLogin.prepare();
   }

   @Test(expected = IllegalStateException.class)
   public void testPrepare4()
   {
      System.setProperty(localToolBrokerLogin.loggingDirectoryKey, new File("pom.xml").getAbsolutePath());
      localToolBrokerLogin.prepare();
   }

   @Test(expected = LoginFailedException.class)
   public void testLogin1()
   {
      localToolBrokerLogin.login(null, null);
   }

   @Test(expected = LoginFailedException.class)
   public void testLogin2()
   {
      localToolBrokerLogin.login("", null);
   }

   @Test(expected = LoginFailedException.class)
   public void testLogin3()
   {
      localToolBrokerLogin.login("a", null);
   }

   @Test(expected = LoginFailedException.class)
   public void testLogin4()
   {
      localToolBrokerLogin.login("a", "a");
   }

   @Test(expected = LoginFailedException.class)
   public void testLogin5()
   {
      localToolBrokerLogin.login("ab", "ab");
   }

   @Test(expected = LoginFailedException.class)
   public void testLogin6()
   {
      localToolBrokerLogin.login(" ab", " ab");
   }

   @Test(expected = LoginFailedException.class)
   public void testLogin7()
   {
      localToolBrokerLogin.login("ab/", "ab/");
   }

   @Test
   public void testLogin10()
   {
      final String userName = "abc";
      ToolBrokerAPI tbi = localToolBrokerLogin.login(userName, userName);
      TestUtils.testToolBrokerAPI(tbi);
      Assert.assertEquals(userName,tbi.getLoginUserName());
      Assert.assertTrue(localToolBrokerLogin.loggingDirectory.listFiles().length>0);
      List<ISearchResult> searchResults = tbi.getRepository().search(null);
      Assert.assertFalse(searchResults.isEmpty());
   }
   
}
