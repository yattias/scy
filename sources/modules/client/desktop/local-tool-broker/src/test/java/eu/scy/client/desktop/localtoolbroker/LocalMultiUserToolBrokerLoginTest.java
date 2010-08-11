package eu.scy.client.desktop.localtoolbroker;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.List;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import roolo.api.search.ISearchResult;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.toolbrokerapi.LoginFailedException;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

public class LocalMultiUserToolBrokerLoginTest
{
   private final String localToolBrokerLoginConfigFile = "/config/localScyServices.xml";
   private final String enableLocalLoggingKey = "enableLocalLogging";
   private final String userNameA = "userA";
   private final String userNameB = "userB";
   private LocalMultiUserToolBrokerLogin localMultiUserToolBrokerLogin;
   private ToolBrokerAPI tbi = null;

   @Before
   public void setUp()
   {
      localMultiUserToolBrokerLogin = new LocalMultiUserToolBrokerLogin();
      localMultiUserToolBrokerLogin.prepare();
      localMultiUserToolBrokerLogin.setSpringConfigFile(localToolBrokerLoginConfigFile);
      System.getProperties().put(enableLocalLoggingKey, "true");
   }

   @After
   public void tearDown() throws IOException
   {
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

   @Test(expected = LoginFailedException.class)
   public void testLogin1()
   {
      localMultiUserToolBrokerLogin.login(null, null);
   }

   @Test(expected = LoginFailedException.class)
   public void testLogin2()
   {
      localMultiUserToolBrokerLogin.login("", null);
   }

   @Test(expected = LoginFailedException.class)
   public void testLogin3()
   {
      localMultiUserToolBrokerLogin.login("a", null);
   }

   @Test(expected = LoginFailedException.class)
   public void testLogin4()
   {
      localMultiUserToolBrokerLogin.login("a", "a");
   }

   @Test(expected = LoginFailedException.class)
   public void testLogin5()
   {
      localMultiUserToolBrokerLogin.login("ab", "ab");
   }

   @Test(expected = LoginFailedException.class)
   public void testLogin6()
   {
      localMultiUserToolBrokerLogin.login(" ab", " ab");
   }

   @Test(expected = LoginFailedException.class)
   public void testLogin7()
   {
      localMultiUserToolBrokerLogin.login("ab/", "ab/");
   }

   @Test
   public void testLogin10() throws IOException
   {
      tbi = localMultiUserToolBrokerLogin.login(userNameA, userNameA);
      TestUtils.testToolBrokerAPI(tbi);
      Assert.assertEquals(userNameA, tbi.getLoginUserName());
      checkDirectoryCopies(true);
      Assert.assertEquals(1, localMultiUserToolBrokerLogin.loggingDirectory.listFiles().length);
      List<ISearchResult> searchResults = tbi.getRepository().search(null);
      Assert.assertFalse(searchResults.isEmpty());
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
      tbi = localMultiUserToolBrokerLogin.login(userNameA, userNameA);
      TestUtils.testToolBrokerAPI(tbi);
      Assert.assertEquals(userNameA, tbi.getLoginUserName());
      checkDirectoryCopies(false);
      Assert.assertEquals(2, localMultiUserToolBrokerLogin.loggingDirectory.listFiles().length);
      searchResults = tbi.getRepository().search(null);
      Assert.assertFalse(searchResults.isEmpty());
      closeTbi();
      // login under a different name
      tbi = localMultiUserToolBrokerLogin.login(userNameB, userNameB);
      Assert.assertEquals(userNameB, tbi.getLoginUserName());
      checkDirectoryCopies(true);
      Assert.assertEquals(1, localMultiUserToolBrokerLogin.loggingDirectory.listFiles().length);
      searchResults = tbi.getRepository().search(null);
      Assert.assertFalse(searchResults.isEmpty());
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
