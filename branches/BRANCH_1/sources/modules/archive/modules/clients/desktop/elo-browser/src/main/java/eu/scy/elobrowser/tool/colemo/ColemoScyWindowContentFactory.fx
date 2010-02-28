package eu.scy.elobrowser.tool.colemo;

import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.tool.elofactory.ScyWindowContentFactory;
import eu.scy.scywindows.ScyWindow;
import java.net.URI;
import javafx.scene.Node;

/**
 * @author lars bollen
 */

public class ColemoScyWindowContentFactory extends ScyWindowContentFactory {
    public var roolo:Roolo;
    public def eloType = "scy/scymapping";
    public def eloTypeReview = "scy";

    public override function getSuitability(eloUri:URI):Integer{
        var type = roolo.extensionManager.getType(eloUri);
        if (type==eloType or type==eloTypeReview) return 5;
        return 0;
    }

    public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
        var colemoNode = ColemoNode.createColemoNode(roolo);
        colemoNode.scyWindow = scyWindow;
        //colemoNode.loadElo(eloUri);
        return colemoNode;
    }

}
