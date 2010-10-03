/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.toolbrokerapi;

/**
 * Interface used by SCY-Desktop do the login
 * 
 * @author sikken
 */
public interface ToolBrokerLogin {
   
   /**
    * Gives the ToolBrokerLogin instance the opportunity to prepare itself for login calls
    */
   public void prepare();

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
