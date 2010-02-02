/*
 * PropertiesViewerCreator.fx
 *
 * Created on 16-jan-2010, 13:24:43
 */

package eu.scy.client.desktop.scydesktop.tools.propertiesviewer;

import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * @author sikken
 */

// place your code here

public class PropertiesViewerCreator extends ScyToolCreatorFX{
    override public function createScyToolNode (eloType:String,creatorId:String, scyWindow:ScyWindow, windowContent:Boolean) : Node {
        PropertiesViewer{
            scyWindow:scyWindow;
        }

    }
}
