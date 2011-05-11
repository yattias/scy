/*
 * SimpleTooltipManager.fx
 *
 * Created on 26-nov-2009, 17:40:11
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;
import java.util.HashMap;
import javafx.scene.input.MouseEvent;
import java.lang.Exception;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import javafx.scene.Group;
import eu.scy.client.desktop.desktoputils.art.AnimationTiming;

/**
 * @author sikken
 */

public var tooltipGroup = Group {
        };

public class SimpleTooltipManager extends TooltipManager {

   def logger = Logger.getLogger(this.getClass());
   def sourceNodes = new HashMap();
   var currentTooltipShower: TooltipShower;
   protected var startAppearingTime = AnimationTiming.startAppearingTime;
   protected var fullAppearingTime = AnimationTiming.fullAppearingTime;
   protected var startDisappearingTime = AnimationTiming.startDisappearingTime;
   protected var fullDisappearingTime = AnimationTiming.fullDisappearingTime;

   public override function registerNode(sourceNode: Node, tooltipCreator: TooltipCreator): Void {
//      println("registerNode({sourceNode},{tooltipCreator})");
      sourceNode.onMouseEntered = onMouseEntered;
      sourceNode.onMouseExited = onMouseExited;
      sourceNodes.put(sourceNode, tooltipCreator);
   }

   public override function unregisterNode(sourceNode: Node): Void {
      sourceNodes.remove(sourceNode);
      sourceNode.onMouseEntered = null;
      sourceNode.onMouseExited = null;
   }

   protected function onMouseEntered(e: MouseEvent): Void {
      if (e.node == currentTooltipShower.sourceNode) {
         // nothing to do
         return
      } else {
         removeTooltip();
      }

      currentTooltipShower = createTooltipShower(e.node, null);
   }

   public override function onMouseEntered(e: MouseEvent, tooltipCreator: TooltipCreator): Void {
//      println("e.node:{e.node}, currentTooltipShower.sourceNode:{currentTooltipShower.sourceNode}");
      if (e.node == currentTooltipShower.sourceNode) {
         // nothing to do
         return
      } else {
         removeTooltip();
      }

      currentTooltipShower = createTooltipShower(e.node, tooltipCreator);
   }

   function createTooltipShower(sourceNode: Node, tooltipCreator: TooltipCreator): TooltipShower {
      def tooltipNode = getTooltipNode(sourceNode, tooltipCreator);
      if (tooltipNode != null) {
         return TooltipShower {
                    tooltipGroup: tooltipGroup
                    tooltipNode: tooltipNode
                    sourceNode: sourceNode
                    startAppearingTime:startAppearingTime
                    fullAppearingTime:fullAppearingTime
                    startDisappearingTime:startDisappearingTime
                    fullDisappearingTime:fullDisappearingTime
                 }

      }
      return null;
   }

   public override function onMouseExited(e: MouseEvent): Void {
      removeTooltip();
   }

   public override function removeTooltip(): Void {
      currentTooltipShower.remove();
      currentTooltipShower = null;
   }

   function getTooltipNode(sourceNode: Node, tooltipCreator: TooltipCreator): Node {
      try {
         var useTooltipCreator = if (tooltipCreator != null) tooltipCreator else sourceNodes.get(sourceNode) as TooltipCreator;
         var newTooltip = useTooltipCreator.createTooltipNode(sourceNode);
         if (newTooltip != null) {
            newTooltip.opacity = 0.0;
            // move it far away, so it does not intercept any mouseevents
            newTooltip.layoutX = 1e6;
            newTooltip.layoutY = 1e6;
            return newTooltip;
         }
      } catch (e: Exception) {
         logger.error("exception during tooltip creation", e);
      }
      return null;
   }
}
