/*
 * CollaborationRequestAction.fx
 *
 * Created on 22.04.2010, 13:00:24
 */
package eu.scy.client.desktop.scydesktop.remotecontrol.impl;

import java.lang.String;
import eu.scy.notification.api.INotification;
import eu.scy.client.desktop.scydesktop.remotecontrol.api.ScyDesktopRemoteCommand;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.DialogBox;

/**
 * @author sven
 */
public class CollaborationRequestCommand extends ScyDesktopRemoteCommand {

    override public function getActionName(): String {
        "collaboration_request"
    }

    override public function executeRemoteCommand(notification: INotification): Void {
        logger.debug("********************collaboration_request*************************");
        def user: String = notification.getFirstProperty("proposing_user");
        //TODO submit user-nickname instead of extracting it
        def userNickname = user.substring(0, user.indexOf("@"));
        def eloUri: String = notification.getFirstProperty("proposed_elo");

        def yesAction:function() = function(){
            logger.debug(" => accepting collaboration");
            scyDesktop.config.getToolBrokerAPI().answerCollaborationProposal(true, user, eloUri);
        }
        def noAction: function() = function(){
            logger.debug(" => denying collaboration");
            scyDesktop.config.getToolBrokerAPI().answerCollaborationProposal(false, user, eloUri);
        }
        def text = "{userNickname} {##"wants to start a collaboration with you on the ELO"} {eloUri}. {##"Accept?"}";

        // todo
        // 1. check if user is collaboration ready state (not in mission map)
        // 2. check if user is in the correct LAS, if not ask if he wants to go there for the collaboration
        // if user is ready to collaborate, do it!

        DialogBox.showOptionDialog(text, ##"Collaboration Request", scyDesktop, yesAction, noAction, "{eloUri}");
    }

}
