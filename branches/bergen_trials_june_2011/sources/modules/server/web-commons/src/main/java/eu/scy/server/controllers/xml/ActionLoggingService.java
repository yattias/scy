package eu.scy.server.controllers.xml;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.SQLSpacesActionLogger;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.core.model.transfer.ActionLogEntry;
import eu.scy.core.model.transfer.ActionLogEntryAttribute;
import eu.scy.core.model.transfer.ServiceMessage;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 02.feb.2011
 * Time: 05:50:53
 * To change this template use File | Settings | File Templates.
 */
public class ActionLoggingService extends XMLStreamerController {

    private SQLSpacesActionLogger sqlSpacesActionLogger;

    @Override
    protected Object getObjectToStream(HttpServletRequest request, HttpServletResponse httpServletResponse) {

        String actionLogXML = request.getParameter("logaction");
        if (actionLogXML != null) {
            logger.info("LOGGED: " + actionLogXML);
            ActionLogEntry entry = (ActionLogEntry) getXmlTransferObjectService().getObject(actionLogXML);
            logger.info(entry.toString());

            IAction action = new Action();
            action.setType(entry.getType());
            action.setUser(getCurrentUserName(request));
            action.addContext(ContextConstants.tool, entry.getTool());
            for (int i = 0; i < entry.getAttributes().size(); i++) {
                ActionLogEntryAttribute actionLogEntryAttribute = (ActionLogEntryAttribute) entry.getAttributes().get(i);
                action.addAttribute(actionLogEntryAttribute.getName(), actionLogEntryAttribute.getValue());
            }

            logAction(action);
        }

        ServiceMessage serviceMessage = new ServiceMessage();
        serviceMessage.setMessage("ActionLoggedSuccessfully");

        return serviceMessage;


    }

    public void logAction(IAction action) {
        getSqlSpacesActionLogger().log(action);
        logger.info("LOGGED: " + action);
    }

    public SQLSpacesActionLogger getSqlSpacesActionLogger() {
        return sqlSpacesActionLogger;
    }

    public void setSqlSpacesActionLogger(SQLSpacesActionLogger sqlSpacesActionLogger) {
        this.sqlSpacesActionLogger = sqlSpacesActionLogger;
    }
}
