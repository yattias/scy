package eu.scy.colemo.client.actions;

import eu.scy.colemo.server.uml.UmlClass;
import eu.scy.colemo.client.ApplicationController;
import eu.scy.actionlogging.logger.ActionLogger;
import eu.scy.actionlogging.api.IActionLogger;

import java.awt.event.ActionEvent;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 19.nov.2008
 * Time: 06:40:21
 * To change this template use File | Settings | File Templates.
 */
public class DeleteConcept extends BaseAction{
    public Class getOperateson() {
        return UmlClass.class;
    }

    protected void performAction(ActionEvent e) {
        log.info("Deleting concept!");
        //IActionLogger actionLogger = ApplicationController.getDefaultInstance().getToolBrokerAPI().getActionLogger();
    }
}
