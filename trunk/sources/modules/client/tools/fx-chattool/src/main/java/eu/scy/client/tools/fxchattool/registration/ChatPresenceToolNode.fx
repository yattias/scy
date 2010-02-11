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
import javafx.ext.swing.SwingComponent;

import eu.scy.client.tools.chattool.ChatPresencePanel;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.text.Font;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Resizable;
import java.awt.Dimension;

import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.*;
import eu.scy.client.desktop.scydesktop.tools.corner.contactlist.ContactFrame;
/**
 * @author jeremyt
 */

public class ChatPresenceToolNode extends CustomNode, Resizable, ScyToolFX {
    public override var width on replace {resizeContent()};
    public override var height on replace {resizeContent()};

    public-init var eloChatActionWrapper:EloChatActionWrapper;

    def spacing = 5.0;

    public var wrappedSPTPanel:SwingComponent;
    public var chatPresenceTool:ChatPresencePanel;
    public var scyWindow:ScyWindow on replace {
        setScyWindowTitle();
    };

    public function loadElo(uri:URI){
        eloChatActionWrapper.loadElo(uri);
        setScyWindowTitle();
    }

    function setScyWindowTitle(){
        if (scyWindow == null) {
            return;
        }

        //scyWindow.title = "Chat: {eloChatActionWrapper.getDocName()}";
        var eloUri = eloChatActionWrapper.getEloUri();
        if (eloUri != null) {
            scyWindow.id = eloUri.toString();
        }
        else {
            scyWindow.id = "";
        }
    };

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
        wrappedSPTPanel = SwingComponent.wrap(chatPresenceTool);
        wrappedSPTPanel.foreground = Color.WHITE;
        return HBox {
         blocksMouse:false;
         content:
            wrappedSPTPanel;
      };
     }

   }

   function resizeContent(){
//      println("wrappedTextEditor.boundsInParent: {wrappedTextEditor.boundsInParent}");
//      println("wrappedTextEditor.layoutY: {wrappedTextEditor.layoutY}");
//      println("wrappedTextEditor.translateY: {wrappedTextEditor.translateY}");
      var size = new Dimension(width,height-wrappedSPTPanel.boundsInParent.minY-spacing);
      // setPreferredSize is needed
      chatPresenceTool.setPreferredSize(size);
      chatPresenceTool.resizeChat(width, height-wrappedSPTPanel.boundsInParent.minY-spacing);
      // setSize is not visual needed
      // but set it, so the component react to it
      chatPresenceTool.setSize(size);
      //println("resized whiteboardPanel to ({width},{height})");
   }

   public override function getPrefHeight(width: Number) : Number{
      return chatPresenceTool.getPreferredSize().getHeight();
   }

   public override function getPrefWidth(width: Number) : Number{
      return chatPresenceTool.getPreferredSize().getWidth();
   }

   public override function canAcceptDrop(object:Object):Boolean{
      println("################################canAcceptDrop of {object.getClass()}");
      return true;
   }

   public override function acceptDrop(object:Object):Void{
      println("################################acceptDrop of {object.getClass()}");
      var c:ContactFrame = object as ContactFrame;
      println("################################acceptDrop of {c.contact.name}");
      chatPresenceTool.addTemporaryUser(c.contact.name);
   }
}
