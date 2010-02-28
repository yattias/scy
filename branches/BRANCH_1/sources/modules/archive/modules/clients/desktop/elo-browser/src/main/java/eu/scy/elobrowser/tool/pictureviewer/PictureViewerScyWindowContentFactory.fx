package eu.scy.elobrowser.tool.pictureviewer;

import eu.scy.elobrowser.main.Roolo;
import eu.scy.elobrowser.tool.elofactory.ScyWindowContentFactory;
import eu.scy.scywindows.ScyWindow;
import java.net.URI;
import javafx.scene.Node;

/**
 * @author lars bollen
 */

public class PictureViewerScyWindowContentFactory extends ScyWindowContentFactory {
    public var roolo:Roolo;
    public def eloType1 = "scy/image";
    public def eloType2 = "scy/melo";

    public override function getSuitability(eloUri:URI):Integer{
        var type = roolo.extensionManager.getType(eloUri);
        if (type==eloType1 or type==eloType2) return 5;
        return 0;
    }

    public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
        var pictureViewerNode = PictureViewerNode.createPictureViewerNode(roolo, eloUri);
        pictureViewerNode.scyWindow = scyWindow;
        return pictureViewerNode;
    }

}
