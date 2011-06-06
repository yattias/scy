/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.localtoolbroker;

import java.io.File;

import eu.scy.client.desktop.localtoolbroker.accesschecker.AccessChecker;
import eu.scy.toolbrokerapi.LoginFailedException;
import eu.scy.toolbrokerapi.ToolBrokerLogin;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * @author sikken
 */
public class LocalToolBrokerLogin implements ToolBrokerLogin
{

   protected final String userNameKey = "userName";
   protected final String eloStoreDirectoryKey = "eloStoreDirectory";
   protected final String loggingDirectoryKey = "loggingDirectory";
   protected final String tbiBeanName = "toolBrokerAPI";
   protected final String eloStoreDirectoryName = "eloStore";
   protected final String loggingDirectoryName = "logging";
   private String springConfigFile;
   protected File eloStoreDirectory = new File(eloStoreDirectoryName);
   protected File loggingDirectory = new File(loggingDirectoryName);
   private AccessChecker accessChecker = new AccessChecker();

   public LocalToolBrokerLogin()
   {
      super();
      System.setProperty(eloStoreDirectoryKey, new File(eloStoreDirectoryName).getAbsolutePath());
      System.setProperty(loggingDirectoryKey, new File(loggingDirectoryName).getAbsolutePath());
      
   }

   public LocalToolBrokerLogin(File storeRoot)
   {
      super();
      System.setProperty(eloStoreDirectoryKey, new File(storeRoot,eloStoreDirectoryName).getAbsolutePath());
      System.setProperty(loggingDirectoryKey, new File(loggingDirectoryName).getAbsolutePath());
      
   }

   @Override
   public void prepare()
   {
      findGeneralDirectories();
   }
   
   public void setPasswordChecker(String name)
   {
      accessChecker.setPasswordChecker(name);
   }
   
   protected void findGeneralDirectories()
   {
      eloStoreDirectory = new File(System.getProperty(eloStoreDirectoryKey));
      checkGeneralDirectory(eloStoreDirectory, eloStoreDirectoryName);
      loggingDirectory = new File(System.getProperty(loggingDirectoryKey));
      checkGeneralDirectory(loggingDirectory, loggingDirectoryName);
      checkDirectoryWriteable(loggingDirectory, loggingDirectoryName);
   }

   protected void checkGeneralDirectory(File dir, String label)
   {
      if (!dir.exists())
      {
         throw new IllegalStateException(label + " directory does not exist: "
                  + dir.getAbsolutePath());
      }
      if (!dir.isDirectory())
      {
         throw new IllegalStateException(label + " directory is not a directory: "
                  + dir.getAbsolutePath());
      }
   }

   protected void checkDirectoryWriteable(File dir, String label)
   {
      try
      {
         File file = File.createTempFile("writeText", ".test", dir);
         file.delete();
      }
      catch (Exception e)
      {
         throw new IllegalStateException("Cannot write to the " + label
                  + " directory does not exist: " + dir.getAbsolutePath());
      }
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
   public Object login(String userName, String password) throws LoginFailedException
   {
      if (userName == null || userName.length() == 0)
      {
         throw new LoginFailedException("user name not be empty");
      }
      String usedUserName = userName.trim();
      try
      {
         accessChecker.checkAndCleanName(userName);
      }
      catch (IllegalArgumentException e)
      {
         throw new LoginFailedException(e.getMessage());
      }
      loginCheck(usedUserName, password);
      return usedUserName;
   }

   protected void loginCheck(String userName, String password) throws LoginFailedException
   {
      if (!accessChecker.isAccessAllowed(userName, "", password))
      {
         throw new LoginFailedException("user name and/or password not valid");
      }
   }

   @Override
   public ToolBrokerAPI getReadyForUser(Object object)
   {
      final String usedUserName = (String) object;
      System.setProperty(userNameKey, usedUserName);
      return readTbiFromSpringConfig();
   }

   private ToolBrokerAPI readTbiFromSpringConfig()
   {
      System.setProperty(eloStoreDirectoryKey, eloStoreDirectory.getAbsolutePath());
      System.setProperty(loggingDirectoryKey, loggingDirectory.getAbsolutePath());

      ApplicationContext context;
      try
      {
         context = new ClassPathXmlApplicationContext(springConfigFile);
      }
      catch (BeansException e)
      {
         throw new IllegalStateException("cannot load LocalToolBrokerLogin spring config file: "
                  + springConfigFile + "\nError: " + e.getMessage(), e);
      }
      ToolBrokerAPI tbi = (ToolBrokerAPI) context.getBean(tbiBeanName);
      if (tbi == null)
      {
         throw new IllegalStateException("cannot get bean named " + tbiBeanName
                  + " from spring config file: " + springConfigFile);
      }
      return tbi;
   }
}
