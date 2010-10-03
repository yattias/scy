/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.login;

import eu.scy.toolbrokerapi.LoginFailedException;

/**
 *
 * @author sikken
 */
public interface LoginValidator {

   /**
    *
    * @param username
    * @param password
    * @return sessionId String
    * @throws LoginFailedException
    */
   public String login(String username, String password) throws LoginFailedException;
}
