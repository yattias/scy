/*
 * WindowTitleBar.fx
 *
 * Created on 4-sep-2009, 16:54:15
 */
package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.Group;
import javafx.scene.Node;

import javafx.scene.shape.Line;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import javafx.scene.text.TextOrigin;
import javafx.stage.Stage;
import javafx.scene.Scene;

import javafx.scene.text.TextAlignment;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.scene.control.Button;

/**
 * @author sikkenj
 */
// place your code here
public class WindowTitleBar extends WindowElement {

   public var width = 100.0;
//   public var height = 20.0;
   public var title = "very very long title";
   public var eloIcon:EloIcon on replace oldEloIcon {eloIconChanged(oldEloIcon)};
   public var iconCharacter = "?";
   public var activated = true on replace{eloIcon.selected = activated;};
   public var iconSize = 17.0;
   public var iconGap = 2.0;
   public var textIconSpace = 2.0;
   public var closeBoxWidth = 0.0;
   public var textInset = 1.0;
   public var rectangleAntialiasOffset = 1.0;
   def titleFontsize = 12;
   def textFont = Font.font("Verdana", FontWeight.BOLD, titleFontsize);
   def eloTypeFontsize = 14;
   def eloTypeFont = Font.font("Verdana", FontWeight.BOLD, eloTypeFontsize);
   def mainColor = bind if (activated) color else subColor;
   def bgColor = bind if (activated) subColor else color;
//   def mainColor =  color;
//   def bgColor = subColor;

   var nodeGroup:Group;
//   var fixedGroup:Group;
   var titleText:Text;
   var clipRect:Rectangle;
   var textClipWidth = bind width - iconSize - textIconSpace;

   public-read var titleTextWidth = bind titleText.layoutBounds.maxX;
   public-read var titleDisplayWidth = bind clipRect.layoutBounds.maxX;

   function eloIconChanged(oldEloIcon:EloIcon){
      delete oldEloIcon from nodeGroup.content;
      insert eloIcon before nodeGroup.content[0];
   }

   public override function create(): Node {
      nodeGroup = Group{
         content:[
            eloIcon,
            clipRect= Rectangle{
							x: iconSize+textIconSpace
							y: rectangleAntialiasOffset
							width: bind textClipWidth
							height: iconSize-rectangleAntialiasOffset
							fill: bind mainColor
						},
            titleText = Text { // title
               font: textFont
               textOrigin:TextOrigin.BOTTOM;
               x: iconSize + textIconSpace+textInset,
               y: iconSize-textInset
               clip:Rectangle{
                  x: iconSize+textIconSpace
                  y: 0
                  width: bind width - iconSize - textIconSpace - closeBoxWidth
                  height: iconSize
                  fill: bind mainColor
               }
               fill: bind bgColor

               content: bind title;
              // content: bind "- {title}";
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
					endX: bind width-2,
					endY: iconSize + iconGap
					strokeWidth: 1.0
					stroke: bind color
				},
         ]
      };
//      nodeGroup = Group{
//         content:[
//            eloIcon,
//            fixedGroup
//         ]
//      }
//
   }
}

function run(){
   var windowTitleBar1:WindowTitleBar;

   var eloIcon1 = CharacterEloIcon {
                     iconCharacter: "1"
                  }
   var eloIcon2 = CharacterEloIcon {
                     iconCharacter: "2"
                  }

   var windowTitleBar2: WindowTitleBar;
   var stage:Stage;
   stage = Stage {
         title: "test title bar"
         scene: Scene {
            width: 200
            height: 200
            content: [
               windowTitleBar1 = WindowTitleBar {
                  eloIcon: CharacterEloIcon {
                     iconCharacter: "?"
                  }
                  translateX: 10;
                  translateY: 10;
                  activated: true;
               }
               windowTitleBar2 =WindowTitleBar {
                  eloIcon: CharacterEloIcon {
                     iconCharacter: "W"
                  }
                  iconCharacter: "w"
                  activated: false
                  translateX: 10;
                  translateY: 50;
               }
               Button {
                  translateX: 10;
                  translateY: 90;
                  text: "Swap icon"
                  action: function () {
                     windowTitleBar1.eloIcon = if (windowTitleBar1.eloIcon==eloIcon1) eloIcon2 else eloIcon1;
                     windowTitleBar1.activated = not windowTitleBar1.activated;
                  }
               }
            ]
         }
      }
      windowTitleBar2.eloIcon = CharacterEloIcon {
                     iconCharacter: "Y"
                  }
      stage;

   }
