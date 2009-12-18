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

import eu.scy.client.tools.chattool.ChatPresencePanelMain;
/**
 * @author jeremyt
 */

public class ChatPresenceToolNode extends CustomNode {
    public-init var eloChatActionWrapper:EloChatActionWrapper;


    public var wrappedSPTPanel:SwingComponent;
    public var chatPresenceTool:ChatPresencePanelMain;
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
     //initTBI();
    // wrappedSPTPanel = studentPlanningTool.createStudentPlanningPanel();
        wrappedSPTPanel = SwingComponent.wrap(chatPresenceTool);
     return Group {
         blocksMouse:false;
         content:
            wrappedSPTPanel;
      };
   }
}
