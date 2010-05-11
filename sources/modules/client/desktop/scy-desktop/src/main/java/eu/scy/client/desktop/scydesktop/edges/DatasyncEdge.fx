package eu.scy.client.desktop.scydesktop.edges;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.paint.*;
import eu.scy.client.desktop.scydesktop.scywindows.DatasyncAttribute;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import javafx.animation.Timeline;
import javafx.animation.Interpolator;
import eu.scy.client.common.datasync.ISyncSession;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.common.datasync.ISyncListener;
import eu.scy.common.datasync.ISyncObject;

/**
 * @author lars
 */
public class DatasyncEdge extends CustomNode, ISyncListener {

    public-init var manager: EdgesManager;
    public-init var startAttrib: DatasyncAttribute;
    public-init var endAttrib: DatasyncAttribute;
    var session: ISyncSession;
    def logger = Logger.getLogger(this.getClass());
    public-read var line: Line = Line {
                startX: startAttrib.localToScene(startAttrib.cableStartX, startAttrib.cableStartY).x;
                startY: startAttrib.localToScene(startAttrib.cableStartX, startAttrib.cableStartY).y;
                endX: endAttrib.localToScene(endAttrib.cableStartX, endAttrib.cableStartY).x;
                endY: endAttrib.localToScene(endAttrib.cableStartX, endAttrib.cableStartY).y;
                strokeWidth: 3.0;
                //stroke: startAttrib.scyWindow.windowColorScheme.mainColor;
                stroke: LinearGradient {
                    startX: 0.0, startY: 0.0, endX: 1.0, endY: 0.0
                    proportional: true
                    stops: [Stop { offset: 0.0 color: startAttrib.scyWindow.windowColorScheme.mainColor },
                        Stop { offset: 1.0 color: endAttrib.scyWindow.windowColorScheme.mainColor }]
                }
            }
    var startWatchX = bind startAttrib.scyWindow.layoutX on replace {
                update()
            };
    var startWatchY = bind startAttrib.scyWindow.layoutX on replace {
                update()
            };
    var startWatchRot = bind startAttrib.scyWindow.layoutX on replace {
                update()
            };
    var endWatchX = bind endAttrib.scyWindow.layoutX on replace {
                update()
            };
    var endWatchY = bind endAttrib.scyWindow.layoutX on replace {
                update()
            };
    var endWatchRot = bind endAttrib.scyWindow.layoutX on replace {
                update()
            };

    function update(): Void {
        line.startX = startAttrib.localToScene(startAttrib.cableStartX, startAttrib.cableStartY).x;
        line.startY = startAttrib.localToScene(startAttrib.cableStartX, startAttrib.cableStartY).y;
        line.endX = endAttrib.localToScene(endAttrib.cableStartX, endAttrib.cableStartY).x;
        line.endY = endAttrib.localToScene(endAttrib.cableStartX, endAttrib.cableStartY).y;
    }

    public function join(mucID: String, tbi: ToolBrokerAPI): Void {
        session = tbi.getDataSyncService().joinSession(mucID, this, "datasyncedge");
    }

    public function flash(): Void {
        FX.deferAction(function () {
            line.strokeWidth = 6;
            Timeline {
                keyFrames: [at (0.5s) {line.strokeWidth => 3 tween Interpolator.EASEOUT}]
            }.play();
        });
    }

    override protected function create(): Node {
        var g: Group = Group {
                    content: bind [
                        line
                    ];
                }
    }

    public override function syncObjectAdded(syncObject: ISyncObject): Void {
        flash();
    }

    public override function syncObjectChanged(syncObject: ISyncObject): Void {
        flash();
    }

    public override function syncObjectRemoved(syncObject: ISyncObject): Void {
        flash();
    }

}
