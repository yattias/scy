package eu.scy.actionlogging.logger;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;

public class ActionLogger implements IActionLogger {
    
    //default settings:
    private String host = "127.0.0.1";
    private int port = 2525;
    private String space = "SCYSpace-actionlogging";
    //
    private TupleSpace ts = null;
    
    /**
     * simple constructor for an actionlogger
     * @param user	user throwing actions (NOT! the tool)
     */
    public ActionLogger() {
        connect();
    }
    
    /**
     * extended constructor
     * @param host	SQLSpaces host
     * @param port	SQLSpaces port
     * @param space	SQLSPace
     * @param user	user throwing actions (NOT! the tool)
     */
    public ActionLogger(String host, int port, String space) {
        this.host = host;
        this.port = port;
        this.space = space;
        connect();
    }
    
    /**
     * creates a connection to the SQLSPaces server
     */
    private void connect() {
        try {
            ts = new TupleSpace(host, port, space);
            System.out.println("actionlogger verbunden");
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * logs an action
     * @param tool	the tool throwing the action
     * @param action IAction thrown
     */
    public void log(String username, String tool, IAction action) {
        Tuple message = new Tuple(username, tool, System.currentTimeMillis(), action.getXML());
        try {
            ts.write(message);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
    
}
