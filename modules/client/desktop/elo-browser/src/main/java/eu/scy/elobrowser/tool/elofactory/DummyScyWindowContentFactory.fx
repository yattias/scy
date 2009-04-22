/*
 * DummyScyWindowContentFactory.fx
 *
 * Created on 23-mrt-2009, 10:44:23
 */

package eu.scy.elobrowser.tool.elofactory;

import eu.scy.elobrowser.tool.elofactory.DummyScyWindowContent;
import eu.scy.elobrowser.tool.elofactory.ScyWindowContentFactory;
import eu.scy.scywindows.ScyWindow;
import java.net.URI;
import javafx.scene.Node;

/**
 * @author sikkenj
 */

public class DummyScyWindowContentFactory extends ScyWindowContentFactory {
    public override function getSuitability(eloUri:URI):Integer{
        return 1;
    }

    public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
        return DummyScyWindowContent{
            label:eloUri.toString();
        }

    }


}
