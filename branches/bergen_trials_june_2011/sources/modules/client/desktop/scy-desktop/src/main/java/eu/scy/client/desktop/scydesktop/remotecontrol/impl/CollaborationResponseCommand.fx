/*
 * CollaborationResponseAction.fx
 *
 * Created on 22.04.2010, 13:00:24
 */
package eu.scy.client.desktop.scydesktop.remotecontrol.impl;

import java.lang.String;
import eu.scy.notification.api.INotification;
import eu.scy.client.desktop.scydesktop.remotecontrol.api.ScyDesktopRemoteCommand;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * @author sven
 */
public class CollaborationResponseCommand extends ScyDesktopRemoteCommand {

    override public function getActionName(): String {
        "collaboration_response"
    }

    override public function executeRemoteCommand(notification: INotification): Void {
        logger.debug("********************collaboration_response*************************");
        def accepted: String = notification.getFirstProperty("accepted");
        def eloUriString: String = notification.getFirstProperty("proposed_elo");
        def proposingUser: String = notification.getFirstProperty("proposing_user");
        def proposedUser: String = notification.getFirstProperty("proposed_user");
        //TODO submit user-nickname instead of extracting it
        def pos1 = proposingUser.indexOf("@");
        def proposingUserNickname = if (pos1 >= 0) proposingUser.substring(0, pos1) else proposingUser;
        def pos2 = proposedUser.indexOf("@");
        def proposedUserNickname = if (pos2 >= 0) proposedUser.substring(0, pos2) else proposedUser;
        def eloUri = if (eloUriString == "") null else new URI(eloUriString);
        if (accepted == "true" and eloUri != null) {
            CollaborationMessageDialogBox {
                scyDesktop: scyDesktop
                eloUri: eloUri
                eloIconName: "collaboration_accepted"
                title: "Collaboration request"
                message: "Collaboration started on ELO"
                yesTitle: "Ok"
            }
            def mucid: String = notification.getFirstProperty("mucid");
            var collaborationWindow: ScyWindow = scyDesktop.scyWindowControl.windowManager.findScyWindow(eloUri);
            if (collaborationWindow == null) {
                collaborationWindow = scyDesktop.scyWindowControl.addOtherCollaborativeScyWindow(eloUri, mucid);
            } else if (not collaborationWindow.isCollaborative) {
                scyDesktop.installCollaborationTools(collaborationWindow, mucid);
                scyDesktop.scyWindowControl.makeMainScyWindow(eloUri);
            }
            collaborationWindow.ownershipManager.addOwner(proposedUserNickname, true);
        } else {
            def pendingCollaborationRequestDialog = CollaborationRequestCommand.pendingCollaborationRequestDialogs.get(eloUri) as CollaborationMessageDialogBox;
            if (pendingCollaborationRequestDialog == null) {
                CollaborationMessageDialogBox {
                    scyDesktop: scyDesktop
                    eloUri: eloUri
                    eloIconName: "collaboration_denied"
                    title: "Collaboration request"
                    message: "{proposedUserNickname} does not want to collaborate on ELO"
                    yesTitle: "Ok"
                }
                logger.debug("collaboration not accepted");
                var collaborationWindow: ScyWindow = scyDesktop.scyWindowControl.windowManager.findScyWindow(eloUri);
                if (collaborationWindow != null) {
                    collaborationWindow.ownershipManager.removeOwner(proposedUserNickname, false);
                }
            } else {
                pendingCollaborationRequestDialog.close();
            }

        }
    }

}
