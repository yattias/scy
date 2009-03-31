/*
 * AnchorAttribute.fx
 *
 * Created on 26-mrt-2009, 15:36:51
 */

package eu.scy.elobrowser.tool.missionmap;

import eu.scy.scywindows.ScyWindowAttribute;
import eu.scy.elobrowser.tool.missionmap.Anchor;
import eu.scy.elobrowser.tool.missionmap.AnchorDisplay;
import eu.scy.elobrowser.tool.missionmap.EloContour;
import java.lang.Object;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * @author sikkenj
 */

public class AnchorAttribute extends ScyWindowAttribute {
    public var anchorDisplay: AnchorDisplay;

    def size = 16.0;
    def bottomAngleWidth = 50.0;
    def anchorStrokeWidth = 2;
    def defaultTitleColor = Color.WHITE;
    def defaultContentColor = Color.GRAY;

    var titleColor = defaultTitleColor;
    var contentColor = defaultContentColor;
    var selected = bind anchorDisplay.selected on replace {
        setColors()
    };

    function setColors(){
        if (selected){
            titleColor = anchorDisplay.anchor.color;
            contentColor = defaultTitleColor;
        }
      else {
            titleColor = defaultTitleColor;
            contentColor = anchorDisplay.anchor.color;
        }
    }

    public override function create(): Node {
        return Group {
            cursor: Cursor.HAND;
            translateY: -size -2;
            content: [
                EloContour{
                    width: size;
                    height: size;
                    controlLength: 5;
                    borderWidth: 2;
					borderColor: bind anchorDisplay.anchor.color;
					fillColor: bind contentColor;
                }
                Line {
                    startX: size / 2,
                    startY: size / 4
                    endX: size / 2,
                    endY: 3 * size / 4
                    strokeWidth: anchorStrokeWidth
                    stroke: bind titleColor
                }
                Arc {
                    centerX: size / 2,
                    centerY: 0
                    radiusX: 2 * size / 3,
                    radiusY: 2 * size / 3
                    startAngle: 270 - bottomAngleWidth / 2,
                    length: bottomAngleWidth
                    type: ArcType.OPEN
                    fill: Color.TRANSPARENT
                    stroke: bind titleColor
                    strokeWidth: anchorStrokeWidth
                }
            ]
            onMouseClicked: function( e: MouseEvent ):Void {
                if (anchorDisplay.selectionAction != null){
                    anchorDisplay.selectionAction(anchorDisplay);
                }
            else {
                    selected = not selected;
                }
            },
        };
    }
}

function run() {

    var anchor1 = AnchorDisplay{
        anchor: Anchor{
            title: "1";
            xPos: 20;
            yPos: 20;
            color: Color.RED
        }
    }

    Stage {
        title: "AnchorAttribute"
        scene: Scene {
            width: 200
            height: 200
            content: [
                Group{
                    translateX: 20;
                    translateY: 40;
                    content: [
                        Rectangle {
                            x: 0,
                            y: 0
                            width: 100,
                            height: 20
                            fill: Color.BLACK
                        }
                        AnchorAttribute{
                            translateX: 10;
                            translateY: 0;
                            anchorDisplay: anchor1
                        }
                    ]
                }

            ]
        }
    }
}
