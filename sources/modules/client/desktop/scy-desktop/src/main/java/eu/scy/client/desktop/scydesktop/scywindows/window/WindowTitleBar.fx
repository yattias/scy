/*
 * WindowTitleBar.fx
 *
 * Created on 4-sep-2009, 16:54:15
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.Group;
import javafx.scene.Node;

import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import javafx.scene.text.TextOrigin;
import javafx.stage.Stage;
import javafx.scene.Scene;

/**
 * @author sikkenj
 */

// place your code here
public class WindowTitleBar extends WindowElement {

   public var width = 100.0;
//   public var height = 20.0;
   public var title = "very very long title";
   public var typeChar = "?";
   public var iconSize = 16.0;
   public var iconGap = 2.0;

	def titleFontsize = 12;
	def textFont = Font.font("Verdana", FontWeight.BOLD, titleFontsize);
	def eloTypeFontsize = 14;
	def eloTypeFont = Font.font("Verdana", FontWeight.BOLD, eloTypeFontsize);

   public override function create(): Node {
      return Group {
         content: [
            Group{ // icon for title
					content: [
						Rectangle{
							x: 0
							y: 0
							width: iconSize
							height: iconSize
							fill: bind color
						}
						Text {
							font: eloTypeFont
							x: eloTypeFont.size / 4 - 1,
							y: eloTypeFont.size - 1
							content: bind typeChar.substring(0, 1)
							fill: Color.WHITE
						}
					]
				},
            Text { // title
					font: textFont
               textOrigin:TextOrigin.BOTTOM;
					x: iconSize + iconGap,
					y: iconSize
					clip: Rectangle {
						x: iconSize,
						y: 0
						width: bind width - iconSize,
						height: iconSize
						fill: Color.BLACK
					}
					fill: bind color;
					content: bind title;
				},
				//				Group{ // just for checking title clip
				//					content:[
				//						Rectangle {
				//							x: 3 * topLeftBlockSize / 4 + iconSize,
				//							y: 0
				//							width: bind width - 3 * topLeftBlockSize / 4 - iconSize,
				//							height: bind height
				//							fill: Color.BLACK
				//						}
				//					]
				//				},
            Line { // line under title
					startX: 0,
					startY: iconSize + iconGap
					endX: bind width,
					endY: iconSize + iconGap
					strokeWidth: 1.0
					stroke: bind color
				},
         ]
      };
   }
}

function run(){
      Stage {
      title : "test title bar"
      scene: Scene {
         width: 200
         height: 200
         content: [
            WindowTitleBar{
               translateX:10;
               translateY:10;
            }

         ]
      }
   }


}
