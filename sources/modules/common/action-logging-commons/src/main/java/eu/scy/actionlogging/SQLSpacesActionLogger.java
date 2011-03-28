package eu.scy.actionlogging;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.util.ArrayList;

import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;

public class SQLSpacesActionLogger implements IActionLogger {

    private TupleSpace ts;

    private ArrayList<IAction> queue;

    private String space;

    private int port;

    private String ip;

    public SQLSpacesActionLogger(String ip, int port, String space) {
        this(ip, port, space, "ActionLogger");
    }

    public SQLSpacesActionLogger(String ip, int port, String space, String user) {
        this.ip = ip;
        this.port = port;
        this.space = space;
        queue = new ArrayList<IAction>();
        // creating / connecting to a space
        try {
            ts = new TupleSpace(new User(user), ip, port, space);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            ts.disconnect();
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void log(String username, String source, IAction action) {
        boolean success = writeToTs(action);
        if (!success) {
            queue.add(action);
        }
    }

    private boolean writeToTs(IAction action) {
        Tuple actionTuple = ActionTupleTransformer.getActionAsTuple(action);
        try {
            getTS().write(actionTuple);
            return true;
        } catch (Exception e) {
            ts = null;
            e.printStackTrace();
        }
        return false;
    }

    private TupleSpace getTS() {
        if (ts == null) {
            try {
                ts = new TupleSpace(ip, port, space);

                @SuppressWarnings("unchecked")
                ArrayList<IAction> queueCopy = (ArrayList<IAction>) (queue.clone());
                for (IAction a : queueCopy) {
                    boolean success = writeToTs(a);
                    if (success) {
                        queue.remove(a);
                    }
                }
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
        }
        return ts;
    }

    @Override
    public void log(IAction action) {
        // The username and source aren't used any more, therefore -> null
        log(null, null, action);
    }

}
