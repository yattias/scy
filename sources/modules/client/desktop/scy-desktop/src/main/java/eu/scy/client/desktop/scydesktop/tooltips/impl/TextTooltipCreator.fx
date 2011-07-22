/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tooltips.impl;
import javafx.scene.Node;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;

/**
 * @author SikkenJ
 */

public class TextTooltipCreator  extends TooltipCreator {

   public var content: String;
   public var windowColorScheme: WindowColorScheme;

   public override function createTooltipNode(sourceNode: Node): Node {
      TextTooltip {
         content: content
         windowColorScheme: windowColorScheme
      }

   }
}
