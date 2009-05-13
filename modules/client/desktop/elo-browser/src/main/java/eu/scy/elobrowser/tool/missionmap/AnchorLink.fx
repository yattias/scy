/*
 * AnchorLink.fx
 *
 * Created on 19-mrt-2009, 10:09:44
 */

package eu.scy.elobrowser.tool.missionmap;

import eu.scy.elobrowser.tool.missionmap.Anchor;
import java.lang.Object;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import java.lang.Math;

/**
 * @author sikkenj
 */

public class AnchorLink extends CustomNode {
    public var fromAnchor: AnchorDisplay;
    public var toAnchor: AnchorDisplay;

    def strokeWidth = 1;
    def color = Color.BLACK;
    def arrowLineLength = 5;
    def arrowAngle = Math.PI / 4;
    def arrowHeight = arrowLineLength * Math.cos(arrowAngle);

    public override function create(): Node {
        var lineAngle = findLineAngle();
        var xArrowPoint = (toAnchor.xCenter + fromAnchor.xCenter) / 2;
        var yArrowPoint = (toAnchor.yCenter + fromAnchor.yCenter) / 2;
//        xArrowPoint -=
//        Math.sin(lineAngle) * arrowHeight;
//        yArrowPoint +=
//        Math.cos(lineAngle) * arrowHeight;
        var angle1 = lineAngle - arrowAngle;
        var x1 = Math.sin(angle1) * arrowLineLength;
        var y1 = Math.cos(angle1) * arrowLineLength;
        var angle2 = lineAngle + arrowAngle;
        var x2 = Math.sin(angle2) * arrowLineLength;
        var y2 = Math.cos(angle2) * arrowLineLength;
		  xArrowPoint += (x2-x1)/2;
		  yArrowPoint -= (y2-y1)/2;
        //      println("to:{toAnchor.anchor.title}, lineAngle:{lineAngle}, angle1:{angle1}, x1:{x1}, y1:{y1}, angle2:{angle2}, x2:{x2}, y2:{y2}");
        return Group {
            content: [
                Line {
                    startX: bind fromAnchor.xCenter,
                    startY: bind fromAnchor.yCenter,
                    endX: bind toAnchor.xCenter,
                    endY: bind toAnchor.yCenter,
                    strokeWidth: strokeWidth
                    stroke: color
                },
                Line {
                    startX: xArrowPoint,
                    startY: yArrowPoint,
                    endX: xArrowPoint + x1,
                    endY: yArrowPoint - y1,
                    strokeWidth: strokeWidth
                    stroke: color
                },
                Line {
                    startX: xArrowPoint,
                    startY: yArrowPoint,
                    endX: xArrowPoint - x2,
                    endY: yArrowPoint + y2,
                    strokeWidth: strokeWidth
                    stroke: color
                },
            ]
        };
    }

    function findLineAngle(){
        var deltaX = toAnchor.xCenter - fromAnchor.xCenter;
        var deltaY = toAnchor.yCenter - fromAnchor.yCenter;
        return Math.atan2(deltaY , deltaX);
    }


}

    function run(){

        var anchor0 = AnchorDisplay{
            anchor: Anchor{
                title: "0";
                xPos: 60;
                yPos: 60;
            }
        }
        var anchor1 = AnchorDisplay{
            anchor: Anchor{
                title: "1";
                xPos: 20;
                yPos: 20;
            }
        }
        var anchor2 = AnchorDisplay{
            anchor: Anchor{
                title: "2";
                xPos: 60;
                yPos: 20;
            }
        }
        var anchor3 = AnchorDisplay{
            anchor: Anchor{
                title: "3";
                xPos: 100;
                yPos: 20;
            }
        }
        var anchor4 = AnchorDisplay{
            anchor: Anchor{
                title: "4";
                xPos: 100;
                yPos: 60;
            }
        }
        var anchor5 = AnchorDisplay{
            anchor: Anchor{
                title: "5";
                xPos: 100;
                yPos: 100;
            }
        }
        var anchor6 = AnchorDisplay{
            anchor: Anchor{
                title: "6";
                xPos: 60;
                yPos: 100;
            }
        }
        var anchor7 = AnchorDisplay{
            anchor: Anchor{
                title: "7";
                xPos: 20;
                yPos: 100;
            }
        }
        var anchor8 = AnchorDisplay{
            anchor: Anchor{
                title: "8";
                xPos: 20;
                yPos: 60;
            }
        }
        var anchorLink01 = AnchorLink{
            fromAnchor: anchor0
            toAnchor: anchor1
        }
        var anchorLink02 = AnchorLink{
            fromAnchor: anchor0
            toAnchor: anchor2
        }
        var anchorLink03 = AnchorLink{
            fromAnchor: anchor0
            toAnchor: anchor3
        }
        var anchorLink04 = AnchorLink{
            fromAnchor: anchor0
            toAnchor: anchor4
        }
        var anchorLink05 = AnchorLink{
            fromAnchor: anchor0
            toAnchor: anchor5
        }
        var anchorLink06 = AnchorLink{
            fromAnchor: anchor0
            toAnchor: anchor6
        }
        var anchorLink07 = AnchorLink{
            fromAnchor: anchor0
            toAnchor: anchor7
        }
        var anchorLink08 = AnchorLink{
            fromAnchor: anchor0
            toAnchor: anchor8
        }

        Stage {
            title: "Anchor test"
            scene: Scene {
                width: 200
                height: 200
                content: [
                    anchorLink01,
                    anchorLink02,
                    anchorLink03,
                    anchorLink04,
                    anchorLink05,
                    anchorLink06,
                    anchorLink07,
                    anchorLink08,
                    anchor0,
                    anchor1,
                    anchor2,
                    anchor3,
                    anchor4,
                    anchor5
                    anchor6
                    anchor7
                    anchor8
                ]
            }
        }
    }
