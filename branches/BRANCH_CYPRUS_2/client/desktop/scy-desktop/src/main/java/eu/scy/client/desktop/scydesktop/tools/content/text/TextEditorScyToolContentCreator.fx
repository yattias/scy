/*
 * TextEditorScyToolContentCreator.fx
 *
 * Created on 30-nov-2009, 15:35:20
 */

package eu.scy.client.desktop.scydesktop.tools.content.text;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;


/**
 * @author sikken
 */

// place your code here
public class TextEditorScyToolContentCreator extends ScyToolCreatorFX {

   public override function createScyToolNode(eloType:String, creatorId:String, scyWindow:ScyWindow,windowContent:Boolean):Node{
      scyWindow.desiredContentWidth = 250;
      scyWindow.desiredContentHeight = 200;
      return createTextEditorNode();
   }

	function createTextEditorNode():ScyTextEditorNode{
//      Thread.sleep(3000);
		return ScyTextEditorNode{
		}
	}
}
