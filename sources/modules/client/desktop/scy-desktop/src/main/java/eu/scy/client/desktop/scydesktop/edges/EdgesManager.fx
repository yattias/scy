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
import javafx.animation.Timeline;
import javafx.animation.Interpolator;

public class EdgesManager extends IEdgesManager {

    var nodes: Node[];
    public var windowManager: WindowManager;
    public var repository: IRepository;
    public var metadataTypeManager: IMetadataTypeManager;
    public-init var showEloRelations: Boolean;

    public function addLink(source: ScyWindow, target: ScyWindow, text: String): Void {
        def edge: Edge = Edge {
                    start: source
                    end: target
                    manager: this;
                    text: text;
                    visible: showEloRelations;
                    opacity: 0.0
                }
        insert edge into nodes;
        Timeline {
            keyFrames: [at (0.5s) {edge.opacity => 1.0 tween Interpolator.EASEIN}]
        }.play();
    }

    public override function findLinks(sourceWindow: ScyWindow)   {
        delete nodes;
        if (not(sourceWindow.eloUri==null) and sourceWindow.eloUri.toString() != "" ) {
            def metadata: IMetadata = repository.retrieveMetadata(sourceWindow.eloUri);
            var targetURI: URI;
            var targetWindow;

            // show "IS_VERSION_OF"-Relation
            targetURI = metadata.getMetadataValueContainer(metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IS_VERSION_OF.getId())).getValue() as URI;
            targetWindow = windowManager.findScyWindow(targetURI);
            if (not (targetWindow == null)) {
                addLink(sourceWindow, targetWindow, "is version of");
            }

            // show "IS_VERSIONED_BY"-Relation
            targetURI = metadata.getMetadataValueContainer(metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IS_VERSIONED_BY.getId())).getValue() as URI;
            targetWindow = windowManager.findScyWindow(targetURI);
            if (not (targetWindow == null)) {
                addLink(sourceWindow, targetWindow, "is versioned by");
            }

            // show "IS_FORK_OF"-Relation
            targetURI = metadata.getMetadataValueContainer(metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IS_FORK_OF.getId())).getValue() as URI;
            targetWindow = windowManager.findScyWindow(targetURI);
            if (not (targetWindow == null)) {
                addLink(sourceWindow, targetWindow, "is fork of");
            }

            // show "IS_FORKED_BY"-Relation
            targetURI = metadata.getMetadataValueContainer(metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IS_FORKED_BY.getId())).getValue() as URI;
            targetWindow = windowManager.findScyWindow(targetURI);
            if (not (targetWindow == null)) {
                addLink(sourceWindow, targetWindow, "is forked by");
            }
        }
    }

    override protected function create(): Node {
        var g: Group = Group {
                    content: bind nodes;
                }
    }

}
