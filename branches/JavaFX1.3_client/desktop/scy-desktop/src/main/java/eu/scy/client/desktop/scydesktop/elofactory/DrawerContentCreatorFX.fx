/*
 * DrawerContentCreatorFX.fx
 *
 * Created on 22-sep-2009, 17:09:50
 */

package eu.scy.client.desktop.scydesktop.elofactory;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;

/**
 * @author sikkenj
 */

// place your code here
public mixin class DrawerContentCreatorFX extends ContentCreator{
   /**
    * Create a new Node to place into a drawer an ELO window, with an existing ELO
    *
    */
   public abstract function getDrawerContent(eloUri:URI, scyWindow:ScyWindow):Node;

}
