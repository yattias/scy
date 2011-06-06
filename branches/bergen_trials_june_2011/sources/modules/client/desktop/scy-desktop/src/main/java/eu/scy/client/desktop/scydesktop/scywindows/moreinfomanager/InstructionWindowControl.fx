/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows.moreinfomanager;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import javafx.stage.Stage;
import javafx.scene.Scene;
import eu.scy.client.desktop.desktoputils.art.ScyColors;
import eu.scy.client.desktop.desktoputils.art.javafx.NotSelectedLogo;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.input.MouseEvent;
import javafx.scene.Cursor;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;
import eu.scy.client.desktop.scydesktop.tooltips.impl.TextTooltip;

/**
 * @author SikkenJ
 */
public class InstructionWindowControl extends CustomNode, TooltipCreator {

   public var windowColorScheme = WindowColorScheme.getWindowColorScheme(ScyColors.darkGray);
   public var clickAction: function(): Void;
   public var tooltip: String;
   public var tooltipManager: TooltipManager;
   def circleBorder = 2.0;

   public override function create(): Node {
      def logo = NotSelectedLogo {
                 color: bind windowColorScheme.mainColor
                 rotate: -90.0
              }
      logo.layoutX = -logo.layoutBounds.width / 2.0 - 0.5;
      logo.layoutY = -logo.layoutBounds.height / 2.0 - 0.5;
      Group {
         layoutY: 3
         cursor: Cursor.HAND
         blocksMouse: true
         content: [
            logo,
            Circle {
               centerX: 0, centerY: 0
               radius: logo.layoutBounds.width / 2.0 + circleBorder
               fill: null
               stroke: bind windowColorScheme.mainColor
            }
         ]
         onMouseClicked: function(m: MouseEvent): Void {
            clickAction();
         }
         onMouseEntered: function(e: MouseEvent): Void {
            tooltipManager.onMouseEntered(e, this);
         }
         onMouseExited: function(e: MouseEvent): Void {
            tooltipManager.onMouseExited(e);
         }
         onMouseReleased: function(e: MouseEvent): Void {
            tooltipManager.onMouseExited(e);
         }
      }
   }

   public override function createTooltipNode(sourceNode: Node): Node {
      if (tooltip != "") {
         return TextTooltip {
                    content: tooltip
                    windowColorScheme: windowColorScheme
                 }

      }
      return null;
   }

}

function run() {
   def xOffset = 80.0;
   def yOffset = 80.0;
   def scale = 4.0;
   Stage {
      title: "MyApp"
      onClose: function() {
      }
      scene: Scene {
         width: 200
         height: 200

         content: Group {
            scaleX: scale
            scaleY: scale
            content: [
               Line {
                  startX: 0, startY: yOffset
                  endX: 2 * xOffset, endY: yOffset
                  strokeWidth: 1
                  stroke: Color.BLACK
               }
               Line {
                  startX: xOffset, startY: 0
                  endX: xOffset, endY: 2 * yOffset
                  strokeWidth: 1
                  stroke: Color.BLACK
               }

               InstructionWindowControl {
                  layoutX: xOffset
                  layoutY: yOffset
               }
            ]
         }
      }
   }

}
