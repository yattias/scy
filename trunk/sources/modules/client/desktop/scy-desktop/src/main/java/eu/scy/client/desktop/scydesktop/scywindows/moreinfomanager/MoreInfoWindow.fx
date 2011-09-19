/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.moreinfomanager;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import eu.scy.client.desktop.desktoputils.art.ScyColors;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import javafx.scene.shape.Rectangle;
import eu.scy.client.desktop.scydesktop.scywindows.window.WindowContent;
import eu.scy.client.desktop.desktoputils.art.EloIcon;
import eu.scy.client.desktop.scydesktop.scywindows.window.WindowClose;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.geometry.Insets;
import javafx.scene.input.MouseEvent;
import eu.scy.client.desktop.desktoputils.art.javafx.MissionMapWindowIcon;
import eu.scy.client.desktop.desktoputils.art.javafx.MoreAssignmentTypeIcon;
import eu.scy.client.desktop.desktoputils.art.javafx.MoreResourcesTypeIcon;
import eu.scy.client.desktop.desktoputils.art.javafx.InstructionTypesIcon;
import javafx.scene.effect.DropShadow;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleManager;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleLayer;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleKey;

/**
 * @author SikkenJ
 */
public class MoreInfoWindow extends CustomNode {

   def iconSize = 40.0;
   public var windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   public var tooltipManager: TooltipManager;
   public var bubbleManager: BubbleManager;
   public var width = 300.0;
   public var height = 200.0;
   public var title = "Title";
   public var eloIcon: EloIcon on replace { eloIconChanged() };
   public var infoTypeIcon: Node;
   public var content: Node;
   public var closeAction: function(): Void;
   public var openAction: function(): Void;
   public var environmentColor = Color.color(.90, .90, .90);
   public var mouseClickedAction: function(: MouseEvent): Void;
   public-init var hideCloseButton: Boolean = false;
   public-init var bubbleLayerId: BubbleLayer;
   public-init var closeBubbleKey: BubbleKey;
   public-init var openCloseBubbleKey: BubbleKey;
   def borderLineWidth = 2.0;
   def borderWidth = 5.0;
   def closeSize = 10.0;
   def topBorderWidth = 2 * borderWidth + iconSize;
   def iconTestWidth = 11.0;
   def titleHeight = 16.0;
   def textInset = 3.0;
   def titleFontsize = 12;
   def textFont = Font.font("Verdana", FontWeight.BOLD, titleFontsize);
   def titleBar = Group {
              layoutX: iconSize + borderWidth + iconTestWidth
              layoutY: borderWidth + (iconSize - titleHeight) / 2.0 + 3
              content: [
                 Rectangle {
                    width: bind width - iconSize - 2 * borderWidth - iconTestWidth
                    height: titleHeight
                    fill: bind windowColorScheme.mainColor
                 }
                 Text {
                    font: textFont
                    x: textInset, y: titleHeight - textInset
                    content: bind title
                    fill: bind windowColorScheme.backgroundColor
                 }
              ]
           }
   public-read def curtainControl = InstructionWindowControl {
              windowColorScheme: bind windowColorScheme
              tooltipManager: tooltipManager
              clickAction: closeAction
              layoutY: height - 10;
              layoutX: width / 2;
           }
   def windowClose = WindowClose {
              windowColorScheme: windowColorScheme
              layoutX: bind width - borderWidth - closeSize
              layoutY: borderWidth
              activated: false
              size: closeSize
              closeAction: closeAction
           }
   def contentElement = WindowContent {
              windowColorScheme: bind windowColorScheme
              width: bind width - 2 * borderWidth - borderLineWidth
              height: bind height - topBorderWidth - borderWidth - borderLineWidth
              content: bind content;
              activated: true
              mouseClickedAction: mouseClickedAction
              layoutX: borderWidth + borderLineWidth / 2.0;
              layoutY: topBorderWidth + borderLineWidth / 2.0;
           }

   public override function create(): Node {
      if (not hideCloseButton){
         bubbleManager.createBubble(windowClose, 5, "moreInfoWindowClose", bubbleLayerId, closeBubbleKey,windowColorScheme)
      }
      bubbleManager.createBubble(curtainControl, 5, "moreInfoWindowOpenClose", bubbleLayerId, openCloseBubbleKey,windowColorScheme);

      Group {
         blocksMouse: true
         content: [
            Rectangle {
               x: 0, y: 0
               width: bind width, height: bind height
               fill: bind windowColorScheme.backgroundColor
               stroke: bind windowColorScheme.mainColor
               strokeWidth: borderLineWidth
               effect: DropShadow {
                  offsetX: 0
                  offsetY: 0
                  color: Color.BLACK
                  radius: 13
               }
            }
            Group {
               layoutX: borderWidth
               layoutY: borderWidth
               content: bind [
                  eloIcon,
                  infoTypeIcon
               ]
            }
            titleBar,
            contentElement,
            if (hideCloseButton) {
               curtainControl
            } else {
               windowClose
            }
         ]
      }
   }

   function eloIconChanged() {
      if (eloIcon != null) {
         eloIcon.size = iconSize;
      }

   }

   public function resizeTheContent(): Void {
      contentElement.resizeTheContent();
   }

   public function setControlFunctionClose(): Void {
      curtainControl.clickAction = closeAction;
      curtainControl.rotate = 0;
      curtainControl.tooltip = ##"close instruction"
   }

   public function setControlFunctionOpen(): Void {
      curtainControl.clickAction = openAction;
      curtainControl.rotate = 180;
      curtainControl.tooltip = ##"open instruction"
   }

}

function run() {
   def spacing = 5.0;
   def iconSize = 40.0;
   def colorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkBlue);
   def moreInfoWindow = MoreInfoWindow {
              layoutX: 40
              layoutY: 60
              windowColorScheme: colorScheme
              hideCloseButton: true
              eloIcon: MissionMapWindowIcon {}
              infoTypeIcon: InstructionTypesIcon {}

              content: Rectangle {
                 width: 500
                 height: 500
                 fill: Color.YELLOW
              }
           }
   moreInfoWindow.infoTypeIcon = MoreResourcesTypeIcon {};
   moreInfoWindow.infoTypeIcon = MoreAssignmentTypeIcon {};
   Stage {
      title: "test more info window"
      onClose: function() {
      }
      scene: Scene {
         width: 400
         height: 400
         fill: Color.LIGHTGRAY
         content: [
            moreInfoWindow,
            HBox {
               spacing: spacing
               padding: Insets {
                  top: spacing
                  left: spacing
               }
               content: [
                  Button {
                     text: "Gray"
                     action: function() {
                        moreInfoWindow.windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray)
                     }
                  }
                  Button {
                     text: "Dark blue"
                     action: function() {
                        moreInfoWindow.windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkBlue)
                     }
                  }
                  Button {
                     text: "Pink"
                     action: function() {
                        moreInfoWindow.windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.pink)
                     }
                  }
               ]
            }
         ]
      }
   }

}

