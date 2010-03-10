/*
 * EdgesManager.fx
 *
 * Created on 08.01.2010, 11:35:55
 */

package eu.scy.client.desktop.scydesktop.edges;

import javafx.scene.Node;
import javafx.scene.Group;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import roolo.api.IRepository;
import roolo.elo.api.IMetadataTypeManager;
import java.lang.Void;
import roolo.elo.api.IMetadata;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.scywindows.WindowManager;
import java.net.URI;


public class EdgesManager extends IEdgesManager {

    var nodes:Node[];

    public var windowManager:WindowManager;
    public var repository:IRepository;
    public var metadataTypeManager:IMetadataTypeManager;

    public function addLink(source:ScyWindow, target:ScyWindow, text:String):Void {
            var edge:Edge = Edge {
                start: source
                end: target
                manager: this;
                text: text;
            }
            insert edge into nodes;
    }

    public override function findLinks(sourceWindow:ScyWindow) {
        delete nodes;
        def metadata:IMetadata = repository.retrieveMetadata(sourceWindow.eloUri);
        // IS_VERSION_OF
        var targetURI:URI = metadata.getMetadataValueContainer(metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IS_VERSION_OF.getId())).getValue() as URI;
        var targetWindow = windowManager.findScyWindow(targetURI);
        if(not(targetWindow == null)) {
            addLink(sourceWindow, targetWindow, "is version of");
        }

        targetURI = metadata.getMetadataValueContainer(metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IS_VERSIONED_BY.getId())).getValue() as URI;
        targetWindow = windowManager.findScyWindow(targetURI);
        if(not(targetWindow == null)) {
            addLink(sourceWindow, targetWindow, "is versioned by");
        }

        targetURI = metadata.getMetadataValueContainer(metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IS_FORK_OF.getId())).getValue() as URI;
        targetWindow = windowManager.findScyWindow(targetURI);
        if(not(targetWindow == null)) {
            addLink(sourceWindow, targetWindow, "is fork of");
        }

        targetURI = metadata.getMetadataValueContainer(metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IS_FORKED_BY.getId())).getValue() as URI;
        targetWindow = windowManager.findScyWindow(targetURI);
        if(not(targetWindow == null)) {
            addLink(sourceWindow, targetWindow, "is forked by");
        }
    }

    override protected function create () : Node {
        var g:Group = Group {
                content: bind nodes;
        }

    }


}
