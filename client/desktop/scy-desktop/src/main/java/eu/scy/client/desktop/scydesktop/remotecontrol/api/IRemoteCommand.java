/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.remotecontrol.api;

import eu.scy.notification.api.INotification;

/**
 * Basic Interface for Remote Commands
 * @author sven
 */
public interface IRemoteCommand {

    /**
     * The Method is used for the mapping ActionName -> Action of the registry,
     * where the ActionName should be unique
     * @return the (hopefully unique) name of the action
     */
    public String getActionName();

    /**
     * This method is called, when the registry recognizes the actionName belonging to this action
     * @param notification
     */
    public void executeRemoteCommand(INotification notification);

}
