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
    public var topx = 80.0;
    public var topy = 10.0;
    public var width = 100.0;
    public var height = 200.0;
    public var pointX = -60.0;
    public var pointY = 180.0;
    public var callHeight = 160.0;
    public var callWidth = 20.0;
    public var fill = Color.LIGHTYELLOW;
    public var stroke = Color.DARKGRAY;
    public var innerBorderWidth = 10.0;

    public override function create() {
        return Group {
            content: [
                Rectangle {
                    fill: fill
                    stroke: stroke
                    x: bind topx
                    y: bind topy
                    width: bind width
                    height: bind height
                    arcWidth: 10
                    arcHeight: 10
                    onMouseDragged: function( e: MouseEvent ):Void {
                        System.out.println("Dragging");
                        topx = e.x;
                        topy = e.y;
                    }
                }
                Polygon {
                    points: bind [topx, topy+callHeight - callWidth / 2, topx + pointX, topy + pointY, topx, topy+callHeight + callWidth / 2]
                    stroke: null
                    fill: fill
                }
                Rectangle {
                    x: bind topx
                    y: bind topy+callHeight - callWidth / 2
                    width: 2
                    height: callWidth
                    stroke: null
                    fill: fill
                }
                Line {
                    startX: bind topx
                    startY: bind topy+callHeight - callWidth / 2
                    endX: bind topx + pointX
                    endY: bind topy + pointY
                    stroke: stroke
                }
                Line {
                    startX: bind topx
                    startY: bind topy+callHeight + callWidth / 2
                    endX: bind topx + pointX
                    endY: bind topy + pointY
                    stroke: stroke
                }
                SwingTextField {
                    columns: 10
                    borderless: true;
                    translateX: bind topx + innerBorderWidth
                    translateY: bind topy + innerBorderWidth
                    width: width - 2*innerBorderWidth
                    height: height - 2*innerBorderWidth
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
                }
            ]

        }
    }
}
