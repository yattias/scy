/*
 * CollaborationResponseAction.fx
 *
 * Created on 22.04.2010, 13:00:24
 */
package eu.scy.client.desktop.scydesktop.remotecontrol.impl;

import java.lang.String;
import eu.scy.notification.api.INotification;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.remotecontrol.api.ScyDesktopRemoteCommand;

/**
 * @author sven
 */
public class EloShowCommand extends ScyDesktopRemoteCommand {

    override public function getActionName(): String {
        "elo_show"
    }

    override public function executeRemoteCommand(notification: INotification): Void {
        logger.debug("*****************elo_save*Notification*********************");
        def uri = new URI(notification.getFirstProperty("elo_uri"));
        var toolWindow: ScyWindow = scyDesktop.scyWindowControl.windowManager.findScyWindow(uri);
        logger.debug("elo_uri: {uri}");
        if (toolWindow == null) {
            logger.debug("creating new ELO/tool.");
            //the ELO is not opened yet
            toolWindow = scyDesktop.scyWindowControl.addOtherScyWindow(uri);
            //logger.debug("Placed externally saved ELO.");
            //fillNewScyWindow2(toolWindow);
        } else {
            //The ELO is already opened
            logger.debug("updating existing ELO/tool.");
            toolWindow.setScyContent(toolWindow);
        }
        scyDesktop.scyWindowControl.makeMainScyWindow(uri);
    }

}
