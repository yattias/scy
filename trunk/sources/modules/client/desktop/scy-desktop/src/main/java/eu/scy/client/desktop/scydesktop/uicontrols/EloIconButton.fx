/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.uicontrols;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import eu.scy.client.desktop.desktoputils.art.EloIcon;
import javafx.scene.input.MouseEvent;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import eu.scy.client.desktop.desktoputils.art.eloicons.EloIconFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.geometry.HPos;
import javafx.util.Math;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;
import eu.scy.client.desktop.scydesktop.tooltips.impl.TextTooltip;
import javafx.scene.Cursor;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.tooltips.impl.SimpleTooltipManager;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Stack;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import org.apache.log4j.Logger;

/**
 * @author SikkenJ
 */
public class EloIconButton extends CustomNode, TooltipCreator {

   def logger = Logger.getLogger(this.getClass());
   var originalWindowColorScheme = WindowColorScheme {};
   public var eloIcon: EloIcon on replace { newEloIcon() };
   public var size = 10.0 on replace { sizesChanged() };
   public var mouseOverSize = 20.0 on replace { sizesChanged() };
   public var action: function(): Void;
   public var turnedOn = false on replace { turnOnChanged() };
   public var tooltip: String;
   public var tooltipFunction: function(): String;
   public var tooltipManager: TooltipManager;
   public var actionScheme = 0;
   public var disableButton = false on replace { updateColors() };
   public var hideBackground = false;
   var mouseOver = false;
   var mousePressed = false;
   var eloIconGroup: Group;
   var displaySize = size on replace { displaySizeChanged() };
   def enabledOpacity = 1.0;
   def disabledOpacity = 0.5;
   def lighterColorFactor = 0.5;
   var animationReady = false;
   def growTime = 250ms;
   var sizeChangeTimeLine: Timeline;
   var timelineStarted = false;

   function createSizeChangeTimeline(): Timeline {
//      println("created new timeline");
      timelineStarted = false;
      Timeline {
         repeatCount: 2
         autoReverse: true
         keyFrames: [
            KeyFrame {
               time: 0s
               canSkip: false
               values: [
                  displaySize => size tween Interpolator.EASEBOTH
               ]
               action: function(): Void {
//                  println("at begin, rate: {sizeChangeTimeLine.rate}, timelineStarted: {timelineStarted}");
                  sizeChangeTimeLine.rate = 1;
                  if (timelineStarted) {
                     sizeChangeTimeLine.stop();
                     sizeChangeTimeLine = null;
                  } else {
                     timelineStarted = true;
                  }

               }
            }
            KeyFrame {
               time: growTime
               canSkip: false
               values: [
                  displaySize => mouseOverSize tween Interpolator.EASEBOTH
               ]
               action: function(): Void {
//                  println("at end, rate: {sizeChangeTimeLine.rate}");
                  sizeChangeTimeLine.rate = 1;
                  sizeChangeTimeLine.pause();
               }
            }
         ];
      };
   }

   function getTimelineInfo():String{
      if (sizeChangeTimeLine==null){
         "no timeline"
      } else{
         "rate:{sizeChangeTimeLine.rate}, running: {sizeChangeTimeLine.running}, paused: {sizeChangeTimeLine.paused}, time: {sizeChangeTimeLine.time}"
      }
   }

   function startGrow(): Void {
//      println("startGrow, rate:{sizeChangeTimeLine.rate}, paused: {sizeChangeTimeLine.paused}, time: {sizeChangeTimeLine.time}");
      def timelineInfo = getTimelineInfo();
      if (sizeChangeTimeLine!=null and not sizeChangeTimeLine.running){
         // some times the animation get stuck some how
         // force a reset
         sizeChangeTimeLine.stop();
         sizeChangeTimeLine = null;
      }
      if (sizeChangeTimeLine == null) {
         sizeChangeTimeLine = createSizeChangeTimeline();
         sizeChangeTimeLine.play();
      } else if (sizeChangeTimeLine.paused) {
         sizeChangeTimeLine.play();
      } else {
         sizeChangeTimeLine.rate = -sizeChangeTimeLine.rate;
      }
   }

