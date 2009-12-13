/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.login;

import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 *
 * @author sikken
 */
public class RemoteToolBrokerLogin implements ToolBrokerLogin {

   private String springConfigFile;

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
      try{
         if (springConfigFile!=null && springConfigFile.length()>0){
            return new ToolBrokerImpl(userName,password,springConfigFile);
         }
         return new ToolBrokerImpl(userName,password);
      }
      catch (Exception e){

      }
      throw new UnsupportedOperationException("Not supported yet.");
   }

}
