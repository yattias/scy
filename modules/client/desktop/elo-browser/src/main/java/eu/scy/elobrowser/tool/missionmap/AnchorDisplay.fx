/*
 * AnchorDisplay2.fx
 *
 * Created on 22-mrt-2009, 13:30:38
 */

package eu.scy.elobrowser.tool.missionmap;

import javafx.scene.paint.Color;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.shape.Line;
import javafx.scene.input.MouseEvent;
import javafx.scene.Cursor;

/**
 * @author sikkenj
 */


public class AnchorDisplay extends CustomNode {
   def size = 20.0;
   def titleFont = Font {
      size: 20
   }
   def defaultTitleColor = Color.WHITE;
   def defaultContentColor = Color.GRAY;

   public var anchor: Anchor;
   var title = bind anchor.title on replace {
      checkTitle()
   };
	//      public var color = Color.LIGHTGRAY;
	//      public var xPos = 0;
	//      public var yPos = 0;
   public-read var xCenter = bind anchor.xPos + size / 2;
   public-read var yCenter = bind anchor.yPos + size / 2;
   public var selected = false on replace {
      setColors()
   };
   public var selectionAction: function(AnchorDisplay):Void;

   var titleColor = defaultTitleColor;
   var contentColor = defaultContentColor;

	var eloContour = EloContour{
		translateX: anchor.xPos;
		translateY: anchor.yPos;
		width: size;
		height: size;
		controlLength: 5;
		borderWidth: 2;
		borderColor: bind anchor.color;
		fillColor: bind contentColor;
	}


   function checkTitle(){
      if (title == null or title.length() == 0){
         title = "?";
      }
      else {
         title = title.substring(0,1);
      }
   }

   function setColors(){
      if (selected){
         titleColor = anchor.color;
         contentColor = defaultTitleColor;
      }
      else {
         titleColor = defaultTitleColor;
         contentColor = anchor.color;
      }
   }


   public override function create(): Node {
      cursor = Cursor.HAND;
      var titleDisplay = Text {
         font: titleFont,
         x: 0,
         y: 0,
         fill: bind titleColor,
         content: bind title,
         translateX:1;
      }
      titleDisplay.x = anchor.xPos + (size - titleDisplay.boundsInLocal.maxX - titleDisplay.boundsInLocal.minX) / 2 + 0;
      titleDisplay.y = anchor.yPos + (size - titleDisplay.boundsInLocal.maxY - titleDisplay.boundsInLocal.minY) / 2 + 1;
      return Group {
         content: [
				//            Rectangle {
				//               x: bind anchor.xPos,
				//               y: bind anchor.yPos,
				//               width: size,
				//               height: size
				//               fill: bind contentColor
				//               stroke: bind anchor.color;
				//               strokeWidth:2;
				//            },
            EloContour{
					translateX: anchor.xPos;
					translateY: anchor.yPos;
					width: size;
					height: size;
					controlLength: 5;
					borderWidth: 2;
					borderColor: bind anchor.color;
					fillColor: bind contentColor;
				}
				titleDisplay
         ],
         onMouseClicked: function( e: MouseEvent ):Void {
            if (selectionAction != null){
               selectionAction(this);
            }
            else {
               selected = not selected;
            }
         },
      };
   }
}

   function run(){

      var anchor1 = AnchorDisplay{
         anchor: Anchor{
            title: "1";
            xPos: 20;
            yPos: 20;
         }
      }
      var anchor2 = AnchorDisplay{
         anchor: Anchor{
            title: "2";
            xPos: 60;
            yPos: 20;
         }
      }
      var anchor3 = AnchorDisplay{
         anchor: Anchor{
            title: "3";
            xPos: 60;
            yPos: 60;
         }
      }
      var anchorg = AnchorDisplay{
         anchor: Anchor{
            title: "g";
            xPos: 20;
            yPos: 100;
         }
      }
      var anchorG = AnchorDisplay{
         anchor: Anchor{
            title: "G";
            xPos: 60;
            yPos: 100;
         }
      }       //      anchor1.nextAnchors = [anchor2,anchor3];

      Stage {
         title: "Anchor test"
         scene: Scene {
            width: 200
            height: 200
            content: [
               anchor1,
               anchor2,
               anchor3
               anchorg
               anchorG
            ]
         }
      }
   }
