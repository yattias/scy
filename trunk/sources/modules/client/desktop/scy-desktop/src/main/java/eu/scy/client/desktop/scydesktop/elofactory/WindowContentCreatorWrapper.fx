/*
 * WindowContentCreatorWrapper.fx
 *
 * Created on 3-jul-2009, 14:49:59
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;

import org.apache.log4j.Logger;


import javax.swing.JComponent;

/**
 * @author sikkenj
 */
var logger = Logger.getLogger("eu.scy.client.desktop.elofactory.WindowContentCreatorWrapper");

public class WindowContentCreatorWrapper extends WindowContentCreatorFX {

   public var windowContentCreator: WindowContentCreator;

   var component:JComponent;

   public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
      component = windowContentCreator.getScyWindowContent(eloUri);
      SwingContentWrapper{
         swingContent:component;
      }
   }

   public override function getScyWindowContentNew(scyWindow:ScyWindow):Node{
      component = windowContentCreator.getScyWindowContentNew();
      SwingContentWrapper{
         swingContent:component;
      }
   }


}
