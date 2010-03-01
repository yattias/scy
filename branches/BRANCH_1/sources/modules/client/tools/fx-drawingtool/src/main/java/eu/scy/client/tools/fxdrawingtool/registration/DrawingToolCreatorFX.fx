/*
 * DrawingToolCreatorFX.fx
 *
 * Created on 13-jan-2010, 14:12:17
 */

package eu.scy.client.tools.fxdrawingtool.registration;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import javafx.scene.Node;
import colab.vt.whiteboard.component.WhiteboardPanel;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * @author sikken
 */

// place your code here

public class DrawingToolCreatorFX extends ScyToolCreatorFX{
    override public function createScyToolNode (eloType:String, creatorId: String, scyWindow:ScyWindow, windowContent: Boolean) : Node {
        DrawingNode{
           whiteboardPanel: new WhiteboardPanel();
           scyWindow:scyWindow
        }

    }
}
