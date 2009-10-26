

/*
 * ChatNode.fx
 *
 * Created on 18-dec-2008, 15:19:52
 */

package eu.scy.client.tools.studentplanningtool.registration;

import java.net.URI;
import javafx.scene.Group;
import javafx.scene.Node;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

import javafx.scene.CustomNode;



/**
 * @author sikkenj
 */

 // place your code here
public class StudentPlanningToolNode extends CustomNode {

    public-init var eloChatActionWrapper:EloStudenPlanningActionWrapper;

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

        scyWindow.title = "StudenPlanning: {eloChatActionWrapper.getDocName()}";
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
     return Group {
         blocksMouse:true;
         content: [
            Group {
                   content: [
                        
                     
                   ]
                }
         ]
      };
   }
}
