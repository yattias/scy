package eu.scy.client.desktop.scydesktop.edges;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.Group;
import javafx.scene.shape.Line;
import javafx.scene.paint.*;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
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
    public-init var scyWindowStart: ScyWindow;
    public-init var scyWindowEnd: ScyWindow;
    var session: ISyncSession;
    def logger = Logger.getLogger(this.getClass());
    public-read def line: Line = Line {
                startX: scyWindowStart.layoutX + (scyWindowStart.width/2);
                startY: scyWindowStart.layoutY;
                endX: scyWindowEnd.layoutX + (scyWindowEnd.width/2);
                endY: scyWindowEnd.layoutY;
                strokeWidth: 3.0;
//                stroke: Color.BLACK;
                stroke: LinearGradient {
                    startX: 0.0, startY: 0.0, endX: 1.0, endY: 0.0
                    proportional: true
                    stops: [Stop { offset: 0.0 color: scyWindowStart.windowColorScheme.mainColor },
                        Stop { offset: 1.0 color: scyWindowEnd.windowColorScheme.mainColor }]
                }
            }

    var startWatchX = bind scyWindowStart.layoutX on replace {
                update()
            };

    var startWatchY = bind scyWindowStart.layoutY on replace {
                update()
            };
   
    var endWatchX = bind scyWindowEnd.layoutX on replace {
                update()
            };

    var endWatchY = bind scyWindowEnd.layoutY on replace {
                update()
            };

    public function update(): Void {
	line.startX = scyWindowStart.layoutX + (scyWindowStart.width/2);
        line.startY = scyWindowStart.layoutY;
        line.endX = scyWindowEnd.layoutX + (scyWindowEnd.width/2);
        line.endY = scyWindowEnd.layoutY;
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
