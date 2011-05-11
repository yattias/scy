/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.desktoputils.art.javafx;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * @author SikkenJ
 */
public class MoreResourcesTypeIcon extends CustomNode {

   def scale = 2.0;

   public override function create(): Node {
      Group {
         scaleX: scale
         scaleY: scale
         translateX: 30
         translateY: 12
         rotate: -10
         content: [
            Group {
               clip: Polygon {
                  points: [0.00, 18.00, 18.00, 18.00, 18.00, 0.00, 0.00, 0.00]
                  fill: null
                  stroke: null
               }
               content: [
                  Group {
                     clip: Polygon {
                        points: [0.00, 0.00, 18.00, 0.00, 18.00, 18.00, 0.00, 18.00]
                        fill: null
                        stroke: null
                     }
                     content: [
                        Group {
                           opacity: 0.58
                           content: [
                              Group {
                                 clip: Polygon {
                                    points: [5.62, 17.73, 12.27, 17.73, 12.27, 3.32, 5.62, 3.32]
                                    fill: null
                                    stroke: null
                                 }
                                 content: [
                                    SVGPath {
                                       fill: Color.WHITE
                                       stroke: null
                                       content: "M12.27,14.81 C12.27,16.42 11.05,17.73 9.53,17.73 L8.36,17.73 C6.84,17.73 5.62,16.42 5.62,14.81 L5.62,6.24 C5.62,4.63 6.84,3.32 8.36,3.32 L9.53,3.32 C11.05,3.32 12.27,4.63 12.27,6.24 Z "
                                    },]
                              },]
                        },
                        SVGPath {
                           fill: null
                           stroke: Color.rgb(0x26, 0x30, 0x66)
                           strokeLineCap: StrokeLineCap.BUTT
                           content: "M8.41,3.34 L9.59,3.34 C11.10,3.34 12.33,4.65 12.33,6.26 L12.33,14.83 C12.33,16.44 11.10,17.75 9.59,17.75 L8.41,17.75 C6.90,17.75 5.67,16.44 5.67,14.83 L5.67,6.26 C5.67,5.25 6.15,4.36 6.88,3.84 "
                        },
                        SVGPath {
                           fill: Color.WHITE
                           stroke: null
                           content: "M9.87,6.18 C9.87,6.77 9.40,7.24 8.81,7.24 C8.23,7.24 7.75,6.77 7.75,6.18 C7.75,5.60 8.23,5.12 8.81,5.12 C9.40,5.12 9.87,5.60 9.87,6.18 "
                        },
                        SVGPath {
                           fill: null
                           stroke: Color.rgb(0x26, 0x30, 0x66)
                           strokeWidth: 1.06
                           strokeLineCap: StrokeLineCap.BUTT
                           content: "M9.87,6.18 C9.87,6.77 9.40,7.24 8.81,7.24 C8.23,7.24 7.75,6.77 7.75,6.18 C7.75,5.60 8.23,5.12 8.81,5.12 C9.40,5.12 9.87,5.60 9.87,6.18 Z "
                        },
                        SVGPath {
                           fill: null
                           stroke: Color.rgb(0xea, 0x5b, 0xb)
                           strokeWidth: 0.98
                           strokeLineCap: StrokeLineCap.BUTT
                           strokeMiterLimit: 8.0
                           content: "M10.12,0.55 C9.39,0.25 8.42,1.10 7.97,2.46 C7.86,2.78 7.79,3.09 7.75,3.39 "
                        },
                        SVGPath {
                           fill: null
                           stroke: Color.rgb(0xea, 0x5b, 0xb)
                           strokeWidth: 0.98
                           strokeLineCap: StrokeLineCap.BUTT
                           strokeMiterLimit: 8.0
                           content: "M9.34,5.38 C9.83,5.08 10.32,4.43 10.61,3.58 C11.07,2.22 10.85,0.86 10.12,0.55 "
                        },
                        SVGPath {
                           fill: null
                           stroke: Color.rgb(0xea, 0x5b, 0xb)
                           strokeWidth: 0.98
                           strokeLineCap: StrokeLineCap.BUTT
                           strokeMiterLimit: 8.0
                           content: "M8.48,5.48 C8.76,5.59 9.06,5.55 9.36,5.37 "
                        },
                        SVGPath {
                           fill: null
                           stroke: Color.rgb(0x26, 0x30, 0x66)
                           strokeLineCap: StrokeLineCap.BUTT
                           content: "M6.88,3.84 C7.32,3.53 7.84,3.34 8.41,3.34 "
                        },]
                  },]
            },]
      }

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
                  MoreResourcesTypeIcon {}
               ]
            }
            Group {
               layoutX: 120
               layoutY: 80
               content: [
                  MissionMapWindowIcon {}
                  MoreResourcesTypeIcon {}
               ]
            }
         ]
      }
   }

}
