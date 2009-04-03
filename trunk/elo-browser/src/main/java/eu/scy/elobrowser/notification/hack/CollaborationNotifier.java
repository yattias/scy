/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.elobrowser.notification.hack;

import eu.scy.elobrowser.main.user.User;
import eu.scy.notification.Notification;
import eu.scy.notification.NotificationSender;

/**
 * Caution: EPIC hack ahead!!!
 * 
 * This class sends a notification to the other user to start collaboration!
 *
 * @author giemza
 */
public class CollaborationNotifier {

    private static NotificationSender notificationsender = new NotificationSender("scy.collide.info", 2525, "notifications");

    public static void notify(final String username, final String tool) {
        new Thread() {

            @Override
            public void run() {
                Notification n = new Notification();
                n.addProperty("initCollaboration", Boolean.toString(true));
                n.addProperty("username", User.instance.getUsername());
                // ONLY FOR TESTING PURPOSE, MUST BE COMMENTED OUT
                //notificationsender.send(User.instance.getUsername(), tool, n);
                notificationsender.send(username, tool, n);
                System.out.println("Notification send...");
            }

        }.start();
    }
}
