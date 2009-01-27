/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.elobrowser.main;

import eu.scy.sessionmanager.SessionManager;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 *
 * @author Henrik
 */
public class ScyLoginManager {

    public static final Integer LOGIN_FAILED = 0;
    public static final Integer LOGIN_OK = 1;

    public Integer login(String username, String password) {
        ToolBrokerAPI toolBroker = new ToolBrokerImpl();
        SessionManager sessioManager = toolBroker.getUserSession(username, password);
        if(sessioManager == null)  {
            System.out.println("LOGIN FAILED!!");
            return LOGIN_FAILED;
        }

        return LOGIN_OK;
    }

}
