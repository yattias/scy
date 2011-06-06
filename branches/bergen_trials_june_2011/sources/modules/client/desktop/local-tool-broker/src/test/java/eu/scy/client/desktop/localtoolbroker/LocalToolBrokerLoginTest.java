/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.localtoolbroker;

import java.io.File;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import roolo.elo.api.IELO;
import roolo.search.ISearchResult;
import eu.scy.toolbrokerapi.LoginFailedException;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 *
 * @author SikkenJ
 */
public class LocalToolBrokerLoginTest extends CommonToolBrokerLoginTest
{

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
      System.out.println("localToolBrokerLogin: " + localToolBrokerLogin);
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

   @Test
   public void testLogin10()
   {
      final String userName = "abc";
      Object object = localToolBrokerLogin.login(userName, userName);
      ToolBrokerAPI tbi = localToolBrokerLogin.getReadyForUser(object);
      TestUtils.testToolBrokerAPI(tbi);
      Assert.assertEquals(userName,tbi.getLoginUserName());
      Assert.assertTrue(localToolBrokerLogin.loggingDirectory.listFiles().length>0);
      List<IELO> allElos = tbi.getRepository().retrieveAllELOs();
      Assert.assertFalse(allElos.isEmpty());
   }
   
}
