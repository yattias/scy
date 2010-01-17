/*
 * FitexToolCreatorFX.fx
 *
 */

package eu.scy.client.tools.fxfitex.registration;

import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * @author Marjolaine
 */

public class FitexToolCreatorFX extends ScyToolCreatorFX{
    override public function createScyToolNode (type:String, scyWindow:ScyWindow, windowContent:Boolean) : Node {
        FitexNode{
           fitexPanel: new FitexPanel();
        }

    }
}
