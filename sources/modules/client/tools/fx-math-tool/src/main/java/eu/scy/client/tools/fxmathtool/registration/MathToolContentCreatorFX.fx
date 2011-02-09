package eu.scy.client.tools.fxmathtool.registration;

import javafx.scene.Node;
import javafx.util.StringLocalizer;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * @author aperritano
 */

public class MathToolContentCreatorFX extends ScyToolCreatorFX {
    public var authorMode:Boolean;
    public override function createScyToolNode(eloType:String, creatorId:String, scyWindow:ScyWindow, windowContent: Boolean):Node {
        StringLocalizer.associate("eu.scy.client.tools.fxmathtool.registration.resources.RichTextEditorRegistration", "eu.scy.client.tools.fxmathtool.registration");
        return MathToolScyNode{authorMode:authorMode};
    }
}
