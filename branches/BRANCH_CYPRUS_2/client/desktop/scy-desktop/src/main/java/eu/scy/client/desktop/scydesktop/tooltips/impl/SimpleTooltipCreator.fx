/*
 * SimpleTooltipCreator.fx
 *
 * Created on 27-nov-2009, 9:27:42
 */

package eu.scy.client.desktop.scydesktop.tooltips.impl;

import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

/**
 * @author sikken
 */

public class SimpleTooltipCreator extends TooltipCreator{

   public override function createTooltipNode(sourceNode:Node):Node{
      Text {
         font : Font {
            size: 12
         }
         x: 0, y: 0
         content: "Test tooltip"
      }


   }

}
