/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.uicontrols;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import javafx.scene.input.MouseEvent;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.Scene;
import eu.scy.client.desktop.scydesktop.art.eloicons.EloIconFactory;
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

/**
 * @author SikkenJ
 */
public class EloIconButton extends CustomNode, TooltipCreator {

   var originalWindowColorScheme = WindowColorScheme {};
   public var eloIcon: EloIcon on replace { newEloIcon() };
   public var size = 10.0;
   public var action: function(): Void;
   public var turnedOn = false;
   public var tooltip: String;
   public var tooltipManager: TooltipManager;
//   public override var disable on replace { updateColors() }
   public var disableButton = false on replace { updateColors() };
   var mouseOver = false;
   var mousePressed = false;
   def enabledOpacity = 1.0;
   def disabledOpacity = 0.5;
   def lighterColorFactor = 0.5;

   function newEloIcon(): Void {
      originalWindowColorScheme.assign(eloIcon.windowColorScheme);
      eloIcon.windowColorScheme = WindowColorScheme {};
      eloIcon.windowColorScheme.assign(originalWindowColorScheme);
      eloIcon.size = size;
      updateColors();
   }

   function updateColors(): Void {
      if (disableButton) {
         eloIcon.windowColorScheme.assign(originalWindowColorScheme);
         opacity = disabledOpacity;
      } else if ((mouseOver and mousePressed) or turnedOn) {
         opacity = enabledOpacity;
         eloIcon.windowColorScheme.mainColor = originalWindowColorScheme.mainColor;
         eloIcon.windowColorScheme.mainColorLight = originalWindowColorScheme.mainColor;
         eloIcon.windowColorScheme.secondColor = originalWindowColorScheme.secondColor;
         eloIcon.windowColorScheme.secondColorLight = originalWindowColorScheme.secondColor;
         eloIcon.windowColorScheme.thirdColor = originalWindowColorScheme.thirdColor;
         eloIcon.windowColorScheme.thirdColorLight = originalWindowColorScheme.thirdColor;
         eloIcon.windowColorScheme.backgroundColor = originalWindowColorScheme.backgroundColor;
         eloIcon.windowColorScheme.emptyBackgroundColor = originalWindowColorScheme.mainColor;
      } else if (not mouseOver and not mousePressed) {
         opacity = enabledOpacity;
         eloIcon.windowColorScheme.assign(originalWindowColorScheme);
      } else if (mouseOver and not mousePressed) {
         opacity = enabledOpacity;
         eloIcon.windowColorScheme.mainColor = originalWindowColorScheme.mainColorLight;
         eloIcon.windowColorScheme.mainColorLight = originalWindowColorScheme.mainColor;
         eloIcon.windowColorScheme.backgroundColor = originalWindowColorScheme.backgroundColor;
         eloIcon.windowColorScheme.emptyBackgroundColor = originalWindowColorScheme.mainColor;
      } else if (not mouseOver and mousePressed) {
         opacity = enabledOpacity;
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

   function lighterColor(color: Color): Color {
      Color.rgb(lighterColorPart(color.red), lighterColorPart(color.green), lighterColorPart(color.blue), color.opacity)
   }

   function lighterColorPart(value: Number): Integer {
      Math.round(255 * (1 - (1 - value) * lighterColorFactor))
   }

   public override function create(): Node {
      newEloIcon();
      Group {
         cursor: Cursor.HAND
         blocksMouse: true
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
   }

   public override function createTooltipNode(sourceNode: Node): Node {
      if (tooltip!=""){
         return TextTooltip{
            content:tooltip
            windowColorScheme:originalWindowColorScheme
         }

      }
      return null;
   }
}

public class EloIconButtonPreview extends CustomNode {
   public-init var tooltipManager: TooltipManager;
   public-init var size = 20.0;
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
         eloIconButton.size = size;
         eloIconButton.eloIcon = eloIcon.clone();
         eloIconButton.tooltipManager = tooltipManager;
      }
   }

   public override function create(): Node {
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
   def tooltipManager = SimpleTooltipManager{};
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
   Stage {
      title: "Test EloIconButton"
      onClose: function() {
      }
      scene: Scene {
         width: 400
         height: 200
         fill: Color.LAVENDER
         content: [
            EloIconButtonPreview {
               layoutX: 10
               layoutY: 10
               size: 40
               eloIcon: eloIcon.clone()
               tooltipManager: tooltipManager
            }
            SimpleTooltipManager.tooltipGroup
         ]
      }
   }

}
