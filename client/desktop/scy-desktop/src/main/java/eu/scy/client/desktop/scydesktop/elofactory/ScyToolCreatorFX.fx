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
    * The eloType and creatorId can be used to determine what Node to return, in case the creator is capable of returning multiple types.
    *
    * @param eloType - the technical type of the elo in the window
    * @param creatorId - the id of the registered creator
    * @param window - the ScyWindow where the tool will be placed in
    * @param windowContent - true if the tool will be placed in the window, other is will be placed in a drawer
    */
   public abstract function createScyToolNode(eloType:String, creatorId:String, window:ScyWindow, windowContent: Boolean):Node;

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
