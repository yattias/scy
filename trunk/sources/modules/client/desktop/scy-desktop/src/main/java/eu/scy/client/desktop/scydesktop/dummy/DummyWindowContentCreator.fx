/*
 * DummyWindowContentCreator.fx
 *
 * Created on 30-jun-2009, 15:51:26
 */

package eu.scy.client.desktop.scydesktop.dummy;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;

import eu.scy.client.desktop.scydesktop.elofactory.DummyScyWindowContent;
import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorFX;

/**
 * @author sikkenj
 */

public class DummyWindowContentCreator extends WindowContentCreatorFX {

   public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
      return DummyScyWindowContent{
         label: "FX: {eloUri.toString()}";
      }
   }

   public override function getScyWindowContentNew(scyWindow:ScyWindow):Node{
      return DummyScyWindowContent{
         label: "FX: New {scyWindow.eloType}";
      }
   }


}
