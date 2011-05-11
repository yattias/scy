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
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @author SikkenJ
 */
public class MoreAssignmentTypeIcon extends CustomNode {

   def scale = 2.0;
   def color = Color.rgb(0x26, 0x30, 0x66);
//   def color = Color.RED;

   public override function create(): Node {
      Group {
         scaleX: scale
         scaleY: scale
         translateX: 19
         translateY: 3
         rotate: 40
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
                           opacity: 0.5
                           content: [
                              Group {
                                 clip: Polygon {
                                    points: [6.53, 17.30, 12.57, 17.30, 12.57, 0.33, 6.53, 0.33]
                                    fill: null
                                    stroke: null
                                 }
                                 content: [
                                    SVGPath {
                                       fill: Color.WHITE
                                       stroke: null
                                       content: "M12.57,7.63 L12.57,15.38 C12.57,16.44 11.58,17.30 10.35,17.30 L8.75,17.30 C7.52,17.30 6.53,16.44 6.53,15.38 L6.53,2.25 C6.53,1.19 7.52,0.33 8.75,0.33 L10.35,0.33 C11.58,0.33 12.57,1.19 12.57,2.25 L12.57,2.64 "
                                    },]
                              },]
                        },
                        SVGPath {
                           fill: null
                           stroke: color
                           strokeLineCap: StrokeLineCap.ROUND
                           content: "M12.57,7.91 L12.57,15.66 C12.57,16.72 11.58,17.58 10.35,17.58 L8.75,17.58 C7.52,17.58 6.53,16.72 6.53,15.66 L6.53,2.52 C6.53,1.47 7.52,0.60 8.75,0.60 L10.35,0.60 C11.58,0.60 12.57,1.47 12.57,2.52 L12.57,2.92 "
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
                  MoreAssignmentTypeIcon {}
               ]
            }
            Group {
               layoutX: 120
               layoutY: 80
               content: [
                  MissionMapWindowIcon {}
                  MoreAssignmentTypeIcon {}
               ]
            }
         ]
      }
   }

}
