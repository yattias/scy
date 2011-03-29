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
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextOrigin;
import javafx.stage.Stage;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
//import eu.scy.client.desktop.scydesktop.imagewindowstyler.ImageEloIcon;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.client.desktop.scydesktop.uicontrols.MouseOverDisplay;
import eu.scy.client.desktop.scydesktop.art.ImageLoader;
import javafx.scene.shape.Line;
import javafx.scene.shape.Circle;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.FxdEloIcon;
import eu.scy.client.desktop.scydesktop.art.ScyColors;
import eu.scy.client.desktop.scydesktop.art.ArtSource;
import eu.scy.client.desktop.scydesktop.art.EloImageInformation;
import eu.scy.client.desktop.scydesktop.art.FxdImageLoader;
import javafx.scene.shape.Ellipse;
import eu.scy.client.desktop.scydesktop.art.eloicons.EloIconFactory;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.JavaFxWindowStyler;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButton;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import eu.scy.client.desktop.scydesktop.scywindows.TestAttribute;

/**
 * @author SikkenJ
 */
public class WindowTitleBarDouble extends WindowElement {

   public var width = 200.0 on replace {
//              println("new width: {width}");
              updatePositions();
           };
   public var title = "very very long title";
   public var eloIcon: EloIcon on replace oldEloIcon {
              eloIconChanged(oldEloIcon)
           };
   public var activated = true on replace {
              activatedChanged()
           };
   public var windowStateControls: WindowStateControls;
   public var titleBarBuddies: TitleBarBuddies;
   public var titleBarButtons: TitleBarButtons;
   public var titleBarWindowAttributes: TitleBarWindowAttributes;
   public var iconSize = 40.0;
   public var iconGap = 2.0;
   public var textIconSpace = 5.0;
   public var closeBoxWidth = 10.0;
   public var closeBoxShown = false;
   public var textInset = 1.0;
   public var rectangleAntialiasOffset = 0.0;
   public var beingDragged = false on replace {
              if (beingDragged) {
                 mouseOverTitleDisplay.abort();
              }
           };
   public-init var startDragIcon: function(e: MouseEvent): Void;
   public-init var allowDragIcon = true;
   public-read var minimumWidth = width;
   def minimumItemSpacing = 18.0;
   def titleFontsize = 12;
   def textFont = Font.font("Verdana", FontWeight.BOLD, titleFontsize);
   def mainColor = bind if (activated) windowColorScheme.mainColor else windowColorScheme.emptyBackgroundColor;
   def bgColor = bind if (activated) windowColorScheme.emptyBackgroundColor else windowColorScheme.mainColor;
   // make sure the background color of the title bar changes, when the main changes
   def theMainColor = bind windowColorScheme.mainColor on replace {
              activatedChanged()
           }
   def mouseOverBorderSize = 1.0;
   def closeBoxWidthOffset = bind if (closeBoxShown) closeBoxWidth + textIconSpace else 0;
   def borderWidth = 2.0;
   def borderDistance = 22;
   var nodeGroup: Group;
   var eloIconGroup: Group;
   var textBackgroundFillRect: Rectangle;
   var titleText: Text;
   var clipRect: Rectangle;
   var textClipWidth = bind width - iconSize - textIconSpace;
   var mouseOverTitleDisplay: MouseOverDisplay;

   function eloIconChanged(oldEloIcon: EloIcon) {
      eloIcon.size = iconSize;
      delete oldEloIcon from eloIconGroup.content;
      insert eloIcon into eloIconGroup.content;
   }

   function activatedChanged() {
      eloIcon.selected = activated;
      if (activated) {
         textBackgroundFillRect.fill = windowColorScheme.mainColor;
      } else {
         textBackgroundFillRect.fill = windowColorScheme.backgroundColor;
      }
   }

