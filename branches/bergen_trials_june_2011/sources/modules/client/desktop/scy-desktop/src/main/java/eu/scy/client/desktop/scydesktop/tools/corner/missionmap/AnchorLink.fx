/*
 * AnchorLink.fx
 *
 * Created on 19-mrt-2009, 10:09:44
 */
package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import java.lang.Object;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import java.lang.Math;

/**
 * @author sikkenj
 */
public class AnchorLink extends CustomNode {

   public var fromAnchor: AnchorDisplay;
   public var toAnchor: AnchorDisplay;
   public var bidirectional = false;
   def strokeWidth = 1;
   def color = Color.BLACK;
   def arrowLineLength = 5;
   def arrowAngle = Math.PI / 4;
   def arrowHeight = arrowLineLength * Math.cos(arrowAngle);

   public override function create(): Node {
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
//               if (bidirectional){
//                  [
//                     createArrow(arrowHeight, false),
//                     createArrow(arrowHeight, true)
//                  ]
//               }
//               else{
//                  createArrow(0.0, false)
//               }
            ]
         };
   }

   function findLineAngle() {
      var deltaX = toAnchor.xCenter - fromAnchor.xCenter;
      var deltaY = toAnchor.yCenter - fromAnchor.yCenter;
      return Math.atan2(deltaY, deltaX);
   }

   function createArrow(arrowShift: Number, backwards: Boolean): Node[] {
      var lineAngle = findLineAngle();
      if (backwards){
         lineAngle += Math.PI;
      }

      var angle1 = lineAngle - arrowAngle;
      var x1 = Math.sin(angle1) * arrowLineLength;
      var y1 = Math.cos(angle1) * arrowLineLength;
      var angle2 = lineAngle + arrowAngle;
      var x2 = Math.sin(angle2) * arrowLineLength;
      var y2 = Math.cos(angle2) * arrowLineLength;
      var xArrowPoint = bind (toAnchor.xCenter + fromAnchor.xCenter) / 2 + (x2 - x1) / 2 + Math.cos(lineAngle)*arrowShift;
      var yArrowPoint = bind (toAnchor.yCenter + fromAnchor.yCenter) / 2 - (y2 - y1) / 2 + Math.sin(lineAngle)*arrowShift;
      [
         Line {
            startX: bind xArrowPoint,
            startY: bind yArrowPoint,
            endX: bind xArrowPoint + x1,
            endY: bind yArrowPoint - y1,
            strokeWidth: strokeWidth
            stroke: color
         },
         Line {
            startX: bind xArrowPoint,
            startY: bind yArrowPoint,
            endX: bind xArrowPoint - x2,
            endY: bind yArrowPoint + y2,
            strokeWidth: strokeWidth
            stroke: color
         },
      ]
   }

}

function run() {
   //
   //        var anchor0 = AnchorDisplay{
   //            anchor: MissionAnchorFX{
   //                iconCharacter: "0";
   //                xPos: 60;
   //                yPos: 60;
   //            }
   //        }
   //        var anchor1 = AnchorDisplay{
   //            anchor: MissionAnchorFX{
   //                iconCharacter: "1";
   //                xPos: 20;
   //                yPos: 20;
   //            }
   //        }
   //        var anchor2 = AnchorDisplay{
   //            anchor: MissionAnchorFX{
   //                iconCharacter: "2";
   //                xPos: 60;
   //                yPos: 20;
   //            }
   //        }
   //        var anchor3 = AnchorDisplay{
   //            anchor: MissionAnchorFX{
   //                iconCharacter: "3";
   //                xPos: 100;
   //                yPos: 20;
   //            }
   //        }
   //        var anchor4 = AnchorDisplay{
   //            anchor: MissionAnchorFX{
   //                iconCharacter: "4";
   //                xPos: 100;
   //                yPos: 60;
   //            }
   //        }
   //        var anchor5 = AnchorDisplay{
   //            anchor: MissionAnchorFX{
   //                iconCharacter: "5";
   //                xPos: 100;
   //                yPos: 100;
   //            }
   //        }
   //        var anchor6 = AnchorDisplay{
   //            anchor: MissionAnchorFX{
   //                iconCharacter: "6";
   //                xPos: 60;
   //                yPos: 100;
   //            }
   //        }
   //        var anchor7 = AnchorDisplay{
   //            anchor: MissionAnchorFX{
   //                iconCharacter: "7";
   //                xPos: 20;
   //                yPos: 100;
   //            }
   //        }
   //        var anchor8 = AnchorDisplay{
   //            anchor: MissionAnchorFX{
   //                iconCharacter: "8";
   //                xPos: 20;
   //                yPos: 60;
   //            }
   //        }
   //        var anchorLink01 = AnchorLink{
   //            fromAnchor: anchor0
   //            toAnchor: anchor1
   //        }
   //        var anchorLink02 = AnchorLink{
   //            fromAnchor: anchor0
   //            toAnchor: anchor2
   //        }
   //        var anchorLink03 = AnchorLink{
   //            fromAnchor: anchor0
   //            toAnchor: anchor3
   //        }
   //        var anchorLink04 = AnchorLink{
   //            fromAnchor: anchor0
   //            toAnchor: anchor4
   //        }
   //        var anchorLink05 = AnchorLink{
   //            fromAnchor: anchor0
   //            toAnchor: anchor5
   //        }
   //        var anchorLink06 = AnchorLink{
   //            fromAnchor: anchor0
   //            toAnchor: anchor6
   //        }
   //        var anchorLink07 = AnchorLink{
   //            fromAnchor: anchor0
   //            toAnchor: anchor7
   //        }
   //        var anchorLink08 = AnchorLink{
   //            fromAnchor: anchor0
   //            toAnchor: anchor8
   //        }
   //
   //        Stage {
   //            title: "Anchor test"
   //            scene: Scene {
   //                width: 200
   //                height: 200
   //                content: [
   //                    anchorLink01,
   //                    anchorLink02,
   //                    anchorLink03,
   //                    anchorLink04,
   //                    anchorLink05,
   //                    anchorLink06,
   //                    anchorLink07,
   //                    anchorLink08,
   //                    anchor0,
   //                    anchor1,
   //                    anchor2,
   //                    anchor3,
   //                    anchor4,
   //                    anchor5,
   //                    anchor6,
   //                    anchor7,
   //                    anchor8
   //                ]
   //            }
   //        }
}
