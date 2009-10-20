/*
 * SCYMapperContentCreator.fx
 *
 * Created on 8-jul-2009, 15:41:12
 */

package eu.scy.client.tools.fxscymapper.registration;

import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorFX;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;

import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;

import eu.scy.scymapper.impl.SCYMapperPanel;

/**
 * @author sikkenj
 */

public class SCYMapperContentCreator extends WindowContentCreatorFX {
   public var eloFactory:IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository:IRepository;

   public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
      var scyMapperNode = createScyMapperNode(scyWindow);
      scyMapperNode.loadElo(eloUri);
      return scyMapperNode;
   }


   public override function getScyWindowContentNew(scyWindow:ScyWindow):Node{
      return createScyMapperNode(scyWindow);
   }

	function createScyMapperNode(scyWindow:ScyWindow):SCYMapperNode{
      setWindowProperties(scyWindow);
		var scymapperPanel= new SCYMapperPanel();
		scymapperPanel.setRepository(repository);
		scymapperPanel.setEloFactory(eloFactory);
		scymapperPanel.setMetadataTypeManager(metadataTypeManager);
		//scymapperPanel.setPreferredSize(new Dimension(2000,2000));
		var eloScyMapperActionWrapper= new EloScyMapperActionWrapper(scymapperPanel);
		eloScyMapperActionWrapper.setRepository(repository);
		eloScyMapperActionWrapper.setMetadataTypeManager(metadataTypeManager);
		eloScyMapperActionWrapper.setEloFactory(eloFactory);
      eloScyMapperActionWrapper.setDocName(scyWindow.title);
		return SCYMapperNode{
			scyMapperPanel:scymapperPanel;
			eloSCYMapperActionWrapper:eloScyMapperActionWrapper;
         scyWindow: scyWindow;
		}
	}

   function setWindowProperties(scyWindow:ScyWindow){
      scyWindow.minimumWidth = 320;
      scyWindow.minimumHeight = 100;
   }


}
