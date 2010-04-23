/*
 * TextEditorToolContentCreator.fx
 *
 * Created on 29-sep-2009, 20:36:37
 */

package eu.scy.client.desktop.scydesktop.tools.content.eloImporter;

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

public class ELOImporterToolContentCreator extends WindowContentCreatorFX {
   public var eloFactory:IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository:IRepository;

   public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
      var eloImporterNode = createELOImporterNode(scyWindow);
      //textEditorNode.loadElo(eloUri);
      return eloImporterNode;
   }


   public override function getScyWindowContentNew(scyWindow:ScyWindow):Node{
      return createELOImporterNode(scyWindow);
   }

	function createELOImporterNode(scyWindow:ScyWindow):ELOImporterNode{
//      Thread.sleep(5000);
      setWindowProperties(scyWindow);
		var eloImporterModel= new EloImporterModel();
		//whiteboardPanel.setPreferredSize(new Dimension(2000,2000));
		var eloImporterActionWrapper= new EloImporterActionWrapper(eloImporterModel);
		eloImporterActionWrapper.setRepository(repository);
		eloImporterActionWrapper.setMetadataTypeManager(metadataTypeManager);
		eloImporterActionWrapper.setEloFactory(eloFactory);
                eloImporterActionWrapper.setDocName(scyWindow.title);
		return ELOImporterNode{
			eloImporterModel:eloImporterModel;
                        eloImporterActionWrapper:eloImporterActionWrapper
                        scyWindow: scyWindow;
		}
	}

   function setWindowProperties(scyWindow:ScyWindow){
      scyWindow.minimumWidth = 300;
      scyWindow.minimumHeight = 300;
   }


}
