/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;
import eu.scy.common.scyelo.ScyElo;

/**
 * @author SikkenJ
 */

public class LasInfoTooltipCreator extends TooltipCreator {

   public var scyDesktop:ScyDesktop;
   public var openElo : function(scyElo: ScyElo, las: LasFX):Void;

   public override function createTooltipNode(sourceNode:Node):Node{
      if (sourceNode instanceof AnchorDisplay){
         var anchorDisplay = sourceNode as AnchorDisplay;
         return LasInfoDisplayTooltip{
            las: anchorDisplay.las
            windowStyler:scyDesktop.windowStyler
            tbi: scyDesktop.config.getToolBrokerAPI()
            openElo: openElo
         }
      }
      return null;
   }
}
