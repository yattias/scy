/*
 * TopLeftCorner.fx
 *
 * Created on 30-jun-2009, 16:27:52
 */

package eu.scy.client.desktop.scydesktop.corners;

import javafx.scene.shape.Line;

import javafx.scene.Group;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

import javafx.scene.text.TextOrigin;
import javafx.scene.shape.Rectangle;

/**
 * @author sikkenj
 */

public class TopLeftCorner extends Corner {

    init{
      background.content=[
              Rectangle {
               x: 0, y: 0
               width: bind width, height: bind height-radius
               stroke:bind backgroundColor
               strokeWidth:1
               fill: bind backgroundColor;
            }
            Arc {
               centerX: bind width-radius, centerY: bind height-radius
               radiusX: bind radius, radiusY: bind radius
               startAngle: 0, length: -90
               type: ArcType.ROUND
               stroke:bind backgroundColor
               strokeWidth:1
               fill: bind backgroundColor;
            }
            Rectangle {
               x: 0, y: bind radius
               width: bind width-radius, height: bind height-radius
               stroke: bind backgroundColor
               strokeWidth: 1
               fill: bind Color.BLACK;
            }
              ];
      border.content=[
                          Line {
               startX: 0, startY: bind height
               endX: bind width-radius, endY: bind height
               strokeWidth: bind strokeWidth
               stroke: bind color
            }
            Arc {
               centerX: bind width-radius, centerY: bind height-radius
               radiusX: bind radius, radiusY: bind radius
               startAngle: 0, length: -90
               type: ArcType.OPEN
               fill: null;
               strokeWidth: bind strokeWidth
               stroke: bind color
            }
            Line {
               startX: bind width, startY: bind height-radius
               endX: bind width, endY: 0
               strokeWidth: bind strokeWidth
               stroke: bind color
            }
];
        
    }


   protected override function resizeContent(){
       
   }


}


function run(){
   Stage {
      title : "Test corner"
      scene: Scene {
         width: 200
         height: 200
         content: [
            TopLeftCorner{
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
