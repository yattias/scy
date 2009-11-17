package eu.scy.actionlogging;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;

public class SQLSpacesActionLogger implements IActionLogger {

    private TupleSpace ts;

    private Field idField;

    private Field xmlField;

    private Field userField;

    private Field typeField;

    private Field timeField;

    private Tuple actionTuple;

    private Field toolField;

    private Field missionField;

    private Field sessionField;

    public SQLSpacesActionLogger(String ip, int port, String space) {
        // creating / connecting to a space
        try {
            ts = new TupleSpace(ip, port, space);
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
        if (ts != null) {
            idField = new Field(action.getId());
            //timeField = new Field(TimeFormatHelper.getInstance().getISO8601AsLong(action.getTime()));
            timeField = new Field(action.getTimeInMillis());
            typeField = new Field(action.getType());
            userField = new Field(action.getUser());
            toolField = new Field(action.getContext(ContextConstants.tool));
            missionField = new Field(action.getContext(ContextConstants.mission));
            sessionField = new Field(action.getContext(ContextConstants.session));
            xmlField = new Field(new ActionXMLTransformer(action).getActionAsString());
            actionTuple = new Tuple(idField, timeField, typeField, userField, toolField, missionField, sessionField, xmlField);
            try {
                ts.write(actionTuple);
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void log(IAction action) {
        //The username and source aren't used any more, therefore -> null
        log(null, null, action);
    }
}