   function updatePositions(): Void {
      titleBarWindowAttributes.itemListChanged = updatePositions;
      titleBarBuddies.itemListChanged = updatePositions;
      titleBarButtons.itemListChanged = updatePositions;
      def y = 2 * borderWidth + 1;
      titleBarWindowAttributes.layoutY = y;
      titleBarBuddies.layoutY = y;
      titleBarButtons.layoutY = y;
      def leftSide = iconSize + textIconSpace;
      def rightSize = width - windowStateControls.layoutBounds.width - 2 * borderWidth;
      //      println("leftSide: {leftSide}, rightSize: {rightSize}");
      def attributesWidth = titleBarWindowAttributes.layoutBounds.width;
      def buddiesWidth = titleBarBuddies.layoutBounds.width;
      def buttonsWidth = titleBarButtons.layoutBounds.width;
      def attributesPresent = sizeof titleBarWindowAttributes.scyWindowAttributes>0;
      def buddiesPresent = sizeof titleBarBuddies.ownershipManager.getOwners()>0;
      def buttonsPresent = sizeof titleBarButtons.titleBarButtons>0;
//      println("attributesWidth: {attributesWidth} ({sizeof titleBarWindowAttributes.scyWindowAttributes}), buddiesWidth: {buddiesWidth}, buttonWidth: {buttonsWidth}");
      var nrOfElements = 0;
      if (buttonsPresent) {
      //         ++nrOfElements
      }
      if (buddiesPresent) {
         ++nrOfElements
      }
      if (attributesPresent) {
         ++nrOfElements
      }
      def spacing = (rightSize - leftSide - attributesWidth - buddiesWidth - buttonsWidth) / (nrOfElements + 1);
      minimumWidth = leftSide + attributesWidth + buddiesWidth + buttonsWidth + (nrOfElements + 1) * minimumItemSpacing + windowStateControls.layoutBounds.width + 2 * borderWidth;
      var x = leftSide;
      if (buttonsPresent) {
         titleBarButtons.layoutX = x;
         x += buttonsWidth + spacing;
      } else {
         x += spacing;
      }
      if (attributesPresent) {
         titleBarWindowAttributes.layoutX = x;
         x += attributesWidth + spacing;
      }
      if (buddiesPresent) {
         titleBarBuddies.layoutX = x;
         x += buddiesWidth + spacing;
      }
//      println("titleBarButtons.layoutX: {titleBarButtons.layoutX}, titleBarWindowAttributes.layoutX: {titleBarWindowAttributes.layoutX}, titleBarBuddies.layoutX: {titleBarBuddies.layoutX}");
   }

