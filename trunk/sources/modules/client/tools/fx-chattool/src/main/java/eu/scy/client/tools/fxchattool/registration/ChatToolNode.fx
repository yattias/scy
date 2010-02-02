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
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.scene.paint.Color;
/**
 * @author jeremyt
 */

public class ChatToolNode extends CustomNode {
    public-init var eloChatActionWrapper:EloChatActionWrapper;


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
        return Group {
         blocksMouse:false;
         content:
            wrappedSPTPanel;
      };
    
   }
}
