/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxcopex.registration;

import eu.scy.notification.api.INotification;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Marjolaine
 */
public class CopexNotificationManager {

    private String message = "";
    private String toolId;
    private static final Logger logger = Logger.getLogger(CopexNotificationManager.class.getName());
    private final static String keyMessage = "message";
    
    public CopexNotificationManager(String toolId) {
        this.toolId = toolId;
    }

    public boolean processNotification(INotification notification) {
        if(notification != null && notification.getToolId() != null && notification.getToolId().equals(toolId)){
            logger.log(Level.INFO, "notification from {0}", notification.getSender());
            if(!message.equals(""))
                message +="\n";
            this.message += notification.getFirstProperty(keyMessage);
            return true;
        } else {
            return false;
        }
    }

    public String getNotification(){
        return message;
    }

    public void keepNotification(boolean keep){
        if(!keep){
            message = "";
        }
    }

}
