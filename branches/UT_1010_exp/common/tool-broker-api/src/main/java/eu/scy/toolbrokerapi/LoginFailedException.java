/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.toolbrokerapi;

/**
 *
 * @author sikken
 */
public class LoginFailedException extends RuntimeException {

   private String userName;

   public LoginFailedException(String userName){
      super();
      this.userName = userName;
   }

   public String getUserName()
   {
      return userName;
   }
}
