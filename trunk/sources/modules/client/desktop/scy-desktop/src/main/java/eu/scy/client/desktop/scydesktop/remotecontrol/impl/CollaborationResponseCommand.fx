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
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.DialogBox;

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
        def eloUri: String = notification.getFirstProperty("proposed_elo");
        if (accepted == "true" and eloUri != null) {
            def messageDialogText = "{##"Starting collaboration on"} {eloUri}";
            DialogBox.showMessageDialog(messageDialogText, ##"Your Collaboration Request", scyDesktop, function(){});
            def mucid: String = notification.getFirstProperty("mucid");
            var uri = new URI(eloUri);
            var collaborationWindow: ScyWindow = scyDesktop.scyWindowControl.windowManager.findScyWindow(uri);
            if (collaborationWindow == null) {
                collaborationWindow = scyDesktop.scyWindowControl.addOtherScyWindow(uri);
                collaborationWindow.mucId = mucid;
            } else {
                collaborationWindow.mucId = mucid;
                scyDesktop.installCollaborationTools(collaborationWindow);
            }
            scyDesktop.scyWindowControl.makeMainScyWindow(uri);
        } else {
            def messageDialogText = "{##"Your request for Collaboration on"} {eloUri} {##"was not accepted!"}";
            DialogBox.showMessageDialog(messageDialogText, ##"Your Collaboration Request", scyDesktop, function(){});
            logger.debug("collaboration not accepted");
        }
    }

}
