/*
 * NewJavaFXClass.fx
 *
 * Created on 23-mrt-2009, 9:56:36
 */

package eu.scy.elobrowser.tool.elofactory;

import java.net.URI;
import javafx.scene.Node;

import eu.scy.scywindows.ScyWindow;

/**
 * @author sikkenj
 */

public abstract class ScyWindowContentFactory {
    public abstract function getSuitability(eloUri:URI):Integer;
    public abstract function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node;
}
