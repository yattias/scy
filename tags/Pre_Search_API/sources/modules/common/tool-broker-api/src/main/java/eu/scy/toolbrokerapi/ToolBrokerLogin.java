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
public interface ToolBrokerLogin
{

   /**
    * Gives the ToolBrokerLogin instance the opportunity to prepare itself for login calls
    */
   public void prepare();

   /**
    * does a user name and password check. This action should be quick, after this call
    * getReadyForUser for user will be called. The creation of the ToolBrokerAPI and setting up of
    * the connections, should be done in getReadyForUser.
    * 
    * If the username/password is incorrect a LoginFailedException exception will be thrown
    * 
    * @param username
    * @param password
    * @return Object - result of login, to be used in getReadyForUser
    * @throws LoginFailedException
    */
   public Object login(String userName, String password) throws LoginFailedException;

   /**
    * finish the login and return a instance of the ToolBrokerAPI. The time consuming part of the login should be done here.
    * 
    * @param object - result of the login call
    * @return ToolBrokerAPI instance
    */
   public ToolBrokerAPI getReadyForUser(Object object);
}
