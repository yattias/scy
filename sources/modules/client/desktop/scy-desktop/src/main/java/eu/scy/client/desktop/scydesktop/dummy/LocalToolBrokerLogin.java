/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.dummy;

import eu.scy.toolbrokerapi.LoginFailedException;
import eu.scy.toolbrokerapi.ToolBrokerLogin;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author sikken
 */
public class LocalToolBrokerLogin implements ToolBrokerLogin
{

   private final String userNameKey = "userName";
   private final String tbiBeanName = "toolBrokerAPI";
   private String springConfigFile;

   public void prepare()
   {
   }

   public String getSpringConfigFile()
   {
      return springConfigFile;
   }

   public void setSpringConfigFile(String springConfigFile)
   {
      this.springConfigFile = springConfigFile;
   }

   @Override
   public ToolBrokerAPI login(String userName, String password) throws LoginFailedException
   {
      loginCheck(userName, password);
      System.setProperty(userNameKey, userName);
      return readTbiFromSpringConfig();
   }

   private void loginCheck(String userName, String password) throws LoginFailedException
   {
      if (userName == null || !userName.equals(password))
      {
         throw new LoginFailedException("user name and/or password not valid");
      }
   }

   private ToolBrokerAPI readTbiFromSpringConfig()
   {

      ApplicationContext context = new ClassPathXmlApplicationContext(springConfigFile);
      if (context == null)
      {
         throw new IllegalStateException("cannot load LocalToolBrokerLogin spring config file: " + springConfigFile);
      }
      ToolBrokerAPI tbi = (ToolBrokerAPI) context.getBean(tbiBeanName);
      if (tbi == null)
      {
         throw new IllegalStateException("cannot get bean named " + tbiBeanName + " from spring config file: " + springConfigFile);
      }
      return tbi;
   }
}
