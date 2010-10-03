/*
 * ChatToolNode.fx
 *
 * Created on Nov 17, 2009, 8:38:36 PM
 */

package eu.scy.client.tools.fxchattool.registration;


import java.net.URI;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.CustomNode;


import eu.scy.client.tools.chattool.ChatPanel;
import javafx.scene.layout.Resizable;
import javafx.scene.layout.VBox;
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;
import javafx.scene.layout.Container;
/**
 * @author jeremyt
 */

public class ChatToolNode extends CustomNode, Resizable {

    public override var width on replace {resizeContent()};
    public override var height on replace {resizeContent()};

    public-init var eloChatActionWrapper:EloChatActionWrapper;

    def spacing = 5.0;


    public var wrappedSPTPanel:Node;
    public var chatTool:ChatPanel;
    public var scyWindow:ScyWindow on replace {
    };

    public function loadElo(uri:URI){
        eloChatActionWrapper.loadElo(uri);
    }

   public override function create(): Node {
   wrappedSPTPanel = ScySwingWrapper.wrap(chatTool);
   return VBox {
         blocksMouse:false;
         content:
            wrappedSPTPanel;
      };
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
}
