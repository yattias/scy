/*
 * CallOut.fx
 *
 * Created on 5-feb-2009, 16:39:48
 */

package eu.scy.scywindows.demos.stylebook;

import eu.scy.scywindows.demos.stylebook.CallOut;
import java.lang.System;
import javafx.ext.swing.SwingTextField;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;



/**
 * @author JoolingenWR
 */

public class CallOut extends CustomNode {
    public var topX = 80.0;
    public var topY = 10.0;
    public var width = 100.0;
    public var height = 200.0;
    public var pointX = -60.0;
    public var pointY = 180.0;
    public var callHeight = 160.0;
    public var callWidth = 20.0;
    public var fill = Color.LIGHTYELLOW;
    public var stroke = Color.DARKGRAY;
    public var innerBorderWidth = 10.0;
    def calloutLineX = 
    bind if (pointX < 0) {0;
    } else {
        width;
    }

    public override function create() {
        return Group {
            translateX: bind topX;
            translateY: bind topY;
            content: [
                Rectangle {
                    fill: fill
                    stroke: stroke
                    x: 0
                    y: 0
                    width: bind width
                    height: bind height
                    arcWidth: 10
                    arcHeight: 10
                }
                Polygon {
                    points: bind [calloutLineX, callHeight - callWidth / 2, calloutLineX + pointX, pointY, calloutLineX, callHeight + callWidth / 2]
                    stroke: null
                    fill: fill
                }
                Rectangle {
                    x: bind calloutLineX
                    y: bind callHeight - callWidth / 2
                    width: 2
                    height: callWidth
                    stroke: null
                    fill: fill
                }
                Line {
                    startX: calloutLineX
                    startY: bind callHeight - callWidth / 2
                    endX: bind calloutLineX + pointX
                    endY: bind pointY
                    stroke: stroke
                }
                Line {
                    startX: calloutLineX
                    startY: bind callHeight + callWidth / 2
                    endX: bind calloutLineX + pointX
                    endY: bind pointY
                    stroke: stroke
                }
                SwingTextField {
                    columns: 10
                    borderless: true;
                    translateX: innerBorderWidth
                    translateY: innerBorderWidth
                    width: width - 2 * innerBorderWidth
                    height: height - 2 * innerBorderWidth
                    text: "TextField"
                    editable: true

                }
            ]

        }
    }

}

function run() {
    Stage {
        title: "Callout test"
        width: 300
        height: 400
        scene: Scene {
            content: [
                CallOut {
                    var cx = 10.0;
                    var cy = 10.0;
                    topX: bind cx;
                    topY: bind cy;
                    pointX:-60
                    pointY:120
                    callWidth: 20
                    onMouseDragged: function( e: MouseEvent ):Void {
                        System.out.println("Dragging{e.x} {e.y}");
                        cx = e.x;
                        cy = e.y;
                    }
                }
            ]

        }
    }
}
