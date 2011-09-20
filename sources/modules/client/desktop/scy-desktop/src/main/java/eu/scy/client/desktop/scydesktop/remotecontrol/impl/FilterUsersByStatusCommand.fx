/*
 * CollaborationRequestAction.fx
 *
 * Created on 22.04.2010, 13:00:24
 */
package eu.scy.client.desktop.scydesktop.remotecontrol.impl;

import java.lang.String;
import eu.scy.notification.api.INotification;
import eu.scy.client.desktop.scydesktop.remotecontrol.api.ScyDesktopRemoteCommand;

/**
 * @author sven
 */
public class FilterUsersByStatusCommand extends ScyDesktopRemoteCommand {

    override public function getActionName(): String {
        "filter_users"
    }

    override public function executeRemoteCommand(notification: INotification): Void {
        logger.debug("********************filter users command*************************");
        def filterUsersAsString: String = notification.getFirstProperty("filter");
        def filterUsers = Boolean.parseBoolean(filterUsersAsString);
        def groupId: String = notification.getFirstProperty("group-id");

        scyDesktop.contactlist.filter = filterUsers;
        scyDesktop.contactlist.filterId = groupId;

        logger.info("user filtering: {filterUsers}, group ID: {groupId}");
    }

}
