/*
 * ScyToolViewerCreator.fx
 *
 * Created on 12-jan-2010, 15:35:15
 */

package eu.scy.client.desktop.scydesktop.tools.scytoolviewer;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * @author sikken
 */

// place your code here
public class ScyToolViewerCreator extends ScyToolCreatorFX{

    override public function createScyToolNode (eloType:String,creatorId:String, scyWindow:ScyWindow, windowContent:Boolean) : Node {
        ScyToolViewer{

        }
    }
}
