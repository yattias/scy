package eu.scy.client.desktop.localtoolbroker;


import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import eu.scy.toolbrokerapi.LoginFailedException;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

public class LocalMultiUserToolBrokerLoginTest
{
   private final String localToolBrokerLoginConfigFile = "/config/localScyServices.xml";
   private final String enableLocalLoggingKey = "enableLocalLogging";
   private LocalMultiUserToolBrokerLogin localMultiUserToolBrokerLogin;

   @Before
   public void setUp()
   {
      localMultiUserToolBrokerLogin = new LocalMultiUserToolBrokerLogin();
      localMultiUserToolBrokerLogin.prepare();
      localMultiUserToolBrokerLogin.setSpringConfigFile(localToolBrokerLoginConfigFile);
      System.getProperties().put(enableLocalLoggingKey, "false");
   }

   @After
   public void tearDown()
   {
      if (localMultiUserToolBrokerLogin.usersDirectory!=null && localMultiUserToolBrokerLogin.usersDirectory.isDirectory()){
         TestUtils.deleteDirectory(localMultiUserToolBrokerLogin.usersDirectory,true);
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

   @Test
   public void testLogin4()
   {
      final String userName = "a";
      ToolBrokerAPI tbi = localMultiUserToolBrokerLogin.login(userName, userName);
      TestUtils.testToolBrokerAPI(tbi);
      Assert.assertEquals(userName,tbi.getLoginUserName());
   }
   


}
