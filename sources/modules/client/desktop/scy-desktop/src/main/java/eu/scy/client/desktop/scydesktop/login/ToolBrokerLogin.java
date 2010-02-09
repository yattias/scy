/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.login;

import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.toolbrokerapi.LoginFailedException;

/**
 *
 * @author sikken
 */
public interface ToolBrokerLogin {

   /**
    * does a user login and return a instance of the ToolBrokerAPI.
    *
    * If the login fails a LoginFailedException exception will be thrown
    * 
    * @param username
    * @param password
    * @return ToolBrokerAPI instance
    * @throws LoginFailedException
    */
   public ToolBrokerAPI login(String userName, String password) throws LoginFailedException;
}
