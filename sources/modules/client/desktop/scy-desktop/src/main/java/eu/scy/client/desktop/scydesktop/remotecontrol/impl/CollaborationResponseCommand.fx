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
      def eloUriString: String = notification.getFirstProperty("proposed_elo");
      def user: String = notification.getFirstProperty("proposing_user");
      //TODO submit user-nickname instead of extracting it
      def pos = user.indexOf("@");
      def userNickname = if (pos>=0) user.substring(0, pos) else user;
      def eloUri = if (eloUriString=="") null else new URI(eloUriString);
      if (accepted == "true" and eloUri != null) {
         CollaborationMessageDialogBox {
            scyDesktop: scyDesktop
            eloUri: eloUri
            eloIconName: "collaboration_accepted"
            title: "Collaboration request"
            message: "Collaboration started on ELO"
            yesTitle: "Ok"
         }
         //            def messageDialogText = "{##"Starting collaboration on"} {eloUri}";
         //            DialogBox.showMessageDialog(messageDialogText, ##"Your Collaboration Request", scyDesktop, function(){}, "{eloUri}");
         def mucid: String = notification.getFirstProperty("mucid");
//         var uri = new URI(eloUri);
         var collaborationWindow: ScyWindow = scyDesktop.scyWindowControl.windowManager.findScyWindow(eloUri);
         if (collaborationWindow == null) {
            collaborationWindow = scyDesktop.scyWindowControl.addOtherScyWindow(eloUri);
            collaborationWindow.mucId = mucid;
         } else {
            collaborationWindow.mucId = mucid;
            scyDesktop.installCollaborationTools(collaborationWindow);
         }
         scyDesktop.scyWindowControl.makeMainScyWindow(eloUri);
      } else {
//         def closed = DialogBox.closeDialog("{eloUri}");
//         if (not closed) {
         def pendingCollaborationRequestDialog = CollaborationRequestCommand.pendingCollaborationRequestDialogs.get(eloUri) as CollaborationMessageDialogBox;
         if (pendingCollaborationRequestDialog==null){
            CollaborationMessageDialogBox {
               scyDesktop: scyDesktop
               eloUri: eloUri
               eloIconName: "collaboration_denied"
               title: "Collaboration request"
               message: "{userNickname} does not want to collaborate on ELO"
               yesTitle: "Cancel"
               yesFunction: function(): Void {
                  println("cancel");
               }
            }
            //                def messageDialogText = "{##"Your request for Collaboration on"} {eloUri} {##"was not accepted!"}";
            //                DialogBox.showMessageDialog(messageDialogText, ##"Your Collaboration Request", scyDesktop, function(){}, "{eloUri}");
            logger.debug("collaboration not accepted");
         }
         else{
            pendingCollaborationRequestDialog.close();
         }

      }
   }

}
