/*
 * CopexToolCreatorFX.fx
 *
 * Created on 13 janv. 2010, 17:20:37
 */

package eu.scy.client.tools.fxcopex.registration;

import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import javafx.scene.Node;
/**
 * @author Marjolaine
 */

public class CopexToolCreatorFX extends ScyToolCreatorFX{
    override public function createScyToolNode () : Node {
        CopexNode{
           scyCopexPanel: new ScyCopexPanel();
        }

    }
}
