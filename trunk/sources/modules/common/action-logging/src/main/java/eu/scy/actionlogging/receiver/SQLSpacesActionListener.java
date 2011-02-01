package eu.scy.actionlogging.receiver;

import java.io.IOException;

import org.jdom.JDOMException;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Tuple;
import eu.scy.actionlogging.logger.Action;

public class SQLSpacesActionListener implements Callback {
    
    //default settings:
    private String host = "127.0.0.1";
    private int port = 2525;
    private String space = "actionlogging";
    //
    //	private Field user = null;
    private TupleSpace ts = null;
    private ActionReceiver ar = null;
    
    /**
     * simple constructor expecting an ActionReceiver
     * the action listener will catch any action that arrives.
     * @param ar	ActionReceiver processing incoming actions
     */
    public SQLSpacesActionListener(ActionReceiver ar) {
        this.ar = ar;
        //		this.user = new Field(user);
        connect();
    }
    
    /**
     * extended constructor expecting an ActionReceiver and a bunch of other things.
     * @param ar	ActionReceiver processing incoming actions
     * @param host	SQLSpaces host
     * @param port	SQLSpaces port
     * @param space	SQLSpace used
     */
    public SQLSpacesActionListener(ActionReceiver ar, String host, int port, String space) {
        this.ar = ar;
        //		this.user = new Field(user);
        this.host = host;
        this.port = port;
        this.space = space;
        connect();
    }
    
    /**
     * creates a connection to the server
     */
    private void connect() {
        try {
            ts = new TupleSpace(host, port, space);
            Tuple mask = new Tuple(String.class, String. class, String.class, String.class); //filter?!
            //						user			tool		timestamp		xml
            int seqNo = ts.eventRegister(Command.WRITE, mask, this, false);
            while(true) {}
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * 	SQLSpaces action -> SQLSpaces doku
     */
    public void call(Command arg0, int arg1, Tuple x, Tuple y) {
        try {
			ar.notifyCallbacks(new Action((String)x.getField(3).getValue()));
		} catch (JDOMException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        // System.out.println((String)x.getField(3).getValue());
    }
    
}
