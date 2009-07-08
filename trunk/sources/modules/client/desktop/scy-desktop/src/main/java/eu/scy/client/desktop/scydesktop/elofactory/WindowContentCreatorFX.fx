/*
 * WindowContentCreator.fx
 *
 * Created on 30-jun-2009, 15:49:47
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;

/**
 * @author sikkenj
 */

public mixin class WindowContentCreatorFX {

   public abstract function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node;

   public abstract function getScyWindowContentNew(scyWindow:ScyWindow):Node;
}
