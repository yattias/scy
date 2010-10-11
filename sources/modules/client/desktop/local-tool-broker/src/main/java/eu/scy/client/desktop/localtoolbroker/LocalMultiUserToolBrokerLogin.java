package eu.scy.client.desktop.localtoolbroker;

import java.io.File;
import java.io.IOException;

import eu.scy.toolbrokerapi.LoginFailedException;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

public class LocalMultiUserToolBrokerLogin extends LocalToolBrokerLogin
{
   private final String storeDirectoryName = "store";
   private final String usersDirectoryName = "users";
   
   private File storeDirectory;
   protected File masterEloStoreDirectory;
   protected File usersDirectory;
   
   private File userDirectory;
   

   public LocalMultiUserToolBrokerLogin()
   {
   }

   @Override
   protected void findGeneralDirectories()
   {
      storeDirectory = new File(storeDirectoryName);
      checkGeneralDirectory(storeDirectory,storeDirectoryName);
      masterEloStoreDirectory = new File(storeDirectory,eloStoreDirectoryName);
      checkGeneralDirectory(masterEloStoreDirectory,eloStoreDirectoryName);
      usersDirectory = new File(storeDirectory,usersDirectoryName);
      checkGeneralDirectory(usersDirectory,usersDirectoryName);
      checkDirectoryWriteable(usersDirectory,usersDirectoryName);
   }
   
   @Override
   public ToolBrokerAPI getReadyForUser(Object object)
   {
      final String usedUserName = (String) object;
      findUserDirectories(usedUserName);
      try
      {
         DirectoryUtils.copyDirectory(masterEloStoreDirectory, eloStoreDirectory);
      }
      catch (IOException e)
      {
         new LoginFailedException("failed to setup the local eloStore of the user, " + e.getMessage());
      }

      return super.getReadyForUser(object);
   }

//   @Override
//   protected void loginCheck(String userName, String password) throws LoginFailedException
//   {
//      super.loginCheck(userName, password);
//      // user name and password are checked and thus valid
//      findUserDirectories(userName);
//      try
//      {
//         DirectoryUtils.copyDirectory(masterEloStoreDirectory, eloStoreDirectory);
//      }
//      catch (IOException e)
//      {
//         new LoginFailedException("failed to setup the local eloStore of the user, " + e.getMessage());
//      }
//   }
//   
   private void findUserDirectories(String userName){
      userDirectory = findCreateDirectory(usersDirectory,userName,userName);
      eloStoreDirectory = findCreateDirectory(userDirectory,eloStoreDirectoryName,eloStoreDirectoryName);
      loggingDirectory = findCreateDirectory(userDirectory,loggingDirectoryName,loggingDirectoryName);
   }
   
   private File findCreateDirectory(File root, String name, String label){
      File dir = new File(root,name);
      if (!dir.exists()){
         if (!dir.mkdir()){
            throw new LoginFailedException("failed to create " + label + " directory: " + dir.getAbsolutePath());
          }
      }
      if (!dir.isDirectory()){
         throw new LoginFailedException("the " + label + " directory is not a directory: " + dir.getAbsolutePath());
      }
      return dir;
   }

}
