
package eu.scy.client.tools.studentplanningtool.registration;

import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorFX;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;
import eu.scy.client.tools.studentplanningtool.registration.StudentPlanningToolNode;
import eu.scy.tools.planning.StudentPlanningTool;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 * @author aperritano
 */

public class StudentPlanningToolContentCreator extends WindowContentCreatorFX {
    override public function getScyWindowContentNew (scyWindow : ScyWindow) : Node {
         return createStudentPlanningToolNode(scyWindow);
    }

    public var node:Node;
    public var toolBrokerAPI:ToolBrokerAPI;
   //public var metadataTypeManager: IMetadataTypeManager;
   // public var repository:IRepository;

    //called if an existing ELO
    public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node {
        var planningNode:StudentPlanningToolNode = createStudentPlanningToolNode(scyWindow);
        return node;
    }



    function createStudentPlanningToolNode(scyWindow:ScyWindow):StudentPlanningToolNode{
        setWindowProperties(scyWindow);

        var studentPlanningTool = new StudentPlanningTool(null);

        return StudentPlanningToolNode{
            toolBrokerAPI:toolBrokerAPI;
         }
   }

    function setWindowProperties(scyWindow:ScyWindow){
            /*
        scyWindow.minimumWidth = 500;
        scyWindow.minimumHeight = 600;
        scyWindow.maximumWidth = 500;
        scyWindow.maximumHeight = 600;
        scyWindow.allowResize = false;
        */

    }


}
