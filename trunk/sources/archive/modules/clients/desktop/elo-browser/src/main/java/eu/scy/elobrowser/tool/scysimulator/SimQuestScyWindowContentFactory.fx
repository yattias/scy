package eu.scy.elobrowser.tool.scysimulator;

import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.tool.elofactory.ScyWindowContentFactory;
import eu.scy.scywindows.ScyWindow;
import java.net.URI;
import javafx.scene.Node;

/**
 * @author lars bollen
 */

public class SimQuestScyWindowContentFactory extends ScyWindowContentFactory {
    public var roolo:Roolo;
    public def eloType = "scy/simconfig";

    public override function getSuitability(eloUri:URI):Integer{
        var type = roolo.extensionManager.getType(eloUri);
        if (type==eloType) return 5;
        return 0;
    }

    public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
        var simQuestNode = SimQuestNode.createSimQuestNode(roolo);
		  scyWindow.widthHeightProportion = 1.5;
        simQuestNode.scyWindow = scyWindow;
        simQuestNode.loadElo(eloUri);
        return simQuestNode;
    }

}

