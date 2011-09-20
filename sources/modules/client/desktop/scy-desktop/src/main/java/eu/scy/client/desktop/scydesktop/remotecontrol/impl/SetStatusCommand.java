/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.remotecontrol.impl;

import eu.scy.awareness.AwarenessServiceException;
import eu.scy.awareness.IAwarenessService;
import eu.scy.client.desktop.scydesktop.remotecontrol.api.TBIRemoteCommand;
import eu.scy.notification.api.INotification;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author sven
 */
public class SetStatusCommand extends TBIRemoteCommand {

    public SetStatusCommand(ToolBrokerAPI toolBrokerAPI) {
        super(toolBrokerAPI);
    }

    @Override
    public String getActionName() {
        return "set_status";
    }

    @Override
    public void executeRemoteCommand(INotification notification) {
        String status = notification.getFirstProperty("status");
        final IAwarenessService awarenessService = getToolBrokerAPI().getAwarenessService();
        try {
            awarenessService.setStatus(status);
        } catch (AwarenessServiceException ex) {
            Logger.getLogger(SetStatusCommand.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
