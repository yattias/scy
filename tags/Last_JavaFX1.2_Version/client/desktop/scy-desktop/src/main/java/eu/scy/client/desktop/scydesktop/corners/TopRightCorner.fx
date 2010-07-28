/*
 * TopRightCorner.fx
 *
 * Created on 30-jun-2009, 17:17:09
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

public class TopRightCorner extends Corner {

    init{
        border.content=[
                            Line {
               startX: 0, startY: 0
               endX: 0, endY: bind height-radius
               strokeWidth: bind strokeWidth
               stroke: bind color
            }
            Arc {
               centerX: bind radius, centerY: bind height-radius
               radiusX: bind radius, radiusY: bind radius
               startAngle: 180, length: 90
               type: ArcType.OPEN
               fill: null;
               strokeWidth: bind strokeWidth
               stroke: bind color
            }
            Line {
               startX: bind radius, startY: bind height
               endX: bind width, endY: bind height
               strokeWidth: bind strokeWidth
               stroke: bind color
            }
];
      background.content=[
              Rectangle {
               x: 0, y: 0;
               width: bind width, height: bind height-radius
               stroke:bind backgroundColor
               strokeWidth:1
               fill: bind backgroundColor;
            }
            Arc {
               centerX: bind radius, centerY: bind height-radius
               radiusX: bind radius, radiusY: bind radius
               startAngle: 180, length: 90
               type: ArcType.ROUND
               stroke:bind backgroundColor
               strokeWidth:1
               fill: bind backgroundColor;
            }
            Rectangle {
               x: bind radius, y: bind radius
               width: bind width-radius, height: bind radius
               stroke:bind backgroundColor
               strokeWidth:1
               fill: bind backgroundColor;
            }
              ];
    }


    protected override function resizeContent(){

   }

   protected override function placeInCorner(){
      translateX = scene.width-width;
   }
}


function run(){
   Stage {
      title : "Test corner"
      scene: Scene {
         width: 200
         height: 200
         content: [
            TopRightCorner{
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
