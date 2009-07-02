/*
 * SwingContentCreator.fx
 *
 * Created on 2-jul-2009, 14:18:02
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.lang.Class;
import java.net.URI;
import javax.swing.JComponent;

import javafx.ext.swing.SwingComponent;

/**
 * @author sikkenj
 */

public class SwingContentCreator extends WindowContentCreator {
   public var swingClassName:String;

   public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
      var component = createSwingComponent();
      return SwingComponent.wrap(component);
   };

   function createSwingComponent(): JComponent{
      var clas = Class.forName(swingClassName);
      clas.newInstance() as JComponent;
   }


}
