package eu.scy.client.tools.fxrichtexteditor.registration;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolWindowContentCreatorFX;
import javafx.util.StringLocalizer;

/**
 * @author kaido
 */

public class RichTextEditorContentCreatorFX extends ScyToolWindowContentCreatorFX {
    public var authorMode:Boolean;
    public override function createScyToolWindowContent():Node{
        StringLocalizer.associate("eu.scy.client.tools.fxrichtexteditor.registration.resources.RichTextEditorRegistration", "eu.scy.client.tools.fxrichtexteditor.registration");
        return RichTextEditorNode{authorMode:authorMode};
    }
}
