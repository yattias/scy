package eu.scy.colemo.client.actions;

import eu.scy.colemo.client.ApplicationController;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

import java.awt.event.ActionEvent;

import roolo.api.IRepository;
import roolo.elo.api.IELO;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 25.mar.2009
 * Time: 12:18:07
 * To change this template use File | Settings | File Templates.
 */
public class SaveConceptMapToXML extends BaseAction{
    public Class getOperateson() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    protected void performAction(ActionEvent e) {
        ToolBrokerAPI toolBroker = ApplicationController.getDefaultInstance().getToolBrokerAPI();
        IRepository repository = toolBroker.getRepository();
        //IELO elo  = toolBroker.getRepository().

    }
}
