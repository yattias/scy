package eu.scy.client.tools.fxrichtexteditor.registration;

import javafx.scene.Node;
import javafx.util.StringLocalizer;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

/**
 * @author kaido
 */

public class RichTextEditorContentCreatorFX extends ScyToolCreatorFX {
    public var authorMode:Boolean;
    public override function createScyToolNode(eloType:String, creatorId:String, scyWindow:ScyWindow, windowContent: Boolean):Node {
        StringLocalizer.associate("eu.scy.client.tools.fxrichtexteditor.registration.resources.RichTextEditorRegistration", "eu.scy.client.tools.fxrichtexteditor.registration");
        return RichTextEditorScyNode{authorMode:authorMode};
    }
}
