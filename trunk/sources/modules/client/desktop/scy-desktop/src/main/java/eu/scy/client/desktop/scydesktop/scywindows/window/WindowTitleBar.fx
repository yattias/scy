/*
 * WindowTitleBar.fx
 *
 * Created on 4-sep-2009, 16:54:15
 */
package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.scene.Group;
import javafx.scene.Node;

import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import javafx.scene.text.TextOrigin;

import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.client.desktop.scydesktop.art.ImageLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.ImageEloIcon;
import eu.scy.client.desktop.scydesktop.art.EloImageInformation;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;

/**
 * @author sikkenj
 */
// place your code here
public class WindowTitleBar extends WindowElement {

   public var width = 100.0;
//   public var height = 20.0;
   public var title = "very very long title";
   public var eloIcon:EloIcon on replace oldEloIcon {eloIconChanged(oldEloIcon)};
//   public var iconCharacter = "?";
   public var activated = true on replace{activatedChanged()};
   public var iconSize = 16.0;
   public var iconGap = 2.0;
   public var textIconSpace = 5.0;
   public var closeBoxWidth = 0.0;
   public var textInset = 1.0;
   public var rectangleAntialiasOffset = 0.0;
   def titleFontsize = 12;
   def textFont = Font.font("Verdana", FontWeight.BOLD, titleFontsize);
   def mainColor = bind if (activated) windowColorScheme.mainColor else windowColorScheme.emptyBackgroundColor;
   def bgColor = bind if (activated) windowColorScheme.backgroundColor else windowColorScheme.mainColor;
   def textBackgroundGradient = bind LinearGradient {
      startX : 0.0
      startY : 0.0
      endX : 0.0
      endY : 1.0
      stops: [
         Stop {
            color : windowColorScheme.titleStartGradientColor
            offset: 0.0
         },
         Stop {
            color : windowColorScheme.titleEndGradientColor
            offset: 1.0
         },
      ]
   }


   var nodeGroup:Group;
//   var fixedGroup:Group;
   var textBackgroundFillRect:Rectangle;
   var titleText:Text;
   var clipRect:Rectangle;
   var textClipWidth = bind width - iconSize - textIconSpace;

   public-read var titleTextWidth = bind titleText.layoutBounds.maxX;
   public-read var titleDisplayWidth = bind clipRect.layoutBounds.maxX;


   function eloIconChanged(oldEloIcon:EloIcon){
      delete oldEloIcon from nodeGroup.content;
      insert eloIcon before nodeGroup.content[0];
   }

   function activatedChanged(){
      eloIcon.selected = activated;
      if (activated){
         textBackgroundFillRect.fill = windowColorScheme.mainColor;
      }
      else{
         textBackgroundFillRect.fill = textBackgroundGradient;
      }
   }


   public override function create(): Node {
      textBackgroundFillRect = Rectangle{
         x: iconSize+textIconSpace
         y: rectangleAntialiasOffset
         width: bind textClipWidth
         height: iconSize-rectangleAntialiasOffset
       }
      clipRect= Rectangle{
         x: iconSize+textIconSpace
         y: rectangleAntialiasOffset
         width: bind textClipWidth
         height: iconSize-rectangleAntialiasOffset
         fill: bind mainColor
      };
      activatedChanged();
      nodeGroup = Group{
         content:[
            eloIcon,
            textBackgroundFillRect,
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
                  fill: Color.BLACK
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

var imageLoader = ImageLoader.getImageLoader();

function loadEloIcon(type: String):EloIcon{
   var name = EloImageInformation.getIconName(type);
   ImageEloIcon{
      activeImage:imageLoader.getImage("{name}_act.png")
      inactiveImage:imageLoader.getImage("{name}_inact.png")
   }
}


function run(){
   var windowColorScheme = WindowColorScheme{
      mainColor:Color.web("#0042f1")
      backgroundColor:Color.web("#f0f8db")
      titleStartGradientColor:Color.web("#4080f8")
      titleEndGradientColor:Color.WHITE
      emptyBackgroundColor:Color.WHITE
   }

   var windowTitleBar1:WindowTitleBar;

   var eloIcon1 = loadEloIcon("scy/mapping");
   var eloIcon2 = loadEloIcon("scy/drawing");

   var windowTitleBar2: WindowTitleBar;
   var stage:Stage;
   stage = Stage {
         title: "test title bar"
         scene: Scene {
            width: 200
            height: 200
            fill:LinearGradient {
               startX : 0.0
               startY : -0.5
               endX : 0.0
               endY : 1.0
               stops: [
                  Stop {
                     color : Color.GREEN
                     offset: 0.0
                  },
                  Stop {
                     color : Color.WHITE
                     offset: 1.0
                  },
               ]
            }

            content: [
               windowTitleBar1 = WindowTitleBar {
                  eloIcon: eloIcon1
                  windowColorScheme: windowColorScheme
                  translateX: 10;
                  translateY: 10;
                  activated: true;
               }
               windowTitleBar2 =WindowTitleBar {
                  eloIcon: eloIcon2
                  windowColorScheme: windowColorScheme
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
      windowTitleBar2.eloIcon = eloIcon2;
      stage;

   }
