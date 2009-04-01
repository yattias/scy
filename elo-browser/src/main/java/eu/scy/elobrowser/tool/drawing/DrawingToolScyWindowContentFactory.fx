/*
 * DrawingToolScyWindowContentFactory.fx
 *
 * Created on 31-mrt-2009, 17:49:49
 */

package eu.scy.elobrowser.tool.drawing;

import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.tool.drawing.DrawingNode;
import eu.scy.elobrowser.tool.elofactory.ScyWindowContentFactory;
import eu.scy.scywindows.ScyWindow;
import java.net.URI;
import javafx.scene.Node;

/**
 * @author sikkenj
 */

public class DrawingToolScyWindowContentFactory  extends ScyWindowContentFactory {
    public var roolo:Roolo;
    public def drawingType = "scy/drawing";

    public override function getSuitability(eloUri:URI):Integer{
        var type = roolo.extensionManager.getType(eloUri);
        if (type==drawingType) return 5;
        return 0;
    }

    public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
        var drawingNode = DrawingNode.createDrawingNode(roolo);
        drawingNode.scyWindow = scyWindow;
        drawingNode.loadElo(eloUri);
        return drawingNode;
    }

}
