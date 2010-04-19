package eu.scy.elobrowser.tool.copex;

import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.tool.elofactory.ScyWindowContentFactory;
import eu.scy.scywindows.ScyWindow;
import java.net.URI;
import javafx.scene.Node;

/**
 * @author marjolaine bodin
 */

public class CopexScyWindowContentFactory extends ScyWindowContentFactory {
    public var roolo:Roolo;
    public def eloType1 = "scy/xproc";

    public override function getSuitability(eloUri:URI):Integer{
        var type = roolo.extensionManager.getType(eloUri);
        if (type==eloType1) return 5;
        return 0;
    }

    public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
        var copexNode = CopexNode.createCopexNode(roolo);
        copexNode.scyWindow = scyWindow;
        copexNode.loadElo(eloUri);
        return copexNode;
    }

}
