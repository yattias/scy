/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.scywindows.scydesktop.test;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.ScyDesktop;

/**
 * @author SikkenJ
 */

public class TestDialogsNodeCreator extends ScyToolCreatorFX{

   public var scyDesktop: ScyDesktop;

    override public function createScyToolNode (eloType:String,creatorId:String, scyWindow:ScyWindow, windowContent:Boolean) : Node {
        TestDialogsNode{
            scyDesktop: scyDesktop
        }
    }

}
