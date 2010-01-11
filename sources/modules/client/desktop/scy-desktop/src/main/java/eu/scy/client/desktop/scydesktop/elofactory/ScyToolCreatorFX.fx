/*
 * ScyToolCreatorFX.fx
 *
 * Created on 11-jan-2010, 12:18:53
 */

package eu.scy.client.desktop.scydesktop.elofactory;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * @author sikken
 */

public mixin class ScyToolCreatorFX {

   /**
    * Create a new Node to display/edit an new ELO
    *
    */
   public abstract function createScyToolNode():Node;

   public function supportType(type: String): Boolean{
      return true;
   }

}
