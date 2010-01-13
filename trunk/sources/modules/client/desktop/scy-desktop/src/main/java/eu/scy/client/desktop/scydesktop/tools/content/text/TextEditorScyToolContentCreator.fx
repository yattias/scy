/*
 * TextEditorScyToolContentCreator.fx
 *
 * Created on 30-nov-2009, 15:35:20
 */

package eu.scy.client.desktop.scydesktop.tools.content.text;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;


/**
 * @author sikken
 */

// place your code here
public class TextEditorScyToolContentCreator extends ScyToolCreatorFX {

   public override function createScyToolNode():Node{
      return createTextEditorNode();
   }

	function createTextEditorNode():ScyTextEditorNode{
//      Thread.sleep(3000);
		var textEditor = new TextEditor();
		return ScyTextEditorNode{
			textEditor:textEditor;
		}
	}
}
