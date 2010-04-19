package eu.scy.elobrowser.tool.textpad;

import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.tool.elofactory.ScyWindowContentFactory;
import eu.scy.scywindows.ScyWindow;
import java.net.URI;
import javafx.scene.Node;

/**
 * @author lars bollen
 */

public class TextpadScyWindowContentFactory extends ScyWindowContentFactory {
    public var roolo:Roolo;
    public def eloType = "scy/text";

    public override function getSuitability(eloUri:URI):Integer{
        var type = roolo.extensionManager.getType(eloUri);
        if (type==eloType) return 5;
        return 0;
    }

    public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
        var textpadNode = TextpadNode.createTextpadNode(roolo);
        textpadNode.scyWindow = scyWindow;
        textpadNode.loadElo(eloUri);
        return textpadNode;
    }

}

