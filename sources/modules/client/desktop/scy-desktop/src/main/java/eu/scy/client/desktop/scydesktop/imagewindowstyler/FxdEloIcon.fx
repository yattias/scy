/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.imagewindowstyler;

import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import javafx.fxd.Duplicator;
import eu.scy.client.desktop.scydesktop.art.ScyColors;

/**
 * @author SikkenJ
 */
public class FxdEloIcon extends EloIcon {

   public var fxdNode: Node;
   public var windowColorScheme: WindowColorScheme;
   def borderSize = 2.0;
   def backgroundColor = Color.WHITE;
   def cornerRadius = size / 16.0 * 2.5;
   def borderColor = bind if (selected) windowColorScheme.mainColor else Color.WHITE;

   public override function create(): Node {
      def scale = calculateScale(fxdNode.layoutBounds);
            println("fxdNode.layoutBounds: {fxdNode.layoutBounds}, scale: {scale}");
      //      println("fxdNode.boundsInParent: {fxdNode.boundsInParent}");
      fxdNode.scaleX = scale;
      fxdNode.scaleY = scale;
      fxdNode.layoutX = -fxdNode.layoutBounds.minX + (scale - 1) * fxdNode.layoutBounds.width / 2;
      fxdNode.layoutY = -fxdNode.layoutBounds.minY + (scale - 1) * fxdNode.layoutBounds.height / 2;
      //      println("fxdNode.boundsInParent: {fxdNode.boundsInParent}");
      //      println("");
      return Group {
            content: [
               Group {
                  visible: bind selected
                  content: [
                     Rectangle {
                        x: borderSize / 2, y: borderSize / 2
                        width: size - borderSize, height: size - cornerRadius - borderSize
                        fill: backgroundColor
                     }
                     Rectangle {
                        x: cornerRadius + borderSize / 2, y: size - cornerRadius - borderSize / 2
                        width: size - cornerRadius - borderSize, height: cornerRadius
                        fill: backgroundColor
                     }
                     Arc {
                        centerX: cornerRadius + borderSize / 2, centerY: size - cornerRadius - borderSize / 2
                        radiusX: cornerRadius, radiusY: cornerRadius
                        startAngle: 180, length: 90
                        type: ArcType.ROUND
                        fill: backgroundColor
                     }
                  ]
               }
               fxdNode,
               Group {
                  //                  visible: bind selected
                  content: [
                     Line {
                        startX: 0, startY: 0
                        endX: size, endY: 0
                        strokeWidth: borderSize
                        stroke: bind borderColor
                     }
                     Line {
                        startX: size, startY: 0
                        endX: size, endY: size
                        strokeWidth: borderSize
                        stroke: bind borderColor
                     }
                     Line {
                        startX: cornerRadius + borderSize, startY: size
                        endX: size, endY: size
                        strokeWidth: borderSize
                        stroke: bind borderColor
                     }
                     Line {
                        startX: 0, startY: 0
                        endX: 0, endY: size - cornerRadius - borderSize
                        strokeWidth: borderSize
                        stroke: bind borderColor
                     }
                     Arc {
                        centerX: cornerRadius, centerY: size - cornerRadius
                        radiusX: cornerRadius, radiusY: cornerRadius
                        startAngle: 180, length: 90
                        type: ArcType.OPEN
                        fill: null
                        strokeWidth: borderSize
                        stroke: bind borderColor
                     }
                  ]
               }

            ]
         };
   }

   public override function clone(): EloIcon {
      FxdEloIcon {
         fxdNode: Duplicator.duplicate(fxdNode);
         windowColorScheme: windowColorScheme
      }
   }

}

function run() {
   def scale = 4.0;
   def circleSize = 7.0;
   def windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.pink);
   Stage {
      title: "test FxdEloIcon"
      onClose: function() {
      }
      scene: Scene {
         width: 400
         height: 400
         content: [
            VBox {
               layoutX: 150
               layoutY: 50
               scaleX: scale
               scaleY: scale
               spacing: 10
               content: [
                  HBox {
                     spacing: 10
                     content: [
                        FxdEloIcon {
                           def radius = 0.5 * circleSize
                           windowColorScheme: windowColorScheme
                           selected: true
                           fxdNode: Circle {
                              centerX: radius, centerY: radius
                              radius: radius
                              fill: Color.GREEN
                           }
                        }
                        FxdEloIcon {
                           def radius = 1.0 * circleSize
                           selected: true
                           windowColorScheme: windowColorScheme
                           fxdNode: Circle {
                              centerX: radius, centerY: radius
                              radius: radius
                              fill: Color.GREEN
                           }
                        }
                        FxdEloIcon {
                           def radius = 2.0 * circleSize
                           selected: true
                           windowColorScheme: windowColorScheme
                           fxdNode: Circle {
                              centerX: radius, centerY: radius
                              radius: radius
                              fill: Color.GREEN
                           }
                        }
                     ]
                  }
                  HBox {
                     spacing: 10
                     content: [
                        FxdEloIcon {
                           def radius = 0.5 * circleSize
                           selected: false
                           windowColorScheme: windowColorScheme
                           fxdNode: Circle {
                              centerX: radius, centerY: radius
                              radius: radius
                              fill: Color.GREEN
                           }
                        }
                        FxdEloIcon {
                           def radius = 1.0 * circleSize
                           selected: false
                           windowColorScheme: windowColorScheme
                           fxdNode: Circle {
                              centerX: radius, centerY: radius
                              radius: radius
                              fill: Color.GREEN
                           }
                        }
                        FxdEloIcon {
                           def radius = 2.0 * circleSize
                           selected: false
                           windowColorScheme: windowColorScheme
                           fxdNode: Circle {
                              centerX: radius, centerY: radius
                              radius: radius
                              fill: Color.GREEN
                           }
                        }
                     ]
                  }
               ]
            }
         ]
      }
   }

}

