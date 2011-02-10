/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import javafx.scene.shape.Rectangle;
import javafx.scene.layout.Stack;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.common.scyelo.ScyElo;

/**
 * @author SikkenJ
 */
public class LasInfoDisplayTooltip extends CustomNode {

   public var las: LasFX;
   public var windowStyler: WindowStyler;
   public var tbi: ToolBrokerAPI;
   public var openElo : function(scyElo: ScyElo, las: LasFX):Void;
   def contentBorder = 3;
   def borderWidth = 2;

   public override function create(): Node {
      def colorScheme = windowStyler.getWindowColorScheme(las.mainAnchor.scyElo);
      def lasInfoDisplay = LasInfoDisplay {
            las: las
            windowColorScheme: colorScheme
            tbi: tbi
            openElo: openElo
         }

      Stack {
         blocksMouse:true
         content: [
            Rectangle {
               x: bind lasInfoDisplay.layoutBounds.minX - contentBorder
               y: bind lasInfoDisplay.layoutBounds.minY - contentBorder
               width: bind lasInfoDisplay.layoutBounds.maxX + 2 * contentBorder
               height: bind lasInfoDisplay.layoutBounds.maxY + 2 * contentBorder
               fill: colorScheme.backgroundColor;
               stroke: colorScheme.mainColor;
               strokeWidth: borderWidth;
            }
            lasInfoDisplay
         ]
      }
   }

}
