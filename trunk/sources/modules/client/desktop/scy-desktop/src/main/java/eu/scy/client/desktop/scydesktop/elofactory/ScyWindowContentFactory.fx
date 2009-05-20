/*
 * NewJavaFXClass.fx
 *
 * Created on 23-mrt-2009, 9:56:36
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;
import javafx.scene.Node;

/**
 * @author sikkenj
 */

public abstract class ScyWindowContentFactory {
   public abstract function getSuitability(eloUri:URI):Integer;
   public abstract function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node;
}
