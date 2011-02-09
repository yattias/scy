package eu.scy.client.desktop.localtoolbroker;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import roolo.elo.api.IELO;
import roolo.search.ISearchResult;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

public class LocalMultiUserToolBrokerLoginTest extends CommonToolBrokerLoginTest
{
   private final String userNameA = "userA";
   private final String userNameB = "userB";
   private LocalMultiUserToolBrokerLogin localMultiUserToolBrokerLogin;
   private ToolBrokerAPI tbi = null;
   
   @BeforeClass
   public static void setUpClass()
   {
      System.out.println("setUpClass");
   }

   @AfterClass
   public static void tearDownClass() throws Exception
   {
      System.out.println("tearDownClass");
   }

   @Before
   public void setUp()
   {
      System.out.println("setUp");
      localMultiUserToolBrokerLogin = new LocalMultiUserToolBrokerLogin();
      localToolBrokerLogin = localMultiUserToolBrokerLogin;
      localMultiUserToolBrokerLogin.prepare();
      localMultiUserToolBrokerLogin.setSpringConfigFile(localToolBrokerLoginConfigFile);
      System.getProperties().put(enableLocalLoggingKey, "true");
   }

   @After
   public void tearDown() throws IOException
   {
      System.out.println("tearDown");
      try
      {
         closeTbi();
      }
      finally
      {
         if (localMultiUserToolBrokerLogin.usersDirectory != null
                  && localMultiUserToolBrokerLogin.usersDirectory.isDirectory())
         {
            TestUtils.deleteDirectory(localMultiUserToolBrokerLogin.usersDirectory, true);
         }
      }
   }

   @Test
   public void testLogin10() throws IOException
   {
      Object object = localMultiUserToolBrokerLogin.login(userNameA, userNameA);
      tbi = localMultiUserToolBrokerLogin.getReadyForUser(object);
      TestUtils.testToolBrokerAPI(tbi);
      Assert.assertEquals(userNameA, tbi.getLoginUserName());
      checkDirectoryCopies(true);
      Assert.assertEquals(1, localMultiUserToolBrokerLogin.loggingDirectory.listFiles().length);
      List<IELO> allElos = tbi.getRepository().retrieveAllELOs();
      Assert.assertFalse(allElos.isEmpty());
      // simulate some actions
      TestUtils.deleteDirectory(localMultiUserToolBrokerLogin.eloStoreDirectory.listFiles()[0],
               true);
      DirectoryUtils.copyFile(new File("pom.xml"), new File(
               localMultiUserToolBrokerLogin.eloStoreDirectory, "test.xml"));
      IAction action = new Action();
      action.addContext(ContextConstants.tool, "unittest");
      action.addContext(ContextConstants.mission, "test");
      tbi.getActionLogger().log(action);
      closeTbi();
      // login again
      object = localMultiUserToolBrokerLogin.login(userNameA, userNameA);
      tbi = localMultiUserToolBrokerLogin.getReadyForUser(object);
      TestUtils.testToolBrokerAPI(tbi);
      Assert.assertEquals(userNameA, tbi.getLoginUserName());
      checkDirectoryCopies(false);
      Assert.assertEquals(2, localMultiUserToolBrokerLogin.loggingDirectory.listFiles().length);
      allElos = tbi.getRepository().retrieveAllELOs();
      Assert.assertFalse(allElos.isEmpty());
      closeTbi();
      // login under a different name
      object = localMultiUserToolBrokerLogin.login(userNameB, userNameB);
      tbi = localMultiUserToolBrokerLogin.getReadyForUser(object);
      Assert.assertEquals(userNameB, tbi.getLoginUserName());
      checkDirectoryCopies(true);
      Assert.assertEquals(1, localMultiUserToolBrokerLogin.loggingDirectory.listFiles().length);
      allElos = tbi.getRepository().retrieveAllELOs();
      Assert.assertFalse(allElos.isEmpty());
   }

   private void checkDirectoryCopies(boolean identical)
   {
      long masterEloStoreByes = TestUtils
               .getDirectoryBytes(localMultiUserToolBrokerLogin.masterEloStoreDirectory);
      int masterEloStoreFiles = TestUtils
               .getDirectoryFileCount(localMultiUserToolBrokerLogin.masterEloStoreDirectory);
      long userEloStoreByes = TestUtils
               .getDirectoryBytes(localMultiUserToolBrokerLogin.eloStoreDirectory);
      int userEloStoreFiles = TestUtils
               .getDirectoryFileCount(localMultiUserToolBrokerLogin.eloStoreDirectory);
      boolean directoriesEqual = masterEloStoreByes == userEloStoreByes
               && masterEloStoreFiles == userEloStoreFiles;
      if (identical)
      {
         Assert.assertTrue(directoriesEqual);
      }
      else
      {
         Assert.assertFalse(directoriesEqual);
      }
   }

   private void closeTbi() throws IOException
   {
      closeTbi(tbi);
      tbi = null;
   }

   private void closeTbi(ToolBrokerAPI toolBrokerAPI) throws IOException
   {
      if (toolBrokerAPI instanceof Closeable)
      {
         ((Closeable) toolBrokerAPI).close();
      }
   }
}
