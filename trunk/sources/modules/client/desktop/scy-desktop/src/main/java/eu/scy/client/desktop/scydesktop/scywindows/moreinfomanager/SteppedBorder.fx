/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.moreinfomanager;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.shape.Line;

/**
 * @author SikkenJ
 */
public class SteppedBorder extends CustomNode {

   public var width = 100.0;
   public var height = 100.0;
   public var color = Color.WHITE;
   public var steps = 20;
   public var stepSize = 1.0;
   public var startOpacity = 0.25;
   public var endOpacity = 0.0;

   public override function create(): Node {
      Group {
         content: for (i in [steps..0 step -1]) {
            def useStep = i * stepSize;
            Rectangle {
               x: -useStep, y: -useStep
               width: bind width + 2 * useStep, height: bind height + 2 * useStep
               fill: bind Color.color(color.red, color.green, color.blue, calculateOpacity(i))
            }

         }
      }
   }

   function calculateOpacity(step: Integer): Number {
      startOpacity + (endOpacity - startOpacity) * step / steps;
   }

}

function run() {
   Stage {
      title: "MyApp"
      onClose: function() {
      }
      scene: Scene {
         width: 400
         height: 400
         fill: Color.YELLOW
         content: [
            Line {
               startX: 0, startY: 80
               endX: 300, endY: 80
               strokeWidth: 1
               stroke: Color.BLACK
            }
            Line {
               startX: 80, startY: 0
               endX: 80, endY: 300
               strokeWidth: 1
               stroke: Color.BLACK
            }

            SteppedBorder {
               layoutX: 40
               layoutY: 40
               color: Color.RED
            }
         ]
      }
   }

}
