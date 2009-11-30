/*
 * WebressourceContentCreator.fx
 *
 * Created on 04.09.2009, 12:17:15
 */

package eu.scy.client.tools.fxvideo;

import eu.scy.client.desktop.scydesktop.elofactory.WindowContentCreatorFX;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.net.URI;

import java.util.Properties;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
 
/**
 * @author pg
 */

public class VideoContentCreator extends WindowContentCreatorFX {

   public var eloFactory:IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository:IRepository;

   public override function getScyWindowContent(eloUri: URI, scyWindow:ScyWindow):Node {
            var webNode = createVideoNode(scyWindow);
            webNode.loadElo(eloUri);
            return webNode;
    }

    public override function getScyWindowContentNew(scyWindow:ScyWindow):Node {
            return createVideoNode(scyWindow);
    }

    function createVideoNode(scyWindow:ScyWindow):VideoNode {
        setWindowProperties(scyWindow);
        var props:Properties = new Properties();
        props.put("show.filetoolbar", "false");
        var videoNode:VideoNode = VideoNode {
                scyWindow: scyWindow};
//        var eloWebRessourceActionWrapper = new EloWebRessourceActionWrapper(webPanel);
        var eloVideoActionWrapper = new EloVideoActionWrapper(videoNode);
        eloVideoActionWrapper.setRepository(repository);
        eloVideoActionWrapper.setMetadataTypeManager(metadataTypeManager);
        eloVideoActionWrapper.setEloFactory(eloFactory);
        eloVideoActionWrapper.setDocName(scyWindow.title);
        videoNode.eloVideoActionWrapper = eloVideoActionWrapper; 
        return videoNode;
    }

        function setWindowProperties(scyWindow:ScyWindow) {
            scyWindow.minimumWidth = 320;
            scyWindow.minimumHeight = 200;
        }
}
