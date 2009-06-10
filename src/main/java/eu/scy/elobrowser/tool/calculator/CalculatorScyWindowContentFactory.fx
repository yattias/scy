package eu.scy.elobrowser.tool.calculator;

import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.tool.elofactory.ScyWindowContentFactory;
import eu.scy.scywindows.ScyWindow;
import java.net.URI;
import javafx.scene.Node;

/**
 * @author lars bollen
 */

public class CalculatorScyWindowContentFactory extends ScyWindowContentFactory {
    public var roolo:Roolo;
    public def eloType = "scy/text";

    public override function getSuitability(eloUri:URI):Integer{
        var type = roolo.extensionManager.getType(eloUri);
        if (type==eloType) return 5;
        return 0;
    }

    public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
        var calculatorNode = CalculatorNode.createCalculatorNode(roolo);
        calculatorNode.scyWindow = scyWindow;
        calculatorNode.loadElo(eloUri);
        return calculatorNode;
    }

}

