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
import eu.scy.awareness.IAwarenessUser;

/**
 * @author sven
 */
public class RemoveAllBuddiesCommand extends ScyDesktopRemoteCommand {

    override public function getActionName(): String {
        "remove_all_buddies"
    }

    override public function executeRemoteCommand(notification: INotification): Void {
        logger.debug("********************remove_all_buddies_command*************************");
        def awarenessService:IAwarenessService = bind scyDesktop.config.getToolBrokerAPI().getAwarenessService();
        def buddies = awarenessService.getBuddies();
        for(buddy in buddies){
            def buddyname = (buddy as IAwarenessUser).getJid();
            awarenessService.removeBuddy(buddyname);
            logger.info("removed buddy with name {buddyname} from buddylist");
        }
    }

}
