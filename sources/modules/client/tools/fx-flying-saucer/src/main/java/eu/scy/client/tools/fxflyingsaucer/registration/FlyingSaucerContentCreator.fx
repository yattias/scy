/*
 * FlyingSaucerContentCreator.fx
 *
 * Created on 17-sep-2009, 11:26:46
 */

package eu.scy.client.tools.fxflyingsaucer.registration;



import javafx.scene.Node;
import java.net.URI;

import javafx.ext.swing.SwingComponent;
import eu.scy.client.tools.fxflyingsaucer.FlyingSaucerPanel;

import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;


/**
 * @author sikkenj
 */

// place your code here
public class FlyingSaucerContentCreator extends WindowContentCreatorFX {

   public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
      return SwingComponent.wrap(new FlyingSaucerPanel());
   }


   public override function getScyWindowContentNew(scyWindow:ScyWindow):Node{
      return SwingComponent.wrap(new FlyingSaucerPanel());
   }

   function setWindowProperties(scyWindow:ScyWindow){
      scyWindow.minimumWidth = 320;
      scyWindow.minimumHeight = 100;
   }


}
