/*
 * ImageViewerCreator.fx
 *
 * Created on 2-feb-2010, 20:08:30
 */

package eu.scy.client.desktop.scydesktop.tools.imageviewer;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import javafx.scene.Node;
import java.lang.String;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * @author sikken
 */

public class ImageViewerCreator extends ScyToolCreatorFX {

    override public function createScyToolNode (eloType : String, creatorId : String, window : ScyWindow, windowContent : Boolean) : Node {
        ImageViewer{

        }

    }


}
