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
import javafx.scene.shape.Polyline;
import javafx.scene.shape.SVGPath;
import javafx.scene.shape.StrokeLineCap;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * @author SikkenJ
 */
public class MissionMapButtonIcon extends CustomNode {

   var mouseOverColor = Color.WHITE;
   public var mouseOver = false on replace {
         mouseOverColor = if (mouseOver) Color.rgb(0x1d, 0x1d, 0x1b, .75) else Color.WHITE
      };

   public override function create(): Node {
      Group {
         content: [
            Group {
               clip: Polygon {
                  points: [0.00, 26.00, 26.00, 26.00, 26.00, 0.00, 0.00, 0.00]
                  fill: null
                  stroke: null
               }
               content: [
                  //                  Group {
                  //                     clip: Polygon {
                  //                        points: [0.00, 0.00, 26.00, 0.00, 26.00, 26.00, 0.00, 26.00]
                  //                        fill: null
                  //                        stroke: null
                  //                     }
                  //                     content: [
                  //                        Group {
                  //                           opacity: 0.5
                  //                           content: [
                  //                              Group {
                  //                                 clip: Polygon {
                  //                                    points: [0.00, 26.00, 26.00, 26.00, 26.00, 0.00, 0.00, 0.00]
                  //                                    fill: null
                  //                                    stroke: null
                  //                                 }
                  //                                 content: [
                  //                                    Polygon {
                  //                                       points: [26.00, 26.00, 0.00, 26.00, 0.00, 0.00, 26.00, 0.00]
                  //                                       fill: Color.WHITE
                  //                                       stroke: null
                  //                                    },]
                  //                              },]
                  //                        },]
                  //                  },
                  Polyline {
                     fill: null
                     stroke: Color.rgb(0x1d, 0x1d, 0x1b)
                     strokeLineCap: StrokeLineCap.BUTT
                     points: [21.53, 4.69, 6.31, 13.42, 21.53, 21.89]
                  },
                  Polygon {
                     points: [0.00, 19.85, 12.74, 19.85, 12.74, 7.00, 0.00, 7.00]
                     fill: Color.rgb(0x25, 0x31, 0x65)
                     stroke: null
                  },
                  Polygon {
                     points: [17.56, 0.50, 25.50, 0.50, 25.50, 8.88, 17.56, 8.88]
                     fill: bind mouseOverColor
                     stroke: null
                  },
                  SVGPath {
                     fill: Color.rgb(0x4e, 0x76, 0xba)
                     stroke: null
                     content: "M25.00,8.38 L18.06,8.38 L18.06,1.00 L25.00,1.00 Z M26.00,0.00 L17.06,0.00 L17.06,9.38 L26.00,9.38 Z "
                  },
                  Polygon {
                     points: [17.56, 17.12, 25.50, 17.12, 25.50, 25.50, 17.56, 25.50]
                     fill: bind mouseOverColor
                     stroke: null
                  },
                  SVGPath {
                     fill: Color.rgb(0x4e, 0x76, 0xba)
                     stroke: null
                     content: "M18.06,17.62 L25.00,17.62 L25.00,25.00 L18.06,25.00 Z M17.06,26.00 L26.00,26.00 L26.00,16.62 L17.06,16.62 Z "
                  },]
            },]
      }
   }

}

function run() {
   Stage {
      title: "MyApp"
      onClose: function() {
      }
      scene: Scene {
         width: 200
         height: 200
         fill: Color.YELLOW
         content: [
            MissionMapButtonIcon {
               layoutX: 20
               layoutY: 20
            }
            MissionMapButtonIcon {
               layoutX: 60
               layoutY: 20
               mouseOver: true
            }
         ]
      }
   }

}
