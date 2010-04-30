/*
 * CollaborationRequestAction.fx
 *
 * Created on 22.04.2010, 13:00:24
 */
package eu.scy.client.desktop.scydesktop.remotecontrol.impl;

import java.lang.String;
import eu.scy.notification.api.INotification;
import eu.scy.client.desktop.scydesktop.remotecontrol.api.ScyDesktopRemoteCommand;
import javax.swing.JOptionPane;

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
        def option = JOptionPane.showConfirmDialog(null, "{userNickname} wants to start a collaboration with you on the ELO {eloUri}. Accept?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            logger.debug(" => accepting collaboration");
            scyDesktop.config.getToolBrokerAPI().answerCollaborationProposal(true, user, eloUri);
        } else if (option == JOptionPane.NO_OPTION) {
            logger.debug(" => denying collaboration");
            scyDesktop.config.getToolBrokerAPI().answerCollaborationProposal(false, user, eloUri);
        }
    }

}
