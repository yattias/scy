package eu.scy.notification;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import eu.scy.notification.api.INotification;
import eu.scy.notification.api.INotificationSender;

public class NotificationSender implements INotificationSender {
    
    //default settings:
    private String host = "127.0.0.1";
    private int port = 2525;
    private String space = "notifications";
    //
    private TupleSpace ts = null;
    
    
    /**
     * creates a simple notification sender
     */
    public NotificationSender() {
        connect();
    }
    
    /**
     * creates a notification sender with specified host, port and SQLSpace
     * @param host	SQLSpaces host
     * @param port	SQLSpaces port
     * @param space	SQLSpaces space
     */
    public NotificationSender(String host, int port, String space) {
        this.host = host;
        this.port = port;
        this.space = space;
        connect();
    }
    
    /**
     * creates a connection
     */
    private void connect() {
        try {
            this.ts = new TupleSpace(host, port, space);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * sends a notification
     * @param user	user receiving the notification
     * @param tool	source sending the notification
     * @param notification	the notification
     */
    public void send(String user, String tool, INotification notification) {
        Field content = new Field(notification.getXML());
        Field timestamp = new Field(new java.sql.Timestamp(new java.util.Date().getTime()).toString());
        Tuple message = new Tuple(user, tool, timestamp, content);
        try {
            ts.write(message);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}
