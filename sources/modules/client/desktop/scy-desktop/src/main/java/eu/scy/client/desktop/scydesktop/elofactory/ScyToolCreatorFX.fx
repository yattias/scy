/*
 * ScyToolCreatorFX.fx
 *
 * Created on 11-jan-2010, 12:18:53
 */

package eu.scy.client.desktop.scydesktop.elofactory;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * The interface for creating new instances JavaFX tools in SCY-LAB.
 *
 * @author sikken
 */
public mixin class ScyToolCreatorFX {

   /**
    * Create a new Node to display/edit an new ELO
    *
    */
   public abstract function createScyToolNode():Node;

   /**
    * Check if the creator supports the given type
    *
    * @param type
    * @return true if the type is supported, otherwise false
    */
   public function supportType(type: String): Boolean{
      return true;
   }

}
