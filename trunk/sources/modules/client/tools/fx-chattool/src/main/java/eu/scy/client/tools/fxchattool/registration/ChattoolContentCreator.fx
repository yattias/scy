/*
 * DrawingtoolContentCreator.fx
 *
 * Created on 8-jul-2009, 15:41:12
 */

package eu.scy.client.tools.fxchattool.registration;

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

public class ChattoolContentCreator extends WindowContentCreatorFX {
   public var eloFactory:IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository:IRepository;

   public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
      var chatNode = createChatNode(scyWindow);
      chatNode.loadElo(eloUri);
      return chatNode;
   }


   public override function getScyWindowContentNew(scyWindow:ScyWindow):Node{
      return createChatNode(scyWindow);
   }

	function createChatNode(scyWindow:ScyWindow):ChatNode {
		var whiteboardPanel= new WhiteboardPanel();
		//whiteboardPanel.setPreferredSize(new Dimension(2000,2000));
		var eloChatActionWrapper= new EloChatActionWrapper(whiteboardPanel);
		eloChatActionWrapper.setRepository(repository);
		eloChatActionWrapper.setMetadataTypeManager(metadataTypeManager);
		eloChatActionWrapper.setEloFactory(eloFactory);
		return ChatNode{
			whiteboardPanel:whiteboardPanel;
			eloChatActionWrapper:eloChatActionWrapper;
                        scyWindow: scyWindow;
		}
	}

   function setWindowProperties(scyWindow:ScyWindow){
      scyWindow.minimumWidth = 320;
      scyWindow.minimumHeight = 100;
   }


}
