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
import java.util.*;
import java.lang.*;


import eu.scy.client.tools.chattool.ChatPanel;
import javafx.scene.layout.Resizable;
import java.awt.Dimension;
import javafx.scene.layout.VBox;
/**
 * @author jeremyt
 */

public class ChatToolNode extends CustomNode, Resizable {

    public override var width on replace {resizeContent()};
    public override var height on replace {resizeContent()};

    public-init var eloChatActionWrapper:EloChatActionWrapper;

    def spacing = 5.0;


    public var wrappedSPTPanel:SwingComponent;
    public var chatTool:ChatPanel;
    public var scyWindow:ScyWindow on replace {
        //setScyWindowTitle();
    };

    public function loadElo(uri:URI){
        eloChatActionWrapper.loadElo(uri);
        //setScyWindowTitle();
    }

    function setScyWindowTitle(){
        if (scyWindow == null) {
            return;
        }

        scyWindow.title = "ChatTool: {eloChatActionWrapper.getDocName()}";
        var eloUri = eloChatActionWrapper.getEloUri();
        if (eloUri != null) {
            scyWindow.id = eloUri.toString();
        }
        else {
            var r = new Random(System.currentTimeMillis());
            var v = Long.toString(Math.abs(r.nextLong()), Math.random());


            scyWindow.id = v;
        }
    };

   public override function create(): Node {
   wrappedSPTPanel = SwingComponent.wrap(chatTool);
   return VBox {
         blocksMouse:false;
         content:
            wrappedSPTPanel;
      };
   }


   function resizeContent(){
//      println("wrappedTextEditor.boundsInParent: {wrappedTextEditor.boundsInParent}");
//      println("wrappedTextEditor.layoutY: {wrappedTextEditor.layoutY}");
//      println("wrappedTextEditor.translateY: {wrappedTextEditor.translateY}");
      var size = new Dimension(width,height-wrappedSPTPanel.boundsInParent.minY-spacing);
      // setPreferredSize is needed
      chatTool.setPreferredSize(size);
      chatTool.resizeChat(width, height-wrappedSPTPanel.boundsInParent.minY-spacing);
      // setSize is not visual needed
      // but set it, so the component react to it
      chatTool.setSize(size);
      //println("resized whiteboardPanel to ({width},{height})");
   }

   public override function getPrefHeight(width: Number) : Number{
      return chatTool.getPreferredSize().getHeight();
   }

   public override function getPrefWidth(width: Number) : Number{
      return chatTool.getPreferredSize().getWidth();
   }
}
