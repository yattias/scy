/*
 * ChatToolNode.fx
 *
 * Created on Nov 17, 2009, 8:38:36 PM
 */

package eu.scy.client.tools.fxchattool.registration;


import javafx.scene.Node;
import javafx.scene.CustomNode;


import javafx.scene.layout.Resizable;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Container;
/**
 * @author jeremyt
 */

public class ChatToolNode extends CustomNode, Resizable {

    public override var width on replace {resizeContent()};
    public override var height on replace {resizeContent()};

    def spacing = 5.0;

    public var chatTool:Node;

   public override function create(): Node {
      return VBox {
         blocksMouse:false;
         content:
            chatTool;
      };
   }


   function resizeContent(){
       Container.resizeNode(chatTool,width,height);
   }

   public override function getPrefHeight(height: Number) : Number{
      Container.getNodePrefHeight(chatTool, height);
    }

   public override function getPrefWidth(width: Number) : Number{
      Container.getNodePrefWidth(chatTool, width);
   }
}
