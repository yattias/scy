/*
 * AnchorDisplayTooltipCreator.fx
 *
 * Created on 27-nov-2009, 10:54:21
 */

package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;

import javafx.scene.Node;


import eu.scy.client.desktop.scydesktop.tooltips.impl.ColoredTextTooltip;

/**
 * @author sikken
 */

public class AnchorDisplayTooltipCreator extends TooltipCreator {

   public override function createTooltipNode(sourceNode:Node):Node{
      if (sourceNode instanceof AnchorDisplay){
         var anchorDisplay = sourceNode as AnchorDisplay;
         return ColoredTextTooltip{
            content:anchorDisplay.las.toolTip;
            color:anchorDisplay.las.color;
         }
      }
      return null;
   }
}
