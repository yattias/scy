/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextOrigin;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.art.EloImageInformation;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
//import eu.scy.client.desktop.scydesktop.imagewindowstyler.ImageEloIcon;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.client.desktop.scydesktop.uicontrols.MouseOverDisplay;
import eu.scy.client.desktop.scydesktop.art.ImageLoader;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.FxdEloIcon;

/**
 * @author SikkenJ
 */

public class WindowTitleBarDouble extends WindowElement {

   public var width = 100.0;
   public var title = "very very long title";
   public var eloIcon:EloIcon on replace oldEloIcon {eloIconChanged(oldEloIcon)};
   public var activated = true on replace{activatedChanged()};
   public var iconSize = 40.0;
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
   def mouseOverBorderSize = 0.0;

   def borderWidth = 2.0;
   def borderDistance = 22;
   def backgroundColor = Color.web("#eaeaea");

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
      eloIcon.size = iconSize;
   }

   function activatedChanged(){
      eloIcon.selected = activated;
      if (activated){
         textBackgroundFillRect.fill = windowColorScheme.mainColor;
      }
      else{
         textBackgroundFillRect.fill = backgroundColor;
      }
   }

   public override function create(): Node {
      textBackgroundFillRect = Rectangle{
         x: iconSize+textIconSpace
         y: borderDistance+borderWidth/2
         width: bind textClipWidth
         height: borderDistance - 4*borderWidth/2
       }
      clipRect= Rectangle{
         x: iconSize+textIconSpace
         y: 0
         width: bind width - iconSize - textIconSpace
         height: iconSize
         fill: Color.BLACK
      };
      activatedChanged();
      nodeGroup = Group{
         content:[
            Rectangle {
               x: -0, y: 0
               width: bind width, height: 2*borderDistance
               fill: backgroundColor
            }
            Line {
               startX: iconSize+textIconSpace+borderWidth/2, startY: 0
               endX: bind width-borderWidth/2, endY: 0
               strokeWidth: borderWidth
               stroke: bind windowColorScheme.mainColor
            }
            Line {
               startX: iconSize+textIconSpace+borderWidth/2, startY: borderDistance
               endX: bind width-borderWidth/2, endY: borderDistance
               strokeWidth: borderWidth
               stroke: bind windowColorScheme.mainColor
            }
            Line {
               startX: borderWidth/2, startY: 2*borderDistance
               endX: bind width-borderWidth/2, endY: 2*borderDistance
               strokeWidth: borderWidth
               stroke: bind windowColorScheme.mainColor
            }

            Group{
               layoutX:1
               blocksMouse:startDragIcon!=null
               cursor: if(startDragIcon!=null) Cursor.HAND else null
               content:eloIcon
               onMousePressed:function(e: MouseEvent ):Void{
                  startDragIcon(e);
               }
            }
            Line {
               visible: bind not activated
               startX: borderWidth/2, startY: 0
               endX: iconSize+textIconSpace+borderWidth/2, endY: 0
               strokeWidth: borderWidth
               stroke: bind windowColorScheme.mainColor
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
         translateX:1
         translateY:1
         content:[
//            Rectangle{
//               x:-mouseOverBorderSize
//               y:-mouseOverBorderSize
//               width:titleText.layoutBounds.maxX+mouseOverBorderSize+rightBorderSize+1
//               height:iconSize + 2*mouseOverBorderSize
//               fill:windowColorScheme.backgroundColor
//
//            }
            eloIcon.clone(),
            Rectangle{
               x: iconSize+textIconSpace
               y: borderDistance+borderWidth/2
               width: titleText.layoutBounds.width
               height: borderDistance - 4*borderWidth/2
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


function run(){
   var windowColorScheme = WindowColorScheme{
      mainColor:Color.web("#0042f1")
      backgroundColor:Color.web("#f0f8db")
      titleStartGradientColor:Color.web("#4080f8")
      titleEndGradientColor:Color.WHITE
      emptyBackgroundColor:Color.WHITE
   }

   var windowTitleBar1:WindowTitleBarDouble;

   def radius = 10.0;
   var eloIcon1 = FxdEloIcon{
         fxdNode:Circle {
                     centerX: radius, centerY: radius
                     radius: radius
                     fill: Color.GREEN
                  }
         windowColorScheme:windowColorScheme
      };
   var eloIcon2 = FxdEloIcon{
         fxdNode:Circle {
                     centerX: radius, centerY: radius
                     radius: radius
                     fill: Color.GREEN
                  }
         windowColorScheme:windowColorScheme
      };
   var eloIcon3 = FxdEloIcon{
         fxdNode:Circle {
                     centerX: radius, centerY: radius
                     radius: radius
                     fill: Color.GREEN
                  }
         windowColorScheme:windowColorScheme
      };
   var eloIcon4 = FxdEloIcon{
         fxdNode:Circle {
                     centerX: radius, centerY: radius
                     radius: radius
                     fill: Color.GREEN
                  }
         windowColorScheme:windowColorScheme
      };

   var windowTitleBar2: WindowTitleBarDouble;
   var stage:Stage;
   stage = Stage {
         title: "test title bar"
         scene: Scene {
            width: 400
            height: 400
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
               windowTitleBar1 = WindowTitleBarDouble {
                  eloIcon: eloIcon1
                  windowColorScheme: windowColorScheme
                  title:"1: no rotation"
                  translateX: 10;
                  translateY: 10;
                  activated: true;
               }
               windowTitleBar2 =WindowTitleBarDouble {
                  eloIcon: eloIcon2
                  windowColorScheme: windowColorScheme
                  title:"2: no rotation"
                  activated: false
                  translateX: 10;
                  translateY: 70;
               }
               WindowTitleBarDouble {
                  eloIcon: eloIcon3
                  windowColorScheme: windowColorScheme
                  title:"3: rorated by 15"
                  activated: false
                  translateX: 10;
                  translateY: 140;
                  rotate:15
               }
               WindowTitleBarDouble {
                  eloIcon: eloIcon4
                  windowColorScheme: windowColorScheme
                  activated: false
                  title:"4: rotated by -15"
                  translateX: 10;
                  translateY: 210;
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
