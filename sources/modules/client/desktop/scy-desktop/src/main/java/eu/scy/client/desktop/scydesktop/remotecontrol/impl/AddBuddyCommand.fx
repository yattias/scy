/*
 * CollaborationRequestAction.fx
 *
 * Created on 22.04.2010, 13:00:24
 */
package eu.scy.client.desktop.scydesktop.remotecontrol.impl;

import java.lang.String;
import eu.scy.notification.api.INotification;
import eu.scy.client.desktop.scydesktop.remotecontrol.api.ScyDesktopRemoteCommand;
import eu.scy.awareness.IAwarenessService;
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author sven
 */
public class AddBuddyCommand extends ScyDesktopRemoteCommand {

    override public function getActionName(): String {
        "add_buddy"
    }

    override public function executeRemoteCommand(notification: INotification): Void {
        logger.debug("********************add_buddy_command*************************");
        def user: String = notification.getFirstProperty("user");
        def bareUsername:String = StringUtils.parseBareAddress(user);
        def awarenessService:IAwarenessService = bind scyDesktop.config.getToolBrokerAPI().getAwarenessService();

        awarenessService.addBuddy(bareUsername);
        logger.info("added buddy with name {bareUsername} to your buddylist");
    }

}
