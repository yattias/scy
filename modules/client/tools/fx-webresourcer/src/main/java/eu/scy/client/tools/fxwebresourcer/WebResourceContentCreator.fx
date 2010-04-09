/*
 * WebresourceContentCreator.fx
 *
 * Created on 04.09.2009, 12:17:15
 */

package eu.scy.client.tools.fxwebresourcer;


import javafx.scene.Node;


import eu.scy.client.desktop.scydesktop.elofactory.ScyToolWindowContentCreatorFX;
 
/**
 * @author pg
 */

//public class WebResourceContentCreator extends WindowContentCreatorFX {
public class WebResourceContentCreator extends ScyToolWindowContentCreatorFX {
    override function createScyToolWindowContent():Node {
        return WebResourceNode{};
    }
 /*
   public var eloFactory:IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository:IRepository;

   public override function getScyWindowContent(eloUri: URI, scyWindow:ScyWindow):Node {
            var webNode = createWebResourceNode(scyWindow);
            webNode.loadElo(eloUri);
            return webNode;
    }

    public override function getScyWindowContentNew(scyWindow:ScyWindow):Node {
            return createWebResourceNode(scyWindow);
    }

    function createWebResourceNode(scyWindow:ScyWindow):WebResourceNode {
        setWindowProperties(scyWindow);
        var props:Properties = new Properties();
        props.put("show.filetoolbar", "false");
        //var modelEditor = new ModelEditor(props);
        var webNode:WebResourceNode = WebResourceNode {
                scyWindow: scyWindow};
//        var eloWebResourceActionWrapper = new EloWebResourceActionWrapper(webPanel);
        var eloWebResourceActionWrapper = new EloWebResourceActionWrapper(webNode);
        eloWebResourceActionWrapper.setRepository(repository);
        eloWebResourceActionWrapper.setMetadataTypeManager(metadataTypeManager);
        eloWebResourceActionWrapper.setEloFactory(eloFactory);
        eloWebResourceActionWrapper.setDocName(scyWindow.title);
        webNode.eloWebResourceActionWrapper = eloWebResourceActionWrapper;
        return webNode;
    }

        function setWindowProperties(scyWindow:ScyWindow) {
            scyWindow.minimumWidth = 320;
            scyWindow.minimumHeight = 100;
        }
  */
}
