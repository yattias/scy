/*
 * BottomLeftCorner.fx
 *
 * Created on 30-jun-2009, 17:29:34
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

import javafx.scene.layout.VBox;
import javafx.scene.control.Button;

/**
 * @author sikkenj
 */

public class BottomLeftCorner extends Corner {

   protected override function getCornerElements():Group{
      Group{
         content:[
            Line {
               startX: 0, startY: 0
               endX: bind width-radius, endY: 0
               strokeWidth: bind strokeWidth
               stroke: bind color
            }
            Arc {
               centerX: bind width-radius, centerY: bind radius
               radiusX: bind radius, radiusY: bind radius
               startAngle: 0, length: 90
               type: ArcType.OPEN
               fill: null;
               strokeWidth: bind strokeWidth
               stroke: bind color
            }
            Line {
               startX: bind width, startY: bind radius
               endX: bind width, endY: bind height
               strokeWidth: bind strokeWidth
               stroke: bind color
            }
         ]
      }
   }

   protected override function placeInCorner(){
      translateY = scene.height-height;
   }
}


function run(){
   var bottomLeftCorner = BottomLeftCorner{
               color:Color.BLUE
               content:Rectangle {
                     x: 0, y: 0
                     width: 50, height: 20
                     fill: Color.BLACK
                  }


            }
   var bottomLeftCorner2 = BottomLeftCorner{
      color:Color.BLUE;
   }
   bottomLeftCorner2.content = Rectangle {
                     x: 0, y: 0
                     width: 50, height: 40
                     fill: Color.GRAY
                  }

   var bottomLeftCorner3 = BottomLeftCorner{
      color:Color.BLUE;
   }
   bottomLeftCorner3.content = 
      VBox {
         spacing:5;
         content:[
            Button {
               text: "One"
               action: function() {

               }
            }
            Button {
               text: "Two"
               action: function() {

               }
            }
         ]
      }

   Stage {
      title : "Test corner"
      scene: Scene {
         width: 200
         height: 200
         content: [
            bottomLeftCorner3

         ]
      }
   }


}
