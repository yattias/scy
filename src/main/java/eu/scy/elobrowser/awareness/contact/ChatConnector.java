/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.elobrowser.awareness.contact;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Callback.Command;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Mock ChatConnector that directly accesses the tuplespace for 
 * sending and receiving messages.
 * 
 * The tupleformat looks like this:
 * (<sender>, <receiver>, <message>)
 *
 * @author weinbrenner
 */
public class ChatConnector implements Callback {

    private Map<String, ChatReceiver> recvs;

    private String user;

    private TupleSpace ts;

    private ChatInitiator initiator;

    public ChatConnector() {
        this.recvs = new HashMap<String, ChatReceiver>();
    }
    
    public ChatConnector(ChatInitiator initiator) {
        this();
        setChatInitiator(initiator);
    }

    public void setChatInitiator(ChatInitiator initiator) {
        this.initiator = initiator;
    }

    public void register(String user) throws TupleSpaceException {
        this.user = user;
        ts = new TupleSpace("scy.collide.info", 2525, "TFA_CHAT_SPACE");
        ts.eventRegister(Command.WRITE, new Tuple(String.class, user, String.class), this, true);
        System.out.println("Registered for chat messages -> " + user);
    }

    public void sendMessage(String receiver, String message) {
        try {
            System.out.println("Sending message " + message + " to " + receiver);
            ts.write(new Tuple(user, receiver, message));
        } catch (TupleSpaceException ex) {
            Logger.getLogger(ChatConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void addChatReceiver(ChatReceiver cr, String name) {
        recvs.put(name, cr);
    }

    @Override
    public void call(Command cmd, int seq, Tuple afterTuple, Tuple beforeTuple) {
        System.out.println("Received message: " + afterTuple);
        String sender = afterTuple.getField(0).getValue().toString();
        String text = afterTuple.getField(2).getValue().toString();
        ChatReceiver cr = recvs.get(sender);
        if (cr != null) {
            cr.receiveMessage(sender, text);
        } else {
            System.out.println("No chat receiver found, initating new chat with" + sender);
            initiator.startChat(sender, text);
        }
    }

}
