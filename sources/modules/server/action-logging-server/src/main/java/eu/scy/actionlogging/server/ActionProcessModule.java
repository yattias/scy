package eu.scy.actionlogging.server;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.Context;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionProcessModule;


public class ActionProcessModule implements IActionProcessModule {
    
    private TupleSpace ts;
    
    public ActionProcessModule(String host, int port) {
        try {
            ts = new TupleSpace(new User("Action Logging Module"), host, port, "actions");
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }
    
    @Override
    public void create(IAction action) {
        try {
            writeAction(action);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
    }
    
    protected void writeAction(IAction action) throws TupleSpaceException {
        Field idField;
        Field timeField;
        Field typeField;
        Field userField;
        Field toolField;
        Field missionField;
        Field sessionField;
        Field datatypeField;
        Field dataField;
        
        Tuple actionTuple;

        if (ts != null) {
        	// action properties
            idField = new Field(((Action)action).getId());
            timeField = new Field(((Action)action).getTimeInMillis());
            typeField = new Field(((Action)action).getType());
            userField = new Field(((Action)action).getUser());
            datatypeField = ((Action)action).getDataType() == null ? new Field(String.class) : new Field(((Action)action).getDataType());
            dataField = ((Action)action).getData() == null ? new Field(String.class) : new Field(((Action)action).getData());
            // action context values
            toolField = ((Context)((Action)action).getContext()).getTool() == null ? new Field(String.class) : new Field(((Context)((Action)action).getContext()).getTool());
            missionField = ((Context)((Action)action).getContext()).getMission() == null ? new Field(String.class) : new Field(((Context)((Action)action).getContext()).getMission());
            sessionField = ((Context)((Action)action).getContext()).getSession() == null ? new Field(String.class) : new Field(((Context)((Action)action).getContext()).getSession());
            // we first create the tuple
            actionTuple = new Tuple(new Field("action"), idField, timeField, typeField, userField, toolField, missionField, sessionField, datatypeField, dataField);
            // now we add variable attributes
            for(String attribute : ((Action)action).getAttributes().keySet()) {
            	actionTuple.add(attribute + "=" + action.getAttribute(attribute));
            }
            try {
                ts.write(actionTuple);
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
        }
    }
}
