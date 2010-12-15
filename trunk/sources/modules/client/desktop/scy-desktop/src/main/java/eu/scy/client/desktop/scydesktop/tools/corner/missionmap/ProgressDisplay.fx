/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.shape.Circle;

/**
 * @author SikkenJ
 */
public class ProgressDisplay extends CustomNode {

   public var borderColor = Color.GREEN;
   public var fillColor = Color.GREEN;
   public var progress = 0.33;
   public var radius = 15.0;

   public override function create(): Node {
      Group {
         content: [
            Arc {
               centerX: 0, centerY: 0
               radiusX: radius, radiusY: radius
               startAngle: 90, length: -progress * 360
               type: ArcType.ROUND
               fill: fillColor
            }
            Circle {
               centerX: 0, centerY: 0
               radius: radius
               fill: null
               stroke: borderColor
            }
         ]
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
         content: [
            ProgressDisplay {
               layoutX: 30
               layoutY: 30
               progress: 0.4
            }
         ]
      }
   }

}
