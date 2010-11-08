/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.tools.fxcopex.registration;

import eu.scy.notification.api.INotification;

/**
 *
 * @author Marjolaine
 */
public class CopexNotificationManager {

    private String message = "";
    
    public CopexNotificationManager() {
    }

    public void processNotification(INotification notification) {
        this.message = notification.getFirstProperty("message");
        // waiting for the format....
    }

    public String getNotification(){
        return message;
    }

}
