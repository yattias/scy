/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.HBox;
import eu.scy.client.desktop.desktoputils.art.eloicons.EloIconFactory;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.tooltips.impl.TextTooltip;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;

/**
 * @author SikkenJ
 */
public class QuerySelecterDisplay extends CustomNode, TooltipCreator {

   public-init var querySelecter: QuerySelecter;
   public-init var eloIconFactory: EloIconFactory;
   public-init var windowColorScheme: WindowColorScheme;
   public-init var tooltipManager: TooltipManager;
   def querySelecterChoiceBox = ChoiceBox {};
   def iconSize = 20.0;
   def spacing = 5.0;

   public override function create(): Node {
      def icon = eloIconFactory.createEloIcon(querySelecter.getEloIconName());
      icon.size = iconSize;
      icon.windowColorScheme = windowColorScheme;
      tooltipManager.registerNode(icon, this);
      var displayItems = querySelecter.getDisplayOptions().toArray() as String[];
      insert "All" before displayItems[0];
      querySelecterChoiceBox.items = displayItems;
      querySelecterChoiceBox.select(0);
      HBox {
         spacing: spacing
         content: [
            icon,
            querySelecterChoiceBox
         ]
      }
   }

   public override function createTooltipNode(sourceNode: Node): Node {
      TextTooltip {
         content: querySelecter.getEloIconTooltip()
         windowColorScheme: windowColorScheme
      }

   }
}
