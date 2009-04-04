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

    private ChatReceiver recv;

    private String user;

    private TupleSpace ts;

    public ChatConnector(ChatReceiver recv, String user) throws TupleSpaceException {
        this.recv = recv;
        this.user = user;
        ts = new TupleSpace("scy.collide.info", 2525, "TFA_CHAT_SPACE");
        ts.eventRegister(Command.WRITE, new Tuple(String.class, user, String.class), this, false);
    }

    public void sendMessage(String receiver, String message) {
        try {
            ts.write(new Tuple(user, receiver, message));
        } catch (TupleSpaceException ex) {
            Logger.getLogger(ChatConnector.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void call(Command cmd, int seq, Tuple afterTuple, Tuple beforeTuple) {
        recv.receiveMessage(afterTuple.getField(0).getValue().toString(), afterTuple.getField(2).getValue().toString());
    }

}
