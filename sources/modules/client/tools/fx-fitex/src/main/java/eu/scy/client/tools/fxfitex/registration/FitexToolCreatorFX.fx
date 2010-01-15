/*
 * FitexToolCreatorFX.fx
 *
 */

package eu.scy.client.tools.fxfitex.registration;

import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import javafx.scene.Node;

/**
 * @author Marjolaine
 */

public class FitexToolCreatorFX extends ScyToolCreatorFX{
    override public function createScyToolNode () : Node {
        FitexNode{
           fitexPanel: new FitexPanel();
        }

    }
}
