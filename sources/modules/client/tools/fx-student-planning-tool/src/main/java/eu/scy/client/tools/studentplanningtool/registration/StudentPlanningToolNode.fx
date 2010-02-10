

/**
 * Student Planning Tool
 */

package eu.scy.client.tools.studentplanningtool.registration;

import java.net.URI;
import javafx.scene.Group;
import javafx.scene.Node;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.CustomNode;
import javafx.ext.swing.SwingComponent;
import eu.scy.tools.planning.*;



/**
 * @author aperritano
 */

 // place your code here
public class StudentPlanningToolNode extends CustomNode {

    public-init var eloChatActionWrapper:EloStudenPlanningActionWrapper;


    public var wrappedSPTPanel:SwingComponent;
    public var studentPlanningTool:StudentPlanningTool;
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

        scyWindow.title = "Student Planning Tool: {eloChatActionWrapper.getDocName()}";
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
        wrappedSPTPanel = SwingComponent.wrap(studentPlanningTool.createStudentPlanningPanel(null));
     return Group {
         blocksMouse:true;
         cache:false;
         content:

            wrappedSPTPanel;



      };
   }
}
