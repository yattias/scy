/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.elobrowser.main;

import eu.scy.elobrowser.main.user.User;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import org.apache.log4j.Logger;

/**
 *
 * @author Henrik
 */
public class ScyLoginManager {
	private final static Logger logger = Logger.getLogger(ScyLoginManager.class);

    public static final Integer LOGIN_FAILED = 0;
    public static final Integer LOGIN_OK = 1;

    public Integer login(String username, String password) {
		 try{
        ToolBrokerAPI toolBroker = new ToolBrokerImpl();
        User.instance.setUsername(username);
        User.instance.setPassword(password);
        //SessionManager sessioManager = toolBroker.getUserSession(username, password);
//        if(sessioManager == null)  {
//            System.out.println("LOGIN FAILED!!");
//            return LOGIN_FAILED;
//        }
		 }
        catch (Throwable e){
			logger.error("error login, " + e.getMessage());
		 }
        return LOGIN_OK;
    }
}
