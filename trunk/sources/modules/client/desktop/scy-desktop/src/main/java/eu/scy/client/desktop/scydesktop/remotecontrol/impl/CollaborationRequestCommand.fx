/*
 * CollaborationRequestAction.fx
 *
 * Created on 22.04.2010, 13:00:24
 */
package eu.scy.client.desktop.scydesktop.remotecontrol.impl;

import java.lang.String;
import eu.scy.notification.api.INotification;
import eu.scy.client.desktop.scydesktop.remotecontrol.api.ScyDesktopRemoteCommand;
import java.net.URI;
import java.util.HashMap;
import eu.scy.client.desktop.desktoputils.XFX;
import eu.scy.client.desktop.scydesktop.scywindows.window.ProgressOverlay;

/**
 * @author sven
 */

public def pendingCollaborationRequestDialogs = new HashMap();

public class CollaborationRequestCommand extends ScyDesktopRemoteCommand {

    override public function getActionName(): String {
        "collaboration_request"
    }

    override public function executeRemoteCommand(notification: INotification): Void {
        logger.debug("********************collaboration_request*************************");
        def user: String = notification.getFirstProperty("proposing_user");
        //TODO submit user-nickname instead of extracting it
        def userNickname = user.substring(0, user.indexOf("@"));
        def eloUriString: String = notification.getFirstProperty("proposed_elo");
        def eloUri = new URI(eloUriString);

        var collaborationMessageDialogBox: CollaborationMessageDialogBox;

        def yesAction:function() = function(){
            ProgressOverlay.startShowWorking();
            XFX.runActionInBackgroundAndCallBack(function() : Object {
                logger.debug(" => accepting collaboration");
                pendingCollaborationRequestDialogs.remove(eloUri);
                scyDesktop.config.getToolBrokerAPI().answerCollaborationProposal(true, user, eloUriString);
                return null;
            }, function(o : Object){
                ProgressOverlay.stopShowWorking();
            });
        }
        def noAction: function() = function(){
            ProgressOverlay.startShowWorking();
            XFX.runActionInBackgroundAndCallBack(function() : Object {
                logger.debug(" => denying collaboration");
                pendingCollaborationRequestDialogs.remove(eloUri);
                scyDesktop.config.getToolBrokerAPI().answerCollaborationProposal(false, user, eloUriString);
            }, function(o : Object){
                ProgressOverlay.stopShowWorking();
            });
        }
//        def text = "{userNickname} {##"wants to start a collaboration with you on the ELO"} {eloUri}. {##"Accept?"}";

        // todo
        // 1. check if user is collaboration ready state (not in mission map)
        // 2. check if user is in the correct LAS, if not ask if he wants to go there for the collaboration
        // if user is ready to collaborate, do it!

//        DialogBox.showOptionDialog(text, ##"Collaboration request", scyDesktop, yesAction, noAction, "{eloUri}");

      collaborationMessageDialogBox = CollaborationMessageDialogBox {
         scyDesktop: scyDesktop
         eloUri: eloUri
         eloIconName: "collaboration_invitation"
         title: ##"Collaboration request"
         message: "{userNickname} {##"invites you to collaborate on ELO"}"
         yesTitle: ##"Accept"
         yesFunction: yesAction
         noTitle: ##"Deny"
         noFunction: noAction
      }
      pendingCollaborationRequestDialogs.put(eloUri,collaborationMessageDialogBox);
    }

}
