package eu.scy.client.tools.studentplanningtool.registration;

import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorFX;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;
import eu.scy.client.tools.studentplanningtool.registration.StudentPlanningToolNode;
import eu.scy.tools.planning.StudentPlanningTool;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;

/**
 * @author aperritano
 */

public class StudentPlanningToolContentCreator extends ScyToolCreatorFX {

       public var toolBrokerAPI:ToolBrokerAPI;
       public var metadataTypeManager: IMetadataTypeManager;
       public var repository:IRepository;
       public var eloFactory:IELOFactory;

      override public function createScyToolNode (eloType:String, creatorId: String, scyWindow:ScyWindow, windowContent: Boolean) : Node {
        StudentPlanningToolNode{
            scyWindow:scyWindow;
            toolBrokerAPI:toolBrokerAPI;
            metadataTypeManager:metadataTypeManager;
            repository:repository;
            eloFactory:eloFactory;
         }

    }
}