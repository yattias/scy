/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.desktoputils.art.javafx;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author SikkenJ
 */
public class InstructionTypesIcon extends CustomNode {

   def scale = 1.25;

   public override function create(): Node {
      Group {
         scaleX: scale
         scaleY: scale
         translateX: 37
         translateY: 0
         rotate: 0
         content: [
            Group {
               clip: Polygon {
                  points: [0.00, 15.39, 18.07, 15.39, 18.07, -0.00, 0.00, -0.00]
                  fill: null
                  stroke: null
               }
               content: [
                  Group {
                     clip: Polygon {
                        points: [0.00, -0.00, 18.07, -0.00, 18.07, 15.39, 0.00, 15.39]
                        fill: null
                        stroke: null
                     }
                     content: [
                        Group {
                           opacity: 0.5
                           content: [
                              Group {
                                 clip: Polygon {
                                    points: [0.89, 13.65, 17.44, 13.65, 17.44, 0.75, 0.89, 0.75]
                                    fill: null
                                    stroke: null
                                 }
                                 content: [
                                    SVGPath {
                                       fill: Color.WHITE
                                       stroke: null
                                       content: "M5.70,11.23 L1.29,13.65 L2.31,9.47 C1.46,8.61 0.94,7.58 0.89,6.48 C0.75,3.37 4.33,0.80 8.90,0.75 C13.47,0.70 17.29,3.18 17.43,6.30 C17.57,9.23 14.38,11.68 10.19,11.98 Z "
                                    },]
                              },]
                        },
                        SVGPath {
                           fill: null
                           stroke: Color.rgb(0x26, 0x2f, 0x66)
                           strokeWidth: 0.97
                           strokeLineCap: StrokeLineCap.BUTT
                           content: "M5.70,11.23 L1.04,14.23 L2.31,9.47 C1.46,8.61 0.94,7.58 0.89,6.48 C0.75,3.37 4.33,0.80 8.90,0.75 C13.47,0.70 17.29,3.18 17.43,6.30 C17.57,9.23 13.59,12.07 10.19,11.98 Q6.79,11.90 5.70,11.23 Z "
                        },
                        Line {
                           fill: null
                           stroke: Color.rgb(0x26, 0x2f, 0x66)
                           strokeWidth: 0.5
                           strokeLineCap: StrokeLineCap.BUTT
                           startX: 5.87
                           startY: 3.82
                           endX: 12.45
                           endY: 3.82
                        },
                        Line {
                           fill: null
                           stroke: Color.rgb(0x26, 0x2f, 0x66)
                           strokeWidth: 0.5
                           strokeLineCap: StrokeLineCap.BUTT
                           startX: 4.04
                           startY: 5.98
                           endX: 15.2
                           endY: 5.98
                        },
                        Line {
                           fill: null
                           stroke: Color.rgb(0x26, 0x2f, 0x66)
                           strokeWidth: 0.5
                           strokeLineCap: StrokeLineCap.BUTT
                           startX: 4.12
                           startY: 8.73
                           endX: 14.29
                           endY: 8.73
                        },]
                  },]
            },] }
   }

}

function run() {
   def scale = 2.0;
   Stage {
      title: "MyApp"
      onClose: function() {
      }
      scene: Scene {
         width: 200
         height: 200
         content: [
            Group {
               scaleX: scale
               scaleY: scale
               layoutX: 40
               layoutY: 40
               content: [
                  MissionMapWindowIcon {}
                  InstructionTypesIcon {}
               ]
            }
            Group {
               layoutX: 120
               layoutY: 80
               content: [
                  MissionMapWindowIcon {}
                  InstructionTypesIcon {}
               ]
            }
         ]
      }
   }

}