   function startShrink(): Void {
//      println("startShrink, rate:{sizeChangeTimeLine.rate}, paused: {sizeChangeTimeLine.paused}, time: {sizeChangeTimeLine.time}");
      def timelineInfo = getTimelineInfo();
      if (sizeChangeTimeLine == null) {
      // nothing to do
      } else if (sizeChangeTimeLine.paused) {
         sizeChangeTimeLine.play();
      } else {
         sizeChangeTimeLine.rate = -sizeChangeTimeLine.rate;
      }
   }

   function turnOnChanged(): Void {
      if (turnedOn) {
         startGrow();
      } else {
         startShrink();
      }
      updateColors();
   }

   function newEloIcon(): Void {
      originalWindowColorScheme.assign(eloIcon.windowColorScheme);
      eloIcon.windowColorScheme = WindowColorScheme {};
      eloIcon.windowColorScheme.assign(originalWindowColorScheme);
      displaySizeChanged();
      updateColors();
   }

   function sizesChanged(): Void {
   //      displaySizeChanged()
   }

   function displaySizeChanged(): Void {
      if (mouseOverSize < size) {
         mouseOverSize = size;
      }
      eloIcon.size = displaySize;
      def newLayout = (mouseOverSize - displaySize) / 2;
   //      println("displaySize: {displaySize}, eloIconGroup.layout: {eloIconGroup.layoutX},{eloIconGroup.layoutY}, newLayout: {newLayout}");
   //      eloIconGroup.layoutX = newLayout;
   //      eloIconGroup.layoutY = newLayout;
   }

   function updateColors(): Void {
      if (actionScheme == 1) {
         updateColors1();
         return;
      }
      updateColors0();
   }

   function updateColors0(): Void {
      if (disableButton) {
         eloIcon.windowColorScheme.assign(originalWindowColorScheme);
         if (hideBackground) {
            eloIcon.windowColorScheme.mainColorLight = originalWindowColorScheme.backgroundColor
         }
         opacity = disabledOpacity;
         blocksMouse = false;
         cursor = null;
      } else {
         opacity = enabledOpacity;
         blocksMouse = true;
         cursor = Cursor.HAND;
         if ((mouseOver and mousePressed) or turnedOn) {
            eloIcon.windowColorScheme.mainColor = originalWindowColorScheme.mainColor;
            eloIcon.windowColorScheme.mainColorLight = originalWindowColorScheme.mainColor;
            eloIcon.windowColorScheme.secondColor = originalWindowColorScheme.secondColor;
            eloIcon.windowColorScheme.secondColorLight = originalWindowColorScheme.secondColor;
            eloIcon.windowColorScheme.thirdColor = originalWindowColorScheme.thirdColor;
            eloIcon.windowColorScheme.thirdColorLight = originalWindowColorScheme.thirdColor;
            eloIcon.windowColorScheme.backgroundColor = originalWindowColorScheme.backgroundColor;
            eloIcon.windowColorScheme.emptyBackgroundColor = originalWindowColorScheme.mainColor;
         } else if (not mouseOver and not mousePressed) {
            eloIcon.windowColorScheme.assign(originalWindowColorScheme);
            if (hideBackground) {
               eloIcon.windowColorScheme.mainColorLight = originalWindowColorScheme.backgroundColor
            }
         } else if (mouseOver and not mousePressed) {
            eloIcon.windowColorScheme.mainColor = originalWindowColorScheme.mainColorLight;
            eloIcon.windowColorScheme.mainColorLight = originalWindowColorScheme.mainColor;
            eloIcon.windowColorScheme.backgroundColor = originalWindowColorScheme.backgroundColor;
            eloIcon.windowColorScheme.emptyBackgroundColor = originalWindowColorScheme.mainColor;
         } else if (not mouseOver and mousePressed) {
            eloIcon.windowColorScheme.mainColor = lighterColor(originalWindowColorScheme.mainColor);
            eloIcon.windowColorScheme.mainColorLight = lighterColor(originalWindowColorScheme.mainColor);
            eloIcon.windowColorScheme.secondColor = lighterColor(originalWindowColorScheme.secondColor);
            eloIcon.windowColorScheme.secondColorLight = lighterColor(originalWindowColorScheme.secondColor);
            eloIcon.windowColorScheme.thirdColor = lighterColor(originalWindowColorScheme.thirdColor);
            eloIcon.windowColorScheme.thirdColorLight = lighterColor(originalWindowColorScheme.thirdColor);
            eloIcon.windowColorScheme.backgroundColor = originalWindowColorScheme.backgroundColor;
            eloIcon.windowColorScheme.emptyBackgroundColor = originalWindowColorScheme.mainColor;
         }
      }
   }

