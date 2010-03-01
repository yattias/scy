/*
 * ChattoolContentCreator.fx
 *
 * Created on 8-jul-2009, 15:41:12
 */

package eu.scy.client.tools.studentplanningtool.registration;

import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorFX;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;


/**
 * @author jeremyt
 */

public class StudentPlanningContentCreator extends WindowContentCreatorFX {
    public var node:Node;
   //public var metadataTypeManager: IMetadataTypeManager;
   // public var repository:IRepository;

    public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node {
        return node;
    }



    public override function getScyWindowContentNew(scyWindow:ScyWindow):Node{
        return node;
    }

    function setWindowProperties(scyWindow:ScyWindow){
        scyWindow.minimumWidth = 320;
        scyWindow.minimumHeight = 100;
    }


}
