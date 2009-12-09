/*
 * PictureViewerContentCreator.fx
 *
 * Created on 15.09.2009, 14:20:31
 */

package eu.scy.client.tools.fxpictureviewer;

import javafx.scene.Node;


import eu.scy.client.desktop.scydesktop.elofactory.ScyToolWindowContentCreatorFX;

/**
 * @author pg
 */

//public class PictureViewerContentCreator extends WindowContentCreatorFX {
public class PictureViewerContentCreator extends ScyToolWindowContentCreatorFX {

        override function createScyToolWindowContent():Node {
                return PictureViewerNode{};
        }


        /*
       public var eloFactory:IELOFactory;
       public var metadataTypeManager: IMetadataTypeManager;
       public var repository:IRepository;

        public override function getScyWindowContent(eloUri: URI, scyWindow:ScyWindow):Node {
            var pictureNode:PictureViewerNode = this.createPictureViewerNode(scyWindow);
            pictureNode.loadElo(eloUri);
            return pictureNode;
        }

        public override function getScyWindowContentNew(scyWindow:ScyWindow):Node {
            return createPictureViewerNode(scyWindow);
        }

        public function createPictureViewerNode(scyWindow:ScyWindow):PictureViewerNode {
            var picNode:PictureViewerNode = PictureViewerNode {
                    scyWindow: scyWindow

            }
            var eloPictureActionWrapper = new EloPictureActionWrapper(picNode);
            eloPictureActionWrapper.setRepository(repository);
            eloPictureActionWrapper.setMetadataTypeManager(metadataTypeManager);
            eloPictureActionWrapper.setEloFactory(eloFactory);
            eloPictureActionWrapper.setDocName(scyWindow.title);
            picNode.eloPictureActionWrapper = eloPictureActionWrapper;
            return picNode;
        }

        function setWindowProperties(scyWindow:ScyWindow) {
            scyWindow.minimumWidth = 320;
            scyWindow.minimumHeight = 100;
        }
        */
}
