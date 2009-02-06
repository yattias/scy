/*
 * CallOut.fx
 *
 * Created on 5-feb-2009, 16:39:48
 */

package eu.scy.scywindows.demos.stylebook;

import eu.scy.scywindows.demos.stylebook.CallOut;
import javafx.ext.swing.SwingTextField;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
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
    public var chatAgent = "Yuri";
    public var iChat = true;
    var startDragX:Number;
    var startDragY:Number;

    var text_Y = innerBorderWidth + 20;

    def calloutLineX = 
    bind
    if (pointX < 0) {0;
    } else {
        width;
    }

    var typeField:SwingTextField = SwingTextField {
        columns: 10
        text: ""
        translateY: height - innerBorderWidth - 20
        translateX: innerBorderWidth
        width: width - 2 * innerBorderWidth;
        editable: true
        action: function ():Void {
            addText(typeField.text);
            typeField.text="";
        }
                }
    var grp = Group {
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
            typeField
        ]
            onMousePressed: function( e: MouseEvent ):Void {
                        startDragX = topX;
                        startDragY = topY;
                    }
                    onMouseDragged: function( e: MouseEvent ):Void {
                        topX = startDragX + e.dragX;
                        topY = startDragY + e.dragY;
                    }
        }

    public function addText(txt: String) :Void {
        var cht: String;
        if (iChat) {
            cht = "me:"
        } else {
            cht = "{chatAgent}:"
        }
        iChat = not iChat;


        insert Text {
            content: cht;
            font: Font {
                embolden: true;
            }
            x: innerBorderWidth;
            y: text_Y
            } into grp.content;

        text_Y +=10;
        insert Text { 
            content: txt;
             font: Font {
                oblique: true;
            }           x: innerBorderWidth;
            y: text_Y
            } into grp.content;
         text_Y +=13;
    }


    public override function create() {

        return grp;
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
                    var startDragX = 0.0;
                    var startDragY = 0.0;
                    topX: 10;
                    topY: 10;
                    pointX:-60
                    pointY:120
                    callWidth: 20
                }
            ]

        }
    }
}
