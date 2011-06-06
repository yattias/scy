/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.versionviewer;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import javafx.scene.control.Label;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.ScyEloDisplayProperties;
import roolo.elo.api.IMetadataKey;
import javafx.scene.input.MouseEvent;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipCreator;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.tooltips.impl.NodeTooltip;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.ExtendedScyEloDisplayNode;
import eu.scy.client.desktop.scydesktop.elofactory.NewEloCreationRegistry;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;

/**
 * @author SikkenJ
 */
public class ScyEloVersionCellDisplay extends CustomNode, TooltipCreator {

   public var tooltipManager: TooltipManager;
   public var windowColorScheme: WindowColorScheme;
   public var newEloCreationRegistry: NewEloCreationRegistry;
   public var windowStyler: WindowStyler;
   public var versionKey: IMetadataKey;
   public var item: Object on replace {
              if (item instanceof ScyElo) {
                 elo = item as ScyElo;
                 newScyElo();
              } else {
                 elo = null;
                 diaplay.text = "";
              }
           }
   var elo: ScyElo;
   var diaplay = Label {
           }

   function newScyElo(): Void {
      def version = elo.getMetadata().getMetadataValueContainer(versionKey).getValue();
      diaplay.text = "{version}: {ScyEloDisplayProperties.getLastModifiedDateString(elo)}"
   }

   public override function create(): Node {
      Group {
         content: [
            diaplay
         ]
         onMouseEntered: function(e: MouseEvent): Void {
            if (elo != null) {
               tooltipManager.onMouseEntered(e, this);
            }
         }
         onMouseExited: function(e: MouseEvent): Void {
            if (elo != null) {
               tooltipManager.onMouseExited(e);
            }
         }
      }
   }

   public override function createTooltipNode(sourceNode: Node): Node {
      if (elo==null){
         return null;
      }
      def eloIcon = windowStyler.getScyEloIcon(elo);
      eloIcon.windowColorScheme = windowColorScheme;
      NodeTooltip {
         content: ExtendedScyEloDisplayNode {
            scyElo: elo
            newEloCreationRegistry: newEloCreationRegistry
            eloIcon: eloIcon
         }

         windowColorScheme: windowColorScheme
      }
   }

}
