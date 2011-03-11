/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.scydesktop.test;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.DialogBox;
import org.apache.log4j.Logger;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.remotecontrol.impl.CollaborationMessageDialogBox;

/**
 * @author SikkenJ
 */
public class TestDialogsNode extends CustomNode, ScyToolFX {

   def logger = Logger.getLogger(this.getClass());
   public var scyDesktop: ScyDesktop;
   public var toolBrokerAPI: ToolBrokerAPI on replace {
              userName = toolBrokerAPI.getLoginUserName();
           };
   var eloUri: URI;
   var userName = "?";
   def spacing = 5.0;

   public override function create(): Node {
      VBox {
         spacing: spacing
         content: [
            Label {
               text: "Test Dialog\nnot part of mission"
            }
            Button {
               text: "Old Request collaboration"
               disable: bind eloUri == null
               action: function() {
                  testOldCollaborationRequestCommand();
               }
            }
            Button {
               text: "Request collaboration"
               disable: bind eloUri == null
               action: function() {
                  testCollaborationRequestCommand();
               }
            }
            Button {
               text: "Collaboration accepted"
               disable: bind eloUri == null
               action: function() {
                  testCollaborationAccceptedCommand();
               }
            }
            Button {
               text: "Collaboration denied"
               disable: bind eloUri == null
               action: function() {
                  testCollaborationDeniedCommand();
               }
            }
            Button {
               text: "Start of collaboration"
               disable: bind eloUri == null
               action: function() {
                  testCollaborationRequestCommand();
               }
            }
         ]
      }
   }

   public override function newElo(): Void {
      eloUri = null;
   }

   public override function loadElo(eloUri: URI): Void {
      this.eloUri = eloUri;
   }

   public override function loadedEloChanged(eloUri: URI): Void {
      this.eloUri = eloUri;
   }

   function testOldCollaborationRequestCommand() {
      //        def user: String = toolBrokerAPI.getLoginUserName();
      //TODO submit user-nickname instead of extracting it
      def userNickname = userName;
      //        def eloUri: String = "eloUri";

      def yesAction: function() = function() {
                 logger.debug(" => accepting collaboration");
//            scyDesktop.config.getToolBrokerAPI().answerCollaborationProposal(true, user, eloUri);
              }
      def noAction: function() = function() {
                 logger.debug(" => denying collaboration");
//            scyDesktop.config.getToolBrokerAPI().answerCollaborationProposal(false, user, eloUri);
              }
      def text = "{userNickname} {##"wants to start a collaboration with you on the ELO"} {eloUri}. {##"Accept?"}";

      // todo
      // 1. check if user is collaboration ready state (not in mission map)
      // 2. check if user is in the correct LAS, if not ask if he wants to go there for the collaboration
      // if user is ready to collaborate, do it!
      DialogBox.showOptionDialog(text, ##"Collaboration Request", scyDesktop, yesAction, noAction, "{eloUri}");

   }

   function testCollaborationRequestCommand() {
      CollaborationMessageDialogBox {
         scyDesktop: scyDesktop
         eloUri: eloUri
         eloIconName: "collaboration_invitation"
         title: "Collaboration request"
         message: "{userName} invites you to collaborate on ELO"
         yesTitle: "Accept"
         yesFunction: function(): Void {
            println("accept");
         }
         noTitle: "Deny"
         noFunction: function(): Void {
            println("deny");
         }
      }
   }

   function testCollaborationAccceptedCommand() {
      CollaborationMessageDialogBox {
         scyDesktop: scyDesktop
         eloUri: eloUri
         eloIconName: "collaboration_accepted"
         title: "Collaboration request"
         message: "{userName} wants to collaborate on ELO"
         yesTitle: "Ok"
         yesFunction: function(): Void {
            println("ok");
         }
      }
   }

   function testCollaborationDeniedCommand() {
      CollaborationMessageDialogBox {
         scyDesktop: scyDesktop
         eloUri: eloUri
         eloIconName: "collaboration_denied"
         title: "Collaboration request"
         message: "{userName} does not want to collaborate on ELO"
         yesTitle: "Cancel"
         yesFunction: function(): Void {
            println("cancel");
         }
      }
   }

}
