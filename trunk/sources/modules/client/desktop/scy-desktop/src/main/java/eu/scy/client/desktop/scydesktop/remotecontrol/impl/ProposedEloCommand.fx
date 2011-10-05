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
import org.jivesoftware.smack.util.StringUtils;

/**
 * @author sven
 */
public class ProposedEloCommand extends ScyDesktopRemoteCommand {

    override public function getActionName(): String {
        "proposed_elo"
    }

    override public function executeRemoteCommand(notification: INotification): Void {
        logger.debug("*****************proposed_elo*Notification*********************");
        def uri = new URI(notification.getFirstProperty("proposed_elo"));
        def user = notification.getFirstProperty("proposing_user");
        def userNickname = StringUtils.parseName(user);
        var toolWindow: ScyWindow = scyDesktop.scyWindowControl.windowManager.findScyWindow(uri);
        logger.debug("elo_uri: {uri}");
        logger.debug("user: {user}");
        logger.debug("user name: {userNickname}");

        //Dialog! Show ELO?
        CollaborationMessageDialogBox {
            scyDesktop: scyDesktop
            eloUri: uri
            eloIconName: "collaboration_invitation"
            title: ##"Shared ELO"
            message: "{userNickname} {##"wants to share an ELO with you. Do you want to open it?"}"
            yesTitle: ##"Accept"
            yesFunction: function(): Void {
                if (toolWindow == null) {
                    logger.debug("creating new ELO/tool.");
                    //the ELO is not opened yet
                    toolWindow = scyDesktop.scyWindowControl.addOtherScyWindow(uri);
                } else {
                    //The ELO is already opened
                    logger.debug("updating existing ELO/tool.");
                    toolWindow.setScyContent(toolWindow);
                }
                toolWindow.openDrawer("right");
                scyDesktop.scyWindowControl.makeMainScyWindow(uri);
            }
            noTitle: ##"Deny"
            noFunction: function():Void{
                
            }

        }

    }

}
