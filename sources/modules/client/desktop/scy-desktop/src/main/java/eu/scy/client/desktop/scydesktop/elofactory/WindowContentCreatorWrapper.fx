/*
 * WindowContentCreatorWrapper.fx
 *
 * Created on 3-jul-2009, 14:49:59
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import javafx.ext.swing.SwingComponent;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;

/**
 * @author sikkenj
 */

public class WindowContentCreatorWrapper extends WindowContentCreatorFX {

   public var windowContentCreator: WindowContentCreator;

   public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
      var component = windowContentCreator.getScyWindowContent(eloUri);
      return SwingComponent.wrap(component);
   }
}
