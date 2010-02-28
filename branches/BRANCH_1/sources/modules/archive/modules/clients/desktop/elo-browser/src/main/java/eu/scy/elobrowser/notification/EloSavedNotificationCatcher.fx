/*
 * EloSavedNotificationCatcher.fx
 *
 * Created on 3-apr-2009, 16:11:40
 */

package eu.scy.elobrowser.notification;

import eu.scy.notification.api.INotification;
import eu.scy.notification.api.INotificationCallback;
import eu.scy.notification.NotificationService;
import java.net.URI;
import javafx.lang.FX;

/**
 * @author sikkenj
 */

// place your code here
public class EloSavedNotificationCatcher extends INotificationCallback{

    public var eloSavedAction:function(eloUri:URI):Void;

    var eloUri:URI;

    public function register(){
        def notificationService = new NotificationService("scy.collide.info", 2525, "notifications");
        notificationService.registerCallback("roolo", this);
    }

    override function onNotification(notification :INotification) {
        var target = notification.getProperty("target");
        if (target=="elobrowser"){
            var savedEloUri = notification.getProperty("eloUri");
            if (savedEloUri!=null){
                println("received a notification of elo saved uri: {savedEloUri}");
                eloUri = new URI(savedEloUri);
                if (eloSavedAction!=null){
                     FX.deferAction(function() :Void {
                         eloSavedAction(eloUri);
                    });
                }


            }
        }
    }
}
