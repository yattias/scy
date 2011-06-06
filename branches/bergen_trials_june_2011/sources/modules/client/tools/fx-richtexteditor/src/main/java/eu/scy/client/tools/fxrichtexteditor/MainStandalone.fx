package eu.scy.client.tools.fxrichtexteditor;

/**
 * @author kaido
 */

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.util.StringLocalizer;
import eu.scy.actionlogging.DevNullActionLogger;
import javafx.scene.text.Font;
import eu.scy.client.tools.fxrichtexteditor.registration.RichTextEditorNode;

var myLocale : java.util.Locale = new java.util.Locale("en", "US");
java.util.Locale.setDefault(myLocale);
StringLocalizer.associate("eu.scy.client.tools.fxrichtexteditor.registration.resources.RichTextEditorRegistration", "eu.scy.client.tools.fxrichtexteditor.registration");
def node:RichTextEditorNode = RichTextEditorNode{};
var w: Number = 420 on replace {
    if (w<420) {
        node.width = 400;
    } else {
        node.width=w-20;
    }
}
var h: Number = 240 on replace {
    if (h<240) {
        node.height = 200;
    } else {
        node.height=h-40;
    }
}
Stage {
	title : "Rich text editor"
        width: bind w with inverse
        height: bind h with inverse
	scene: Scene {
		content: [ node ]
	}
}
