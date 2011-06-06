/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.dummy;

import eu.scy.toolbrokerapi.LoginFailedException;
import eu.scy.client.desktop.scydesktop.login.LoginValidator;

/**
 *
 * @author sikken
 */
public class DummyLoginValidator implements LoginValidator {

   @Override
   public String login(String username, String password) throws LoginFailedException
   {
      if (username==null || !username.equals(password)){
         throw new LoginFailedException("user name and/or password not valid");
      }
      return "sessionId";
   }

}
