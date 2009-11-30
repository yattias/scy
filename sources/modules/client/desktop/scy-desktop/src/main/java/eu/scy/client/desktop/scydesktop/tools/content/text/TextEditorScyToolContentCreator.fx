/*
 * TextEditorScyToolContentCreator.fx
 *
 * Created on 30-nov-2009, 15:35:20
 */

package eu.scy.client.desktop.scydesktop.tools.content.text;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolWindowContentCreatorFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;



/**
 * @author sikken
 */

// place your code here
public class TextEditorScyToolContentCreator extends ScyToolWindowContentCreatorFX {

   public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
      var textEditorNode = createTextEditorNode(scyWindow);
      textEditorNode.loadElo(eloUri);
      return textEditorNode;
   }


   public override function getScyWindowContentNew(scyWindow:ScyWindow):Node{
      return createTextEditorNode(scyWindow);
   }

	function createTextEditorNode(scyWindow:ScyWindow):ScyTextEditorNode{
//      Thread.sleep(5000);
      setWindowProperties(scyWindow);
		var textEditor = new TextEditor();
		//whiteboardPanel.setPreferredSize(new Dimension(2000,2000));
		return ScyTextEditorNode{
			textEditor:textEditor;
		}
	}

   function setWindowProperties(scyWindow:ScyWindow){
      scyWindow.minimumWidth = 220;
      scyWindow.minimumHeight = 100;
   }


}
