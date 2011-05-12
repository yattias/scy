/*
 * EdgesManager.fx
 *
 * Created on 08.01.2010, 11:35:55
 */
package eu.scy.client.desktop.scydesktop.edges;

import javafx.scene.Node;
import javafx.scene.CustomNode;
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
import eu.scy.client.desktop.scydesktop.scywindows.window.StandardScyWindow;
import java.lang.Void;
import eu.scy.client.desktop.scydesktop.scywindows.DatasyncAttribute;
import eu.scy.client.desktop.desktoputils.log4j.Logger;
import javafx.scene.shape.Circle;
import eu.scy.client.desktop.scydesktop.edges.DatasyncEdge;

public class EdgesManager extends IEdgesManager {

    var nodes: Node[];
    var datasyncNodes: Node[];
    public var windowManager: WindowManager;
    public var repository: IRepository;
    public var metadataTypeManager: IMetadataTypeManager;
    public-init var showEloRelations: Boolean;
    def logger = Logger.getLogger(this.getClass());

    public override function addDatasyncLink(scyWindowStart: ScyWindow, scyWindowEnd: ScyWindow): DatasyncEdge {
        def edge: DatasyncEdge = DatasyncEdge {
                    scyWindowStart: scyWindowStart;
                    scyWindowEnd: scyWindowEnd;
                    manager: this;
                    visible: true;
                    opacity: 0.5
                }
        logger.error("adding a datasync-edge {edge} from {scyWindowStart} to {scyWindowEnd}.");
        insert edge into nodes;
        insert edge into datasyncNodes;
        return edge;
    }

    public override function removeDatasyncLink(edge: DatasyncEdge): Void {
        delete edge from datasyncNodes;
        delete edge from nodes;
    }

    public function addLink(source: ScyWindow, target: ScyWindow, text: String): Void {
        FX.deferAction(function() :Void {
            def edge: Edge = Edge {
                        start: (source as StandardScyWindow);
                        end: (target as StandardScyWindow);
                        manager: this;
                        text: text;
                        visible: showEloRelations;
                        opacity: 0.0
                    }
            insert edge into nodes;
            Timeline {
                keyFrames: [at (0.5s) {edge.opacity => 0.3 tween Interpolator.EASEIN}]
            }.play();
        });
    }

    public override function findLinks(sourceWindow: ScyWindow) {
        logger.error("finding links...");
        delete  nodes;
        // insert datasyncnodes if according window is visible
        insertDatasyncNodes();

        var f = function() {
            if (not (sourceWindow.eloUri == null) and sourceWindow.eloUri.toString() != "") {
                def metadata: IMetadata = repository.retrieveMetadata(sourceWindow.eloUri);
                var targetURI: URI;
                var targetWindow;
                // show "IS_VERSION_OF"-Relation
                targetURI = metadata.getMetadataValueContainer(metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IS_VERSION_OF.getId())).getValue() as URI;
                targetWindow = windowManager.findScyWindow(targetURI);
                if (not (targetURI == null) and not (targetWindow == null)) {
                    addLink(sourceWindow, targetWindow, "is version of");
                }

                // show "IS_VERSIONED_BY"-Relation
                targetURI = metadata.getMetadataValueContainer(metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IS_VERSIONED_BY.getId())).getValue() as URI;
                targetWindow = windowManager.findScyWindow(targetURI);
                if (not (targetURI == null) and not (targetWindow == null)) {
                    addLink(sourceWindow, targetWindow, "is versioned by");
                }

                // show "IS_FORK_OF"-Relation
                targetURI = metadata.getMetadataValueContainer(metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IS_FORK_OF.getId())).getValue() as URI;
                targetWindow = windowManager.findScyWindow(targetURI);
                if (not (targetURI == null) and not (targetWindow == null)) {
                    addLink(sourceWindow, targetWindow, "is fork of");
                }

                // show "IS_FORKED_BY"-Relation
                targetURI = metadata.getMetadataValueContainer(metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.IS_FORKED_BY.getId())).getValue() as URI;
                targetWindow = windowManager.findScyWindow(targetURI);
                if (not (targetURI == null) and not (targetWindow == null)) {
                    addLink(sourceWindow, targetWindow, "is forked by");
                }
            }
        };
        // as we don't want to have version / fork-edges anymore, we
        // don't call function f anymore
//        var t = BackgroundTask {
//            backgroundFunction: f;
//            };
//        t.start();
    }

    protected function insertDatasyncNodes() {
        for (datasyncNode in datasyncNodes) {
            var window = windowManager.findScyWindow((datasyncNode as DatasyncEdge).scyWindowStart.eloUri);
            if (not (window == null)) {
		logger.error("insert {datasyncNode} into {sizeof nodes}");
                insert datasyncNode into nodes;
                (datasyncNode as DatasyncEdge).update();
            }
        }
    }

    override protected function create(): Node {
        var g: Group = Group {
                    content: bind nodes;
                }
    }

}
