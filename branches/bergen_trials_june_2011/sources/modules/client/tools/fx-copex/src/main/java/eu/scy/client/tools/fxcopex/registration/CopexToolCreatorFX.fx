/*
 * CopexToolCreatorFX.fx
 *
 * Created on 13 janv. 2010, 17:20:37
 */

package eu.scy.client.tools.fxcopex.registration;

import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import javafx.scene.Node;

import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
/**
 * @author Marjolaine
 */

public class CopexToolCreatorFX extends ScyToolCreatorFX{
    override public function createScyToolNode (eloType:String, creatorId: String, scyWindow:ScyWindow, windowContent:Boolean) : Node {
        CopexNode{
           scyWindow: scyWindow
           scyCopexPanel: new ScyCopexPanel(creatorId);
        }

    }
}
