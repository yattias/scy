/*
 * DrawingtoolContentCreator.fx
 *
 * Created on 8-jul-2009, 15:41:12
 */

package eu.scy.client.tools.fxdrawingtool.registration;

import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorFX;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;

import colab.vt.whiteboard.component.WhiteboardPanel;

import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;

/**
 * @author sikkenj
 */

public class DrawingtoolContentCreator extends WindowContentCreatorFX {
   public var eloFactory:IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository:IRepository;

   public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
      var drawingNode = createDrawingNode(scyWindow);
      drawingNode.loadElo(eloUri);
      return drawingNode;
   }


   public override function getScyWindowContentNew(scyWindow:ScyWindow):Node{
      return createDrawingNode(scyWindow);
   }

	function createDrawingNode(scyWindow:ScyWindow):DrawingNode{
		var whiteboardPanel= new WhiteboardPanel();
		//whiteboardPanel.setPreferredSize(new Dimension(2000,2000));
		var eloDrawingActionWrapper= new EloDrawingActionWrapper(whiteboardPanel);
		eloDrawingActionWrapper.setRepository(repository);
		eloDrawingActionWrapper.setMetadataTypeManager(metadataTypeManager);
		eloDrawingActionWrapper.setEloFactory(eloFactory);
		return DrawingNode{
			whiteboardPanel:whiteboardPanel;
			eloDrawingActionWrapper:eloDrawingActionWrapper;
         scyWindow: scyWindow;
		}
	}

   function setWindowProperties(scyWindow:ScyWindow){
      scyWindow.minimumWidth = 320;
      scyWindow.minimumHeight = 100;
   }


}
