/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.versionviewer;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import javafx.scene.input.MouseEvent;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.tooltips.impl.NodeTooltip;
import javafx.scene.layout.Stack;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;

/**
 * @author SikkenJ
 */
public class NodeWithTooltip extends CustomNode, TooltipCreator {

   public var node: Node;
   public var tooltipFunction: function(): Node;
   public var tooltipManager: TooltipManager;
   public var windowColorScheme: WindowColorScheme;

   public override function create(): Node {
      Stack {
         content: [
               node,
               Rectangle{
                  x: bind node.layoutBounds.minX
                  y: bind node.layoutBounds.minY
                  width : bind node.layoutBounds.width
                  height: bind node.layoutBounds.height
                  fill: Color.TRANSPARENT
                  onMouseEntered: function(e: MouseEvent): Void {
                     tooltipManager.onMouseEntered(e, this);
                  }
                  onMouseExited: function(e: MouseEvent): Void {
                     tooltipManager.onMouseExited(e);
                  }
               }
            ]
      }
   }

   public override function createTooltipNode(sourceNode: Node): Node {
      def tooltipNode = tooltipFunction();
      if (tooltipNode!=null){
         return NodeTooltip {
                    content: tooltipNode
                    windowColorScheme: windowColorScheme
                 }
      }
      return null;
   }

}
