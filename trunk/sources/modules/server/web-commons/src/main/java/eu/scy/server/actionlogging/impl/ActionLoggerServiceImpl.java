package eu.scy.server.actionlogging.impl;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.SQLSpacesActionLogger;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.core.model.transfer.ActionLogEntryAttribute;
import eu.scy.server.actionlogging.ActionLoggerService;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 04.aug.2011
 * Time: 05:57:22
 * To change this template use File | Settings | File Templates.
 */
public class ActionLoggerServiceImpl implements ActionLoggerService {

    private SQLSpacesActionLogger sqlSpacesActionLogger;


    public void logAction(String type, String userName, String tool, String eloURI) {
        IAction action = new Action();
        action.setType(type);
        action.setUser(userName);
        action.addContext(ContextConstants.tool, tool);
        action.addContext(ContextConstants.eloURI, getDecodedURI(eloURI));

        dispatchAction(action);
        
    }

    private String getDecodedURI(String uri) {
        try {
            return URLDecoder.decode(uri, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return uri;
    }


    @Override
    public void logAction(String type, String userName, String tool) {
        IAction action = new Action();
        action.setType(type);
        action.setUser(userName);
        action.addContext(ContextConstants.tool, tool);
/*        for (int i = 0; i < entry.getAttributes().size(); i++) {
            ActionLogEntryAttribute actionLogEntryAttribute = (ActionLogEntryAttribute) entry.getAttributes().get(i);
            action.addAttribute(actionLogEntryAttribute.getName(), actionLogEntryAttribute.getValue());
        }

        */

        dispatchAction(action);

    }

    public void dispatchAction(IAction action) {
        getSqlSpacesActionLogger().log(action);
    }

    public SQLSpacesActionLogger getSqlSpacesActionLogger() {
        return sqlSpacesActionLogger;
    }

    public void setSqlSpacesActionLogger(SQLSpacesActionLogger sqlSpacesActionLogger) {
        this.sqlSpacesActionLogger = sqlSpacesActionLogger;
    }
}