   function updateColors1(): Void {
      if (disableButton) {
         eloIcon.windowColorScheme.assign(originalWindowColorScheme);
         if (hideBackground) {
            eloIcon.windowColorScheme.mainColorLight = originalWindowColorScheme.backgroundColor
         }
         opacity = disabledOpacity;
         blocksMouse = false;
         cursor = null;
      } else {
         opacity = enabledOpacity;
         blocksMouse = true;
         cursor = Cursor.HAND;
         if ((mouseOver and mousePressed) or turnedOn) {
            eloIcon.windowColorScheme.mainColor = originalWindowColorScheme.secondColor;
            eloIcon.windowColorScheme.mainColorLight = originalWindowColorScheme.mainColor;
            eloIcon.windowColorScheme.secondColor = originalWindowColorScheme.secondColor;
            eloIcon.windowColorScheme.secondColorLight = originalWindowColorScheme.secondColor;
            eloIcon.windowColorScheme.thirdColor = originalWindowColorScheme.thirdColor;
            eloIcon.windowColorScheme.thirdColorLight = originalWindowColorScheme.thirdColor;
            eloIcon.windowColorScheme.backgroundColor = originalWindowColorScheme.backgroundColor;
            eloIcon.windowColorScheme.emptyBackgroundColor = originalWindowColorScheme.mainColor;
         } else if (not mouseOver and not mousePressed) {
            eloIcon.windowColorScheme.assign(originalWindowColorScheme);
            if (hideBackground) {
               eloIcon.windowColorScheme.mainColorLight = originalWindowColorScheme.backgroundColor
            }
         } else if (mouseOver and not mousePressed) {
            eloIcon.windowColorScheme.mainColor = originalWindowColorScheme.mainColorLight;
            eloIcon.windowColorScheme.mainColorLight = originalWindowColorScheme.mainColor;
            eloIcon.windowColorScheme.backgroundColor = originalWindowColorScheme.backgroundColor;
            eloIcon.windowColorScheme.emptyBackgroundColor = originalWindowColorScheme.mainColor;
         } else if (not mouseOver and mousePressed) {
            eloIcon.windowColorScheme.mainColor = lighterColor(originalWindowColorScheme.secondColor);
            eloIcon.windowColorScheme.mainColorLight = lighterColor(originalWindowColorScheme.mainColor);
            eloIcon.windowColorScheme.secondColor = lighterColor(originalWindowColorScheme.secondColor);
            eloIcon.windowColorScheme.secondColorLight = lighterColor(originalWindowColorScheme.secondColor);
            eloIcon.windowColorScheme.thirdColor = lighterColor(originalWindowColorScheme.thirdColor);
            eloIcon.windowColorScheme.thirdColorLight = lighterColor(originalWindowColorScheme.thirdColor);
            eloIcon.windowColorScheme.backgroundColor = originalWindowColorScheme.backgroundColor;
            eloIcon.windowColorScheme.emptyBackgroundColor = originalWindowColorScheme.mainColor;
         }
      }
   }

   function lighterColor(color: Color): Color {
      Color.rgb(lighterColorPart(color.red), lighterColorPart(color.green), lighterColorPart(color.blue), color.opacity)
   }

   function lighterColorPart(value: Number): Integer {
      Math.round(255 * (1 - (1 - value) * lighterColorFactor))
   }

