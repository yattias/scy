package eu.scy.notification;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.Vector;

import eu.scy.notification.api.INotification;
import eu.scy.notification.api.INotificationCallback;
import eu.scy.notification.api.INotificationService;

public class NotificationService implements INotificationService, Callback {
    
    //default settings:
    private String host = "127.0.0.1";
    private int port = 2525;
    private String space = "notifications";
    
    private Vector<INotificationCallback> callbacks = new Vector<INotificationCallback>();
    private TupleSpace ts = null;
    
    /**
     * simple constructor.
     * @param user	username used as filter
     */
    public NotificationService() {
        connect();
    }
    
    /**
     * 
     * @param host	SQLSpaces host
     * @param port	SQLSpaces port
     * @param space	SQLSpaces space
     * @param user user name used as filter
     */
    public NotificationService(String host, int port, String space) {
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
            ts = new TupleSpace(host, port, space);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public boolean deregisterCallback(INotificationCallback callback) {
        // FIXME: deregister callback from tuplespace!!!!!!!!!!!!!
        return callbacks.remove(callback);
    }
    
    @Override
    public void notifyCallbacks(INotification notification) {
        for(INotificationCallback item : callbacks) {
            item.onNotification(notification);
        }
    }
    
    @Override
    public void registerCallback(String user, INotificationCallback callback) {
        Tuple mask = new Tuple(user, String.class, String.class, String.class);
        try {
            ts.eventRegister(Command.WRITE, mask, this, false);
            callbacks.add(callback);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * SQLSpaces method -> see SQLSpaces documentation
     */
    public void call(Command arg0, int arg1, Tuple x, Tuple arg3) {
        notifyCallbacks(new Notification((String)x.getField(3).getValue()));
    }
    
}
