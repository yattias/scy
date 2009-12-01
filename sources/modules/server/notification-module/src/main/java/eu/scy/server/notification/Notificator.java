package eu.scy.server.notification;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import info.collide.sqlspaces.commons.Callback.Command;

import java.util.ArrayList;
import java.util.Properties;

import eu.scy.notification.Notification;
import eu.scy.notification.api.INotification;

public class Notificator {

    private TupleSpace commandSpace;

    private String COMMAND_SPACE = "command";

    private ArrayList<Integer> callbacks;

    private static final String NOTIFICATION = "notification";

    public Notificator(String host, int port) {
        try {
            commandSpace = new TupleSpace(new User(Notificator.class.getName()), host, port, false, false, COMMAND_SPACE);
            init();
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }

    }

    private void init() throws TupleSpaceException {
        callbacks = new ArrayList<Integer>();
        SessionCallback cb = new SessionCallback();
        Tuple tupleTemplate = new Tuple(NOTIFICATION, Field.createWildCardField());
        callbacks.add(commandSpace.eventRegister(Command.WRITE, tupleTemplate, cb, false));
    }

    class SessionCallback implements Callback {

        @Override
        public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
            processNotification(afterTuple);
        }

    }

    private synchronized void processNotification(Tuple notificationTuple) {
        // "notification":String, <User>:String, <Receiver>:String, <Sender>:String, <Mission>:String, <Session>:String, <Key=Value>:String*)
        String uniqueID =(String) notificationTuple.getField(1).getValue();
        String userName = (String) notificationTuple.getField(2).getValue();
        String receiver = (String) notificationTuple.getField(3).getValue();
        String sender = (String) notificationTuple.getField(4).getValue();
        String mission = (String) notificationTuple.getField(5).getValue();
        String session = (String) notificationTuple.getField(6).getValue();
        Properties props = new Properties();
        for (int i = 7; i < notificationTuple.getNumberOfFields(); i++) {
            String keyValue = (String) notificationTuple.getField(i).getValue();
            int index = keyValue.indexOf('=');
            String key = keyValue.substring(0, index - 1);
            String value = keyValue.substring(index + 1, keyValue.length());
            props.setProperty(key, value);

        }
        INotification notification = new Notification(uniqueID,sender, receiver, notificationTuple.getCreationTimestamp(), mission, session, props);

        // TODO send via xmpp

    }
}
