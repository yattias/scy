/*
 * ChatToolNode.fx
 *
 * Created on Nov 17, 2009, 8:38:36 PM
 */

package eu.scy.client.tools.fxchattool.registration;


import java.net.URI;
import javafx.scene.Group;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.CustomNode;

import eu.scy.client.tools.chattool.ChatPresencePanel;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Resizable;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.ContactFrame;
import java.util.Vector;
import eu.scy.notification.api.INotifiable;
import eu.scy.notification.api.INotification;
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;
import javafx.scene.layout.Container;

/**
 * @author jeremyt
 */

public class ChatPresenceToolNode extends CustomNode, Resizable, ScyToolFX, INotifiable {
    public override var width on replace {resizeContent()};
    public override var height on replace {resizeContent()};

    var tempUsers:Vector = new Vector();

    def spacing = 5.0;

    public var wrappedSPTPanel:Node;
    public var chatPresenceTool:ChatPresencePanel;
    public var toolBrokerAPI:ToolBrokerAPI;
    public var scyWindow:ScyWindow on replace {
    };

    public override function loadElo(uri:URI){
        // is never called
        println("WARNING: someone loaded a chat elo, not implemented yet");
    }

    public override function create(): Node {
      if(chatPresenceTool == null) {
        return Group {
            content: [
            Text {
                font: Font { size: 22 }
                x: 20, y: 90
                textAlignment: TextAlignment.CENTER
                content:"Chat can  ELO is need for Chat"
                fill: Color.BLACK
            }]
        };
     } else {
        wrappedSPTPanel = ScySwingWrapper.wrap(chatPresenceTool);
//        wrappedSPTPanel.foreground = Color.WHITE;
        return HBox {
         blocksMouse:false;
         content:
            wrappedSPTPanel;
      };
     }

   }

   function resizeContent(){
       Container.resizeNode(wrappedSPTPanel,width,height);
   }

   public override function getPrefHeight(height: Number) : Number{
      Container.getNodePrefHeight(wrappedSPTPanel, height);
    }

   public override function getPrefWidth(width: Number) : Number{
      Container.getNodePrefWidth(wrappedSPTPanel, width);
   }

   public override function canAcceptDrop(object:Object):Boolean{
      return false;
   }

   public override function acceptDrop(object:Object):Void{
      println("ChatPresenceToolNode: acceptDrop of {object.getClass()}");
      var c:ContactFrame = object as ContactFrame;
      println("ChatPresenceToolNode: acceptDrop user: {c.contact.name}");
      if(tempUsers.contains("{c.contact.awarenessUser.getJid()}/Smack") == false) {
        chatPresenceTool.addTemporaryUser(c.contact.name);
        //XXX the "/Smack" should be received correctly via method

        //commented out, as this should be done by the tool. cf S. Manske
        //toolBrokerAPI.proposeCollaborationWith("{c.contact.awarenessUser.getJid()}/Smack", scyWindow.eloUri.toString(),scyWindow.mucid);
        tempUsers.addElement("{c.contact.awarenessUser.getJid()}/Smack");
      }
      else {
          println("user: {c.contact.name} has already been contacted");
      }

   }

   public override function processNotification(notification: INotification): Boolean {
        def notificationType: String = notification.getFirstProperty("type");
        var success: Boolean = false;
        if (not (notificationType == null)) {
            if (notificationType == "collaboration_response") {
                def user: String = notification.getFirstProperty("proposing_user");
                //TODO submit user-nickname instead of extracting it
                def userNickname = user.substring(0, user.indexOf("@"));
                println("ChatPresenceToolNode: receivedCollaborationResponse no2 with user: {userNickname}");
                chatPresenceTool.removeTemporaryUser(userNickname);
                tempUsers.removeElement(userNickname);
                success = true;
            }
        }
        return success;
     }

}
