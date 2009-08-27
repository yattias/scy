package eu.scy.client.tools.fxscydynamics.registration;

import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorFX;
import javafx.scene.Node;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.client.tools.scydynamics.editor.ModelEditor;

public class ScyDynamicsContentCreator extends WindowContentCreatorFX {
        
   public var eloFactory:IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository:IRepository;

   public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
      var scyDynamicsNode:ScyDynamicsNode = createScyDynamicsNode(scyWindow);
      scyDynamicsNode.loadElo(eloUri);
      return scyDynamicsNode;
   }


   public override function getScyWindowContentNew(scyWindow:ScyWindow):Node{
      return createScyDynamicsNode(scyWindow);
   }

   function createScyDynamicsNode(scyWindow:ScyWindow):ScyDynamicsNode{
	var modelEditor = new ModelEditor();
        var eloModelWrapper = new EloModelWrapper(modelEditor);
        eloModelWrapper.setRepository(repository);
		eloModelWrapper.setMetadataTypeManager(metadataTypeManager);
		eloModelWrapper.setEloFactory(eloFactory);

        return ScyDynamicsNode{
            modelEditor:modelEditor;
            eloModelWrapper:eloModelWrapper;
    }
	}

   function setWindowProperties(scyWindow:ScyWindow){
      scyWindow.minimumWidth = 320;
      scyWindow.minimumHeight = 100;
   }


}
