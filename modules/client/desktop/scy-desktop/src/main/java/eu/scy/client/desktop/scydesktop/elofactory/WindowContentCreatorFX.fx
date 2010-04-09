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
 * Interface for creating a Node to display/edit an ELO.
 *
 * @author sikkenj
 */

public mixin class WindowContentCreatorFX {

   /**
    * Create a new Node to display/edit an existing ELO
    *
    */
   public abstract function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node;

   /**
    * Create a new Node to display/edit an new ELO
    *
    */
   public abstract function getScyWindowContentNew(scyWindow:ScyWindow):Node;

   public function supportType(type: String): Boolean{
      return true;
   }

}
