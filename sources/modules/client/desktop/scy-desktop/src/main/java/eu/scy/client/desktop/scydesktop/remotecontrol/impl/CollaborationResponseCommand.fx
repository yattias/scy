/*
 * CollaborationResponseAction.fx
 *
 * Created on 22.04.2010, 13:00:24
 */
package eu.scy.client.desktop.scydesktop.remotecontrol.impl;

import java.lang.String;
import eu.scy.notification.api.INotification;
import eu.scy.client.desktop.scydesktop.remotecontrol.api.ScyDesktopRemoteCommand;
import javax.swing.JOptionPane;
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
        def eloUri: String = notification.getFirstProperty("proposed_elo");
        if (accepted == "true" and eloUri != null) {
            JOptionPane.showMessageDialog(null, "Starting collaboration on {eloUri}", "Info", JOptionPane.INFORMATION_MESSAGE);
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
            JOptionPane.showMessageDialog(null, "Collaboration was not accepted!", "Info", JOptionPane.WARNING_MESSAGE);
            logger.debug("collaboration not accepted");
        }
    }

}
