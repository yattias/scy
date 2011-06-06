/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import javafx.scene.shape.Ellipse;
import javafx.scene.paint.Color;

/**
 * @author SikkenJ
 */
public class NodeTooltip extends CustomNode {

   public var content: Node;
   public var windowColorScheme: WindowColorScheme;
   def contentBorder = 3;
   def borderWidth = 2;

   public override function create(): Node {
      return Group {
                 cache: true
                 blocksMouse: true;
                 content: [
                    Rectangle {
                       x: bind content.boundsInLocal.minX - contentBorder - borderWidth
                       y: bind content.boundsInLocal.minY - contentBorder - borderWidth
                       width: bind content.boundsInLocal.width + 2 * contentBorder + 2 * borderWidth
                       height: bind content.boundsInLocal.height + 2 * contentBorder + 2 * borderWidth
                       fill: windowColorScheme.backgroundColor;
                       stroke: windowColorScheme.mainColorLight;
                       strokeWidth: borderWidth;
                    }
                    Rectangle {
                       x: bind content.boundsInLocal.minX - contentBorder
                       y: bind content.boundsInLocal.minY - contentBorder
                       width: bind content.boundsInLocal.width + 2 * contentBorder
                       height: bind content.boundsInLocal.height + 2 * contentBorder
                       fill: windowColorScheme.backgroundColor;
                       stroke: windowColorScheme.mainColor;
                       strokeWidth: borderWidth;
                    }
                    content
                 ]
              }
   }

}

function run() {

   Stage {
      title: "TextTooltip test"
      scene: Scene {
         width: 200
         height: 200
         content: [
            NodeTooltip {
               content: Ellipse {
                  centerX: 100, centerY: 100
                  radiusX: 40, radiusY: 15
                  fill: Color.GREEN
               }
               windowColorScheme: WindowColorScheme {}
               layoutX: 20
               layoutY: 20
            }
         ]
      }
   }

}


