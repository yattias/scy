/*
 * WebresourceContentCreator.fx
 *
 * Created on 04.09.2009, 12:17:15
 */

package eu.scy.client.tools.fxwebresourcer;


import javafx.scene.Node;


import javafx.util.StringLocalizer;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
 
/**
 * @author pg
 */

public class WebResourceContentCreator extends ScyToolCreatorFX {
    public override function createScyToolNode(eloType:String, creatorId:String, scyWindow:ScyWindow, windowContent: Boolean):Node {
        StringLocalizer.associate("eu.scy.client.tools.fxwebresourcer.resources.WebResourcer", "eu.scy.client.tools.fxwebresourcer");
        return WebResourceNode{};
    }
}