   public override function create(): Node {
      newEloIcon();
      eloIconGroup = Group {
                 content: bind eloIcon

                 onMouseEntered: function(e: MouseEvent): Void {
                    mouseOver = true;
                    updateColors();
                    tooltipManager.onMouseEntered(e, this);
                 }
                 onMouseExited: function(e: MouseEvent): Void {
                    mouseOver = false;
                    updateColors();
                    tooltipManager.onMouseExited(e);
                 }
                 onMousePressed: function(e: MouseEvent): Void {
                    mousePressed = true;
                    updateColors();
                 }
                 onMouseReleased: function(e: MouseEvent): Void {
                    if (not disableButton and not turnedOn and mouseOver) {
                       tooltipManager.onMouseExited(e);
                       action();
                    }
                    mousePressed = false;
                    updateColors();
                 }
              }
      displaySizeChanged();
      Stack {
         content: [
            Rectangle {
               x: 0, y: 0
               width: mouseOverSize, height: mouseOverSize
               fill: Color.TRANSPARENT
               stroke: null
            }
            eloIconGroup
         ]
         onMouseEntered: function(e: MouseEvent): Void {
            if (not disableButton) {
               startGrow();
            }
         }
         onMouseExited: function(e: MouseEvent): Void {
            if (not turnedOn) {
               startShrink();
            }
         }
      }

   }

   public override function createTooltipNode(sourceNode: Node): Node {
      var tooltipText = if (tooltipFunction != null) tooltipFunction() else tooltip;
      //      println("tooltipFunction: {tooltipFunction}, tooltip: {tooltip}  ->  tooltipText: {tooltipText}");
      if (tooltipText != "") {
         return TextTooltip {
                    content: tooltipText
                    windowColorScheme: originalWindowColorScheme
                 }

      }
      return null;
   }

}

public class EloIconButtonPreview extends CustomNode {

   public-init var tooltipManager: TooltipManager;
   public-init var size = 20.0;
   public-init var mouseOverSize = 20.0;
   public-init var actionScheme = 0;
   public var eloIcon: EloIcon on replace { newEloIcon() };
   def spacing = 5.0;
   var normalEloIconButton = EloIconButton {
              size: size;
              mouseOver: false
              mousePressed: false
              tooltip: "normal"
           }
   var mouseOverEloIconButton = EloIconButton {
              size: size;
              mouseOver: true
              mousePressed: false
              tooltip: "mouse over"
           }
   var mouseOverPressedEloIconButton = EloIconButton {
              size: size;
              mouseOver: true
              mousePressed: true
              tooltip: "mouse over and pressed"
           }
   var mouseOutPressedEloIconButton = EloIconButton {
              size: size;
              mouseOver: false
              mousePressed: true
              tooltip: "mouse out and pressed"
           }
   var disabledEloIconButton = EloIconButton {
              size: size;
              //              disable: true
              disableButton: true
              tooltip: "disabled"
           }
   var turnedOnEloIconButton = EloIconButton {
              size: size;
              turnedOn: true
              tooltip: "turned on"
           }
   var workingEloIconButton = EloIconButton {
              size: size;
              tooltip: "working button"
           }
   def eloIconButtons = [normalEloIconButton, mouseOverEloIconButton,
              mouseOverPressedEloIconButton, mouseOutPressedEloIconButton,
              disabledEloIconButton, turnedOnEloIconButton, workingEloIconButton];

   function newEloIcon(): Void {
      for (eloIconButton in eloIconButtons) {
         eloIconButton.actionScheme = actionScheme;
         eloIconButton.size = size;
         eloIconButton.mouseOverSize = mouseOverSize;
         eloIconButton.eloIcon = eloIcon.clone();
         eloIconButton.tooltipManager = tooltipManager;
      }
      mouseOverEloIconButton.size = mouseOverSize;
      mouseOutPressedEloIconButton.size = mouseOverSize;
      turnedOnEloIconButton.size = mouseOverSize;
   }

