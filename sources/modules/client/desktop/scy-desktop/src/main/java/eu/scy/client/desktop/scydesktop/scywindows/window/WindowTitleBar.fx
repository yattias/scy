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

import eu.scy.client.desktop.desktoputils.art.EloIcon;
import eu.scy.client.desktop.desktoputils.art.ImageLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.ImageEloIcon;
import eu.scy.client.desktop.desktoputils.art.EloImageInformation;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import javafx.scene.input.MouseEvent;
import eu.scy.client.desktop.scydesktop.uicontrols.MouseOverDisplay;
import javafx.scene.Cursor;
import eu.scy.client.desktop.desktoputils.art.ScyColors;

/**
 * @author sikkenj
 */
// place your code here
public class WindowTitleBar extends WindowElement {

   public var width = 100.0;
   public var title = "very very long title";
   public var eloIcon:EloIcon on replace oldEloIcon {eloIconChanged(oldEloIcon)};
   public var activated = true on replace{activatedChanged()};
   public var iconSize = 16.0;
   public var iconGap = 2.0;
   public var textIconSpace = 5.0;
   public var closeBoxWidth = 0.0;
   public var textInset = 1.0;
   public var rectangleAntialiasOffset = 0.0;
   public var beingDragged = false on replace{
         if (beingDragged){
            mouseOverTitleDisplay.abort();
         }
      };
   public-init var startDragIcon: function(e: MouseEvent ):Void;
   def titleFontsize = 12;
   def textFont = Font.font("Verdana", FontWeight.BOLD, titleFontsize);
   def mainColor = bind if (activated) windowColorScheme.mainColor else windowColorScheme.emptyBackgroundColor;
   def bgColor = bind if (activated) windowColorScheme.backgroundColor else windowColorScheme.mainColor;
   // make sure the background color of the title bar changes, when the main changes
   def theMainColor = bind windowColorScheme.mainColor on replace {
      activatedChanged()
   }
   def mouseOverBorderSize = 2.0;

   var nodeGroup:Group;
   var textBackgroundFillRect:Rectangle;
   var titleText:Text;
   var clipRect:Rectangle;
   var textClipWidth = bind width - iconSize - textIconSpace;
   var mouseOverTitleDisplay:MouseOverDisplay;

//   public-read var titleTextWidth = bind titleText.layoutBounds.maxX;
//   public-read var titleDisplayWidth = bind clipRect.layoutBounds.maxX;

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
         textBackgroundFillRect.fill = Color.WHITE;
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
         y: 0
         width: bind width - iconSize - textIconSpace - closeBoxWidth
         height: iconSize
         fill: Color.BLACK
      };
      activatedChanged();
      nodeGroup = Group{
         content:[
            Group{
               blocksMouse:startDragIcon!=null
               cursor: if(startDragIcon!=null) Cursor.HAND else null
               content:eloIcon
               onMousePressed:function(e: MouseEvent ):Void{
                  startDragIcon(e);
               }
            }
            textBackgroundFillRect,
            titleText = Text { // title
               font: textFont
               textOrigin:TextOrigin.BOTTOM;
               x: iconSize + textIconSpace+textInset,
               y: iconSize-textInset
               clip: clipRect
               fill: bind bgColor
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
         ]
         onMouseEntered: function (e: MouseEvent): Void {
            mouseOverTitleDisplay = MouseOverDisplay{
               createMouseOverDisplay:createMouseOverNode
               mySourceNode:this
               offsetX:-mouseOverBorderSize
               offsetY:-mouseOverBorderSize
            }
         }
         onMouseExited: function (e: MouseEvent): Void {
            if (mouseOverTitleDisplay!=null){
               mouseOverTitleDisplay.stop();
               mouseOverTitleDisplay = null;
            }
         }
      };
//      FX.deferAction(function():Void{
//            MouseOverDisplay{
//                     createMouseOverDisplay:createMouseOverNode
//                     mySourceNode:this
//                     offsetX:-mouseOverBorderSize
//                     offsetY:-mouseOverBorderSize
//                     showImmediate:true
//                  }
//         });
      nodeGroup
   }

   function createMouseOverNode():Node{
      var rightBorderSize = mouseOverBorderSize;
      if (activated and (clipRect.layoutBounds.maxX>titleText.layoutBounds.maxX)){
         // the window is active (it is on top)
         // and the title is not clipped
         // one would expects to assign 0, but to be sure that no white right border is shown due to anti-aliasing make it -2
         rightBorderSize = -2;
      }
      var fullTitleGroup = Group{
         content:[
            Rectangle{
               x:-mouseOverBorderSize
               y:-mouseOverBorderSize
               width:titleText.layoutBounds.maxX+mouseOverBorderSize+rightBorderSize+1
               height:iconSize + 2*mouseOverBorderSize
               fill:windowColorScheme.backgroundColor

            }
            eloIcon.clone(),
            Rectangle{
               x: iconSize+textIconSpace
               y: rectangleAntialiasOffset
               width: titleText.layoutBounds.width
               height: iconSize-rectangleAntialiasOffset
               fill: textBackgroundFillRect.fill
//               fill: Color.GRAY
             }
            titleText = Text {
               font: textFont
               textOrigin:TextOrigin.BOTTOM;
               x: iconSize + textIconSpace+textInset,
               y: iconSize-textInset
               fill: bgColor
               content: title;
            }
         ]
      }
      fullTitleGroup
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
   var windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);

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
                  title:"1: no rotation"
                  translateX: 10;
                  translateY: 10;
                  activated: true;
               }
               windowTitleBar2 =WindowTitleBar {
                  eloIcon: eloIcon2
                  windowColorScheme: windowColorScheme
                  title:"2: no rotation"
                  activated: false
                  translateX: 10;
                  translateY: 50;
               }
               WindowTitleBar {
                  eloIcon: loadEloIcon("scy/drawing")
                  windowColorScheme: windowColorScheme
                  title:"3: rorated by 15"
                  activated: false
                  translateX: 10;
                  translateY: 100;
                  rotate:15
               }
               WindowTitleBar {
                  eloIcon: loadEloIcon("scy/drawing")
                  windowColorScheme: windowColorScheme
                  activated: false
                  title:"4: rotated by -15"
                  translateX: 10;
                  translateY: 150;
                  rotate:-15
               }
//               Button {
//                  translateX: 10;
//                  translateY: 90;
//                  text: "Swap icon"
//                  action: function () {
//                     windowTitleBar1.eloIcon = if (windowTitleBar1.eloIcon==eloIcon1) eloIcon2 else eloIcon1;
//                     windowTitleBar1.activated = not windowTitleBar1.activated;
//                  }
//               }
            ]
         }
      }
      windowTitleBar2.eloIcon = eloIcon2;
      stage;

   }