   public override function create(): Node {
      if (windowStateControls == null) {
         windowStateControls = WindowStateControls {
                    windowColorScheme: bind windowColorScheme
                 }

      }

      textBackgroundFillRect = Rectangle {
                 x: 0
                 y: 0
                 width: bind textClipWidth
                 height: borderDistance - 4 * borderWidth / 2
              }
      clipRect = Rectangle {
                 x: 0
                 y: 0
                 width: bind width - iconSize - textIconSpace
                 height: iconSize
                 fill: Color.BLACK
              };
      activatedChanged();
      eloIconChanged(null);
      nodeGroup = Group {
                 content: [
                    Rectangle {
                       x: -0, y: 0
                       width: bind width, height: 2 * borderDistance
                       fill: bind windowColorScheme.backgroundColor
                    }
                    Line { // top line
                       startX: iconSize + textIconSpace + borderWidth / 2, startY: 0
                       endX: bind width - borderWidth / 2, endY: 0
                       strokeWidth: borderWidth
                       stroke: bind windowColorScheme.mainColor
                    }
                    Line { // middle line
                       startX: iconSize + textIconSpace + borderWidth / 2, startY: borderDistance
                       endX: bind width - borderWidth / 2, endY: borderDistance
                       strokeWidth: borderWidth
                       stroke: bind windowColorScheme.mainColor
                    }
                    Line { // bottom line
                       startX: borderWidth / 2, startY: 2 * borderDistance
                       endX: bind width - borderWidth / 2, endY: 2 * borderDistance
                       strokeWidth: borderWidth
                       stroke: bind windowColorScheme.mainColor
                    }

                    eloIconGroup = Group {
                               layoutX: 0
                               layoutY: -1
                               blocksMouse: startDragIcon != null
                               cursor: if (allowDragIcon and (startDragIcon != null)) Cursor.HAND else null
                               content: eloIcon
                               onMousePressed: function(e: MouseEvent): Void {
                                  startDragIcon(e);
                               }
                            }
                    Line { // left top line
                       visible: bind not activated
                       startX: borderWidth / 2, startY: 0
                       endX: iconSize + textIconSpace + borderWidth / 2, endY: 0
                       strokeWidth: borderWidth
                       stroke: bind windowColorScheme.mainColor
                    }
                    titleBarButtons,
                    titleBarBuddies,
                    titleBarWindowAttributes,
                    Group {
                       layoutX: bind width - windowStateControls.layoutBounds.width - 2 * borderWidth
                       layoutY: 2 * borderWidth + 1
                       content: windowStateControls;
                    }
                    Group {
                       layoutX: iconSize + textIconSpace
                       layoutY: borderDistance + borderWidth / 2
                       content: [
                          textBackgroundFillRect,
                          titleText = Text { // title
                                     font: textFont
                                     textOrigin: TextOrigin.TOP;
                                     x: textInset,
                                     y: textInset
                                     clip: clipRect
                                     fill: bind bgColor
                                     content: bind title;
                                  },
                       ]
                    }
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
                 onMouseEntered: function(e: MouseEvent): Void {
                    mouseOverTitleDisplay = MouseOverDisplay {
                               createMouseOverDisplay: createMouseOverNode
                               mySourceNode: this
                               offsetX: -mouseOverBorderSize
                               offsetY: -mouseOverBorderSize
                            }
                 }
                 onMouseExited: function(e: MouseEvent): Void {
                    if (mouseOverTitleDisplay != null) {
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
      delayedUpdatePositions(3);

      nodeGroup
   }

   function delayedUpdatePositions(count: Integer): Void {
//      println("delayedUpdatePositions({count})");
      updatePositions();
      if (count > 0) {
         FX.deferAction(function(): Void {
            delayedUpdatePositions(count - 1);
         });
      }

   }

   function createMouseOverNode(): Node {
      var rightBorderSize = mouseOverBorderSize;
      if (activated and (clipRect.layoutBounds.maxX > titleText.layoutBounds.maxX)) {
         // the window is active (it is on top)
         // and the title is not clipped
         // one would expects to assign 0, but to be sure that no white right border is shown due to anti-aliasing make it -2
         rightBorderSize = -2;
      }
      //      println("eloIcon.size: {eloIcon.size} of {eloIcon}");
      var eloIconOffsetX = 0.0;
      var eloIconOffsetY = 0.0;
      var textOffsetX = 0.0;
      var textOffsetY = 1.0;
      if (eloIcon.selected) {
         eloIconOffsetX = 0.0;
         eloIconOffsetY = 0.0;
         textOffsetX = -1.0;
         textOffsetY = 1.0;
      }

      var fullTitleGroup = Group {
                 content: [
                    Rectangle {
                       x: -eloIcon.borderSize / 2, y: -eloIcon.borderSize / 2
                       width: eloIcon.size + eloIcon.borderSize, height: eloIcon.size + eloIcon.borderSize
                       //                  x: 0, y:0
                       //                  width: eloIcon.size , height: eloIcon.size
                       fill: Color.TRANSPARENT
                    }
                    Group {
                       layoutX: eloIconOffsetX
                       layoutY: eloIconOffsetY
                       content: eloIcon.clone()
                    }
                    Group {
                       layoutX: iconSize + textIconSpace + textOffsetX
                       layoutY: borderDistance + borderWidth / 2 + textOffsetY
                       content: [
                          Rectangle {
                             x: 0
                             y: 0
                             width: titleText.layoutBounds.width
                             height: borderDistance - 4 * borderWidth / 2
                             fill: textBackgroundFillRect.fill
                          //               fill: Color.GRAY
                          }
                          titleText = Text {
                                     font: textFont
                                     textOrigin: TextOrigin.TOP;
                                     x: textInset,
                                     y: textInset
                                     fill: bgColor
                                     content: title;
                                  }
                       ]
                    }
                 ]
              }
      fullTitleGroup
   }

}
var imageLoader = ImageLoader.getImageLoader();

function loadEloIcon(type: String): EloIcon {
   def windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   def imageLoader = FxdImageLoader {
              sourceName: ArtSource.plainIconsPackage
              returnDuplicates: true
           };
   var name = EloImageInformation.getIconName(type);
   //   println("name: {name}");
   def eloIcon = FxdEloIcon {
              visible: true
              fxdNode: imageLoader.getNode(name)
              windowColorScheme: windowColorScheme
           }
   //   println("eloIcon: {eloIcon}");
   eloIcon
}

function run() {
   var windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);

   windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.blue);

   println("windowColorScheme: {windowColorScheme}");

   var windowTitleBar1: WindowTitleBarDouble;

   def radius = 10.0;
   var eloIcon1 = FxdEloIcon {
              fxdNode: Ellipse {
                 centerX: radius, centerY: radius
                 radiusX: radius, radiusY: radius / 2
                 fill: Color.GREEN
              }
              windowColorScheme: windowColorScheme
           };

   var eloIcon2 = FxdEloIcon {
              fxdNode: Ellipse {
                 centerX: radius, centerY: radius
                 radiusX: radius, radiusY: radius / 2
                 fill: Color.GREEN
              }
              windowColorScheme: windowColorScheme
           };
   var eloIcon3 = FxdEloIcon {
              fxdNode: Circle {
                 centerX: radius, centerY: radius
                 radius: radius
                 fill: Color.GREEN
              }
              windowColorScheme: windowColorScheme
           };
   var eloIcon4 = FxdEloIcon {
              fxdNode: Circle {
                 centerX: radius, centerY: radius
                 radius: radius
                 fill: Color.GREEN
              }
              windowColorScheme: windowColorScheme
           };

   var windowTitleBar2: WindowTitleBarDouble;
   var stage: Stage;
   stage = Stage {
              title: "test title bar"
              scene: Scene {
                 width: 400
                 height: 400
                 fill: Color.web("#eaeaea")
                 //            fill:LinearGradient {
                 //               startX : 0.0
                 //               startY : -0.5
                 //               endX : 0.0
                 //               endY : 1.0
                 //               stops: [
                 //                  Stop {
                 //                     color : Color.GREEN
                 //                     offset: 0.0
                 //                  },
                 //                  Stop {
                 //                     color : Color.WHITE
                 //                     offset: 1.0
                 //                  },
                 //               ]
                 //            }

                 content: [
                    windowTitleBar1 = WindowTitleBarDouble {
                               eloIcon: eloIcon1
                               windowColorScheme: windowColorScheme
                               title: "1: no rotation, no rotation"
                               titleBarWindowAttributes: TitleBarWindowAttributes {
                                  scyWindowAttributes: [
                                     TestAttribute {
                                        radius: 6.0
                                     }
                                  ]
                               }
                               titleBarButtons: TitleBarButtons {
                                  windowStyler: JavaFxWindowStyler {
                                     eloIconFactory: EloIconFactory {}
                                  }
                                  windowColorScheme: windowColorScheme
                                  titleBarButtons: [
                                     TitleBarButton {
                                        iconType: "pizza"
                                        enabled: true
                                     }
                                     TitleBarButton {
                                        iconType: "drawing"
                                        enabled: true
                                     }
                                  ]
                               }
                               translateX: 10;
                               translateY: 10;
                               activated: true;
                            }
                 //               windowTitleBar2 = WindowTitleBarDouble {
                 //                     eloIcon: eloIcon2
                 //                     windowColorScheme: windowColorScheme
                 //                     title: "2: no rotation, no rotation"
                 //                     activated: false
                 //                     translateX: 10;
                 //                     translateY: 70;
                 //                  }
                 //               WindowTitleBarDouble {
                 //                  eloIcon: loadEloIcon("scy/mapping")
                 //                  windowColorScheme: windowColorScheme
                 //                  title: "3: rorated by 15"
                 //                  activated: false
                 //                  translateX: 10;
                 //                  translateY: 140;
                 //                  rotate: 15
                 //               }
                 //               WindowTitleBarDouble {
                 //                  eloIcon: loadEloIcon("scy/mapping")
                 //                  windowColorScheme: windowColorScheme
                 //                  activated: false
                 //                  title: "4: rotated by -15"
                 //                  translateX: 10;
                 //                  translateY: 210;
                 //                  rotate: -15
                 //               }
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
