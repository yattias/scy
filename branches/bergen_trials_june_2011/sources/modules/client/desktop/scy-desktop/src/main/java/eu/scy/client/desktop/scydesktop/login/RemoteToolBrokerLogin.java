/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.login;

import eu.scy.toolbrokerapi.ToolBrokerLogin;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.toolbrokerapi.LoginFailedException;
import org.apache.log4j.Logger;

/**
 *
 * @author sikken
 */
public class RemoteToolBrokerLogin implements ToolBrokerLogin
{

   private final static Logger logger = Logger.getLogger(RemoteToolBrokerLogin.class);
   private String springConfigFile;

   @Override
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
   public Object login(String userName, String password) throws LoginFailedException
   {
//      if (springConfigFile != null && springConfigFile.length() > 0)
//      {
//         return new ToolBrokerImpl(userName, password, springConfigFile);
//      }
      return new ToolBrokerImpl(userName, password, true);
   }

   @Override
   public ToolBrokerAPI getReadyForUser(Object object)
   {
      final ToolBrokerImpl tbi = (ToolBrokerImpl) object;
      tbi.initializeServices(springConfigFile);
      return (ToolBrokerAPI) object;
   }
}
