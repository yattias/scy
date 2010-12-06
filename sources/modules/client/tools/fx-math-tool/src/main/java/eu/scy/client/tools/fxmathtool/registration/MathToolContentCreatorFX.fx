package eu.scy.client.tools.fxmathtool.registration;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolWindowContentCreatorFX;
import javafx.util.StringLocalizer;

/**
 * @author aperritano
 */

public class MathToolContentCreatorFX extends ScyToolWindowContentCreatorFX {
    public var authorMode:Boolean;
    public override function createScyToolWindowContent():Node{
        StringLocalizer.associate("eu.scy.client.tools.fxmathtool.registration.resources.RichTextEditorRegistration", "eu.scy.client.tools.fxmathtool.registration");
        return MathToolScyNode{authorMode:authorMode};
    }
}
