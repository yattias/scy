/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.remotecontrol.api;

import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 *
 * @author sven
 */
public abstract class TBIRemoteCommand implements IRemoteCommand{

    private ToolBrokerAPI toolBrokerAPI;

    public TBIRemoteCommand(ToolBrokerAPI toolBrokerAPI) {
        this.toolBrokerAPI = toolBrokerAPI;
    }

    public ToolBrokerAPI getToolBrokerAPI() {
        return toolBrokerAPI;
    }

    public void setToolBrokerAPI(ToolBrokerAPI toolBrokerAPI) {
        this.toolBrokerAPI = toolBrokerAPI;
    }

}
