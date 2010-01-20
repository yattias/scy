package eu.scy.client.tools.fxscydynamics.registration;

import eu.scy.client.desktop.scydesktop.elofactory.ScyToolCreatorFX;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import java.util.Properties;

public class ScyDynamicsContentCreator extends ScyToolCreatorFX {
        
   //public var eloFactory:IELOFactory;
   //public var metadataTypeManager: IMetadataTypeManager;
   //public var repository:IRepository;

    override public function createScyToolNode (type:String, scyWindow:ScyWindow, windowContent: Boolean) : Node {
        var props:Properties = new Properties();
        props.put("show.filetoolbar", "false");
        ScyDynamicsNode{
           modelEditor: new ModelEditor(props);
           scyWindow:scyWindow
        }
    }

   /*function createScyDynamicsNode(scyWindow:ScyWindow):ScyDynamicsNode{
      setWindowProperties(scyWindow);
      var props:Properties = new Properties();
      props.put("show.filetoolbar", "false");
      var modelEditor = new ModelEditor(props);
      var eloModelWrapper = new EloModelWrapper(modelEditor);
      eloModelWrapper.setRepository(repository);
      eloModelWrapper.setMetadataTypeManager(metadataTypeManager);
      eloModelWrapper.setEloFactory(eloFactory);
      eloModelWrapper.setDocName(scyWindow.title);
      return ScyDynamicsNode{
        modelEditor:modelEditor;
        eloModelWrapper:eloModelWrapper;
      }
   }

   function setWindowProperties(scyWindow:ScyWindow){
      scyWindow.minimumWidth = 320;
      scyWindow.minimumHeight = 100;
   }

      public override function getScyWindowContent(eloUri:URI, scyWindow:ScyWindow):Node{
      var scyDynamicsNode:ScyDynamicsNode = createScyDynamicsNode(scyWindow);
      scyDynamicsNode.loadElo(eloUri);
      return scyDynamicsNode;
   }

   public override function getScyWindowContentNew(scyWindow:ScyWindow):Node{
      return createScyDynamicsNode(scyWindow);
   }*/

}
