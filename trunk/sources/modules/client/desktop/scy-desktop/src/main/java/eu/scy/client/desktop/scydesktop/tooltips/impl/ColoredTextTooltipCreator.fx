/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;

import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;

/**
 * @author SikkenJ
 */
public class ColoredTextTooltipCreator extends TooltipCreator {

   public var content: String;
   public var windowColorScheme: WindowColorScheme;

   public override function createTooltipNode(sourceNode: Node): Node {
      ColoredTextTooltip {
         content: content
         color: windowColorScheme.mainColor
      }

   }

}
