package eu.scy.client.desktop.localtoolbroker;

import org.junit.Test;

import eu.scy.client.desktop.localtoolbroker.accesschecker.AccessChecker;
import eu.scy.toolbrokerapi.LoginFailedException;

public abstract class CommonToolBrokerLoginTest
{
   protected final String localToolBrokerLoginConfigFile = "/config/localScyServices.xml";
   protected final String enableLocalLoggingKey = "enableLocalLogging";
   protected LocalToolBrokerLogin localToolBrokerLogin;

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

   @Test(expected = IllegalArgumentException.class)
   public void testSetPasswordChecker1()
   {
      localToolBrokerLogin.setPasswordChecker(null);
   }

   @Test(expected = IllegalArgumentException.class)
   public void testSetPasswordChecker2()
   {
      localToolBrokerLogin.setPasswordChecker("");
   }

   @Test(expected = IllegalArgumentException.class)
   public void testSetPasswordChecker3()
   {
      localToolBrokerLogin.setPasswordChecker("abc");
   }

   @Test
   public void testSetPasswordChecker4()
   {
      testSetPasswordChecker(AccessChecker.GENERATOR_SAME);
      testSetPasswordChecker(AccessChecker.GENERATOR_REVERSE);
      testSetPasswordChecker(AccessChecker.GENERATOR_HASH_5);
      testSetPasswordChecker(AccessChecker.GENERATOR_HASH_6);
      testSetPasswordChecker(AccessChecker.GENERATOR_HASH_7);
      testSetPasswordChecker(AccessChecker.GENERATOR_HASH_8);
   }
   
   private void testSetPasswordChecker(String name){
      localToolBrokerLogin.setPasswordChecker(name);      
      localToolBrokerLogin.setPasswordChecker(name.toLowerCase());
      localToolBrokerLogin.setPasswordChecker(name.toUpperCase());
   }
}
