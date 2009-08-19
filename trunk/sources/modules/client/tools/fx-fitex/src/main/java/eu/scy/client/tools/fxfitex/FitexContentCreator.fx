/*
 * FitexContentCreator.fx
 *
 * Created on 17 août 2009, 14:23:44
 */

package eu.scy.client.tools.fxfitex;

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

public class FitexContentCreator  extends WindowContentCreatorFX {
   public var eloFactory:IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository:IRepository;

   public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
      var fitexNode = createFitexNode(scyWindow);
      fitexNode.loadElo(eloUri);
      return fitexNode;
   }


   public override function getScyWindowContentNew(scyWindow:ScyWindow):Node{
      return createFitexNode(scyWindow);
   }

	function createFitexNode(scyWindow:ScyWindow):FitexNode{
		var fitexPanel= new FitexPanel();
		var eloFitexActionWrapper= new EloFitexActionWrapper(fitexPanel);
		eloFitexActionWrapper.setRepository(repository);
		eloFitexActionWrapper.setMetadataTypeManager(metadataTypeManager);
		eloFitexActionWrapper.setEloFactory(eloFactory);
		return FitexNode{
			fitexPanel:fitexPanel;
			eloFitexActionWrapper:eloFitexActionWrapper;
         scyWindow: scyWindow;
		}
	}

   function setWindowProperties(scyWindow:ScyWindow){
      scyWindow.minimumWidth = 320;
      scyWindow.minimumHeight = 100;
   }
}
