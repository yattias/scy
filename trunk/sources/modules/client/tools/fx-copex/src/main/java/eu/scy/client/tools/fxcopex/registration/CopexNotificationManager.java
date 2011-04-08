/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxcopex.registration;

import eu.scy.notification.api.INotification;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * concantenation of the notification messages
 * @author Marjolaine
 */
public class CopexNotificationManager {

    private String message = "";
    private static final Logger logger = Logger.getLogger(CopexNotificationManager.class.getName());
    public final static String keyMessage = "message";
    
    public CopexNotificationManager() {
    }

    public boolean processNotification(INotification notification) {
        //if(notification != null && notification.getToolId() != null && notification.getToolId().equals(toolId)){
        if(notification != null) {
            //logger.log(Level.INFO, "notification from {0}", notification.getSender());
            this.message = notification.getFirstProperty(keyMessage);
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
