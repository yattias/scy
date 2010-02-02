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
/**
 * @author jeremyt
 */

public class ChatPresenceToolNode extends CustomNode {
    public-init var eloChatActionWrapper:EloChatActionWrapper;


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
        return Group {
         blocksMouse:false;
         content:
            wrappedSPTPanel;
      };
     }

   }
}