   public override function create(): Node {
      if (mouseOverSize < size) {
         mouseOverSize = size;
      }
      newEloIcon();
      Group {
         content: [
            HBox {
               spacing: 2 * spacing
               content: [
                  VBox {
                     spacing: spacing
                     nodeHPos: HPos.CENTER
                     disable: true
                     content: [
                        normalEloIconButton,
                        Label {
                           text: "normal"
                        }
                     ]
                  }
                  VBox {
                     spacing: spacing
                     nodeHPos: HPos.CENTER
                     disable: true
                     content: [
                        mouseOverEloIconButton,
                        Label {
                           text: "mouse over"
                        }
                     ]
                  }
                  VBox {
                     spacing: spacing
                     nodeHPos: HPos.CENTER
                     disable: true
                     content: [
                        mouseOverPressedEloIconButton,
                        Label {
                           text: "pressed"
                        }
                     ]
                  }
                  VBox {
                     spacing: spacing
                     nodeHPos: HPos.CENTER
                     disable: true
                     content: [
                        mouseOutPressedEloIconButton,
                        Label {
                           text: "pressed out"
                        }
                     ]
                  }
                  VBox {
                     spacing: spacing
                     nodeHPos: HPos.CENTER
                     //                     disable: true
                     content: [
                        disabledEloIconButton,
                        Label {
                           text: "disabled"
                        }
                     ]
                  }
                  VBox {
                     spacing: spacing
                     nodeHPos: HPos.CENTER
                     disable: true
                     content: [
                        turnedOnEloIconButton,
                        Label {
                           text: "turned on"
                        }
                     ]
                  }
                  VBox {
                     spacing: spacing
                     nodeHPos: HPos.CENTER
                     content: [
                        workingEloIconButton,
                        Label {
                           text: "working"
                        }
                     ]
                  }
               ]
            }
         ]
      }
   }

}

function run() {
   def tooltipManager = SimpleTooltipManager {};
   def eloIconFactory = EloIconFactory {};
   def windowColorScheme = WindowColorScheme {
              mainColor: Color.rgb(129, 163, 66);
              mainColorLight: Color.rgb(230, 245, 213);
              secondColor: Color.rgb(80, 108, 196);
              secondColorLight: Color.rgb(246, 246, 206);
              thirdColor: Color.rgb(74, 0, 198);
              thirdColorLight: Color.rgb(218, 229, 244);
              backgroundColor: Color.rgb(239, 255, 223);
              emptyBackgroundColor: Color.rgb(255, 255, 255);
           }

   def eloIcon = eloIconFactory.createEloIcon("save");
   eloIcon.windowColorScheme = windowColorScheme;
   def displayAll = VBox {
              spacing: 5.0
              layoutX: 10
              layoutY: 10
              content: [
                 EloIconButtonPreview {
                    layoutX: 10
                    layoutY: 10
                    size: 40
                    eloIcon: eloIcon.clone()
                    tooltipManager: tooltipManager
                 }
                 EloIconButtonPreview {
                    layoutX: 10
                    layoutY: 0
                    size: 14
                    mouseOverSize: 20
                    eloIcon: eloIcon.clone()
                    tooltipManager: tooltipManager
                 }
                 EloIconButtonPreview {
                    layoutX: 10
                    layoutY: 10
                    size: 40
                    eloIcon: eloIcon.clone()
                    tooltipManager: tooltipManager
                    actionScheme: 1
                 }
                 EloIconButtonPreview {
                    layoutX: 10
                    layoutY: 0
                    size: 14
                    mouseOverSize: 20
                    eloIcon: eloIcon.clone()
                    tooltipManager: tooltipManager
                    actionScheme: 1
                 }
              ]
           }
   def singleEloIconButton = EloIconButton {
              layoutX: 10
              layoutY: 10
              size: 20
              mouseOverSize: 30
              eloIcon: eloIcon.clone()
              tooltipManager: tooltipManager
              actionScheme: 1
           }

   Stage {
      title: "Test EloIconButton"
      onClose: function() {
      }
      scene: Scene {
         width: 400
         height: 200
         fill: Color.LAVENDER
         content: [
            displayAll,
            //            singleEloIconButton,

            SimpleTooltipManager.tooltipGroup
         ]
      }
   }

}
