/*
 * TextEditorToolContentCreator.fx
 *
 * Created on 29-sep-2009, 20:36:37
 */

package eu.scy.client.desktop.scydesktop.tools.content.text;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;

/**
 * @author sikken
 */

public class TextEditorToolContentCreator extends WindowContentCreatorFX {
   public var eloFactory:IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository:IRepository;

   public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
      var textEditorNode = createTextEditorNode(scyWindow);
      textEditorNode.loadElo(eloUri);
      return textEditorNode;
   }


   public override function getScyWindowContentNew(scyWindow:ScyWindow):Node{
      return createTextEditorNode(scyWindow);
   }

	function createTextEditorNode(scyWindow:ScyWindow):TextEditorNode{
		var textEditor= new TextEditor();
		//whiteboardPanel.setPreferredSize(new Dimension(2000,2000));
		var eloTextEditorActionWrapper= new EloTextEditorActionWrapper(textEditor);
		eloTextEditorActionWrapper.setRepository(repository);
		eloTextEditorActionWrapper.setMetadataTypeManager(metadataTypeManager);
		eloTextEditorActionWrapper.setEloFactory(eloFactory);
		return TextEditorNode{
			textEditor:textEditor;
         eloTextEditorActionWrapper:eloTextEditorActionWrapper
         scyWindow: scyWindow;
		}
	}

   function setWindowProperties(scyWindow:ScyWindow){
      scyWindow.minimumWidth = 320;
      scyWindow.minimumHeight = 100;
   }


}
