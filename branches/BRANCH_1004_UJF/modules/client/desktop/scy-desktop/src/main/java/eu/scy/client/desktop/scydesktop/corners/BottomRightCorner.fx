/*
 * BottomRightCorner.fx
 *
 * Created on 30-jun-2009, 17:33:32
 */

package eu.scy.client.desktop.scydesktop.corners;

import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

/**
 * @author sikkenj
 */

public class BottomRightCorner extends Corner {

   protected override function resizeContent(){

   }

   init{
           background.content=[
              Rectangle {
               x: bind radius, y: 0;
               width: bind width-radius, height: bind radius
               stroke:bind backgroundColor
               strokeWidth:1
               fill: bind backgroundColor
            }
            Arc {
               centerX: bind radius, centerY: bind radius
               radiusX: bind radius, radiusY: bind radius
               startAngle: 90, length: 90
               type: ArcType.ROUND
               fill: bind backgroundColor
               stroke:bind backgroundColor
               strokeWidth:0
            }
            Rectangle {
               x: 0, y: bind radius
               width: bind width, height: bind height-radius
               stroke:bind backgroundColor
               strokeWidth:1
               fill: bind backgroundColor;
            }
              ];
    border.content=[
            Line {
               startX: 0, startY: bind height
               endX: 0, endY: bind radius
               strokeWidth: bind strokeWidth
               stroke: bind color
            }
            Arc {
               centerX: bind radius, centerY: bind radius
               radiusX: bind radius, radiusY: bind radius
               startAngle: 90, length: 90
               type: ArcType.OPEN
               fill: Color.TRANSPARENT;
               strokeWidth: bind strokeWidth
               stroke: bind color
            }
            Line {
               startX: bind radius, startY: 0
               endX: bind width, endY: 0
               strokeWidth: bind strokeWidth
               stroke: bind color
            }
            ]
   }

   protected override function placeInCorner(){
      translateX = scene.width-width;
      translateY = scene.height-height;
   }
}


function run(){
   Stage {
      title : "Test corner"
      scene: Scene {
         width: 200
         height: 200
         content: [
            BottomRightCorner{
               color:Color.BLUE
               content:Rectangle {
                     x: 0, y: 0
                     width: 50, height: 20
                     fill: Color.BLACK
                  }


            }

         ]
      }
   }


}
