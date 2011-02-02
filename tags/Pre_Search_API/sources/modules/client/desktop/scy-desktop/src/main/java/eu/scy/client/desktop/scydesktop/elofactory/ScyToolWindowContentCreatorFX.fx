/*
 * ScyToolWindowContentCreatorFX.fx
 *
 * Created on 30-nov-2009, 12:06:30
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import javafx.scene.Node;

import java.net.URI;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * This interface
 * @author sikken
 */

// place your code here

public mixin class ScyToolWindowContentCreatorFX extends WindowContentCreatorFX{

   /**
   * Create the node representing your tool. You do not need to load an elo or so, the ScyTool interface will tell you when.
   */
   public abstract function createScyToolWindowContent():Node;

   public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
      return null;
   }

   public override function getScyWindowContentNew(scyWindow:ScyWindow):Node{
      return null;
   }


}
