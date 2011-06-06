/*
 * PixelRuler.fx
 *
 * Created on 19-apr-2010, 11:50:24
 */
package eu.scy.client.desktop.scydesktop.uicontrols.test.ruler;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Line;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * @author sikken
 */
public class PixelRuler extends CustomNode {

   public var minimum = 0.0;
   public var maximum = 200.0;
   public var mainTick = 10;
   public var secondTick = 2;
   public var pixelRulerOrientation = PixelRulerOrientation.TOP;
   public var mainTickLength = 10;
   public var secondTickLength = 5;
   public var color = Color.GRAY;

   public override function create(): Node {
      return Group {
            content: [
               for (x in [minimum..maximum step secondTick]) {
                  createLine(x,secondTickLength);
               }
               for (x in [minimum..maximum step mainTick]) {
                  createLine(x,mainTickLength);
               }
            ]
         };
   }

   function createLine(x:Number,length:Number):Node{
      if (pixelRulerOrientation==PixelRulerOrientation.TOP){
         Line {
            startX: x
            startY: 0
            endX: x
            endY: length
            stroke: color
         }
      }
      else if (pixelRulerOrientation==PixelRulerOrientation.RIGHT){
         Line {
            startX: 0
            startY: x
            endX: -length
            endY: x
            stroke: color
         }
      }
      else if (pixelRulerOrientation==PixelRulerOrientation.BOTTOM){
         Line {
            startX: x
            startY: 0
            endX: x
            endY: -length
            stroke: color
         }
      }
      else{
         // PixelRulerOrientation.LEFT
         Line {
            startX: 0
            startY: x
            endX: length
            endY: x
            stroke: color
         }
      }
   }


}

function run() {
   Stage {
      title: "Test PixelRuler"
      scene: Scene {
         width: 300
         height: 300
         content: [
            PixelRuler {
               layoutX: 10
               layoutY: 10
               pixelRulerOrientation:PixelRulerOrientation.TOP
            }
            PixelRuler {
               layoutX: 210
               layoutY: 10
               pixelRulerOrientation:PixelRulerOrientation.RIGHT
            }
            PixelRuler {
               layoutX: 10
               layoutY: 210
               pixelRulerOrientation:PixelRulerOrientation.BOTTOM
            }
            PixelRuler {
               layoutX: 10
               layoutY: 10
               pixelRulerOrientation:PixelRulerOrientation.LEFT
            }
         ]
      }
   }
}

