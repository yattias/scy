/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.imagewindowstyler;

import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import javafx.fxd.Duplicator;
import eu.scy.client.desktop.scydesktop.art.ScyColors;
import javafx.scene.shape.Ellipse;
import javafx.scene.layout.Stack;

/**
 * @author SikkenJ
 */
public class FxdEloIcon extends EloIcon {

   public var fxdNode: Node;

   public override function create(): Node {
      scaleNode(fxdNode);
      return Stack {
            content: [
               EloIconBackground {
                  visible: bind selected
                  size: bind size
                  cornerRadius: cornerRadius
                  borderSize: borderSize
               }
               fxdNode,
               EloIconBorder {
                  visible: bind selected
                  size: bind size
                  cornerRadius: cornerRadius
                  borderSize: borderSize
                  borderColor: bind windowColorScheme.mainColor
               }
            ]
         };
   }

   public override function clone(): EloIcon {
      FxdEloIcon {
         fxdNode: Duplicator.duplicate(fxdNode)
         windowColorScheme: windowColorScheme
         selected:selected
      }
   }

}

function run() {
   def scale = 2.0;
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
                           fxdNode: Ellipse {
                              centerX: radius, centerY: radius
                              radiusX: radius, radiusY: radius/2
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

