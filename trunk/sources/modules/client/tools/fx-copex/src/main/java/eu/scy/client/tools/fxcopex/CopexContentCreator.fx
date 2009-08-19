/*
 * CopexContentCreator.fx
 *
 * Created on 18 août 2009, 15:18:26
 */

package eu.scy.client.tools.fxcopex;

import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorFX;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;


import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;


/**
 * @author Marjolaine
 */

public class CopexContentCreator  extends WindowContentCreatorFX {
   public var eloFactory:IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository:IRepository;

   public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
      var copexNode = createCopexNode(scyWindow);
      copexNode.loadElo(eloUri);
      return copexNode;
   }


   public override function getScyWindowContentNew(scyWindow:ScyWindow):Node{
      return createCopexNode(scyWindow);
   }

	function createCopexNode(scyWindow:ScyWindow):CopexNode{
		var copexPanel= new CopexPanel();
		var eloCopexActionWrapper= new EloCopexActionWrapper(copexPanel);
		eloCopexActionWrapper.setRepository(repository);
		eloCopexActionWrapper.setMetadataTypeManager(metadataTypeManager);
		eloCopexActionWrapper.setEloFactory(eloFactory);
		return CopexNode{
			copexPanel:copexPanel;
			eloCopexActionWrapper:eloCopexActionWrapper;
            scyWindow: scyWindow;
		}
	}

   function setWindowProperties(scyWindow:ScyWindow){
      scyWindow.minimumWidth = 320;
      scyWindow.minimumHeight = 100;
   }
}
