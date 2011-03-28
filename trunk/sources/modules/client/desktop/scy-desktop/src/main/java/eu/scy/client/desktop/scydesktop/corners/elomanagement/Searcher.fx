/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import javafx.scene.Node;
import javafx.scene.CustomNode;
import eu.scy.client.desktop.scydesktop.draganddrop.DropTarget2;
import java.lang.Object;
import roolo.elo.api.IMetadata;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.common.scyelo.ScyElo;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.client.desktop.scydesktop.uicontrols.EloIconButton;

/**
 * @author SikkenJ
 */
public class Searcher extends CustomNode, DropTarget2 {

   public-init var tbi: ToolBrokerAPI;
   public-init var eloIcon: EloIcon;
   public-init var buttonSize = 30.0;
   public-init var buttonActionScheme = 1;
   public-init var clickAction: function(): Void;
   public-init var dropAction: function(scyElo: ScyElo): Void;
   public-init var tooltipManager: TooltipManager;
   public-init var tooltip: String;
   public var turnedOn = false on replace {
              searchButton.turnedOn = turnedOn
           };
   def identifierKey = tbi.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER);
   def searchButton: EloIconButton = EloIconButton {
              eloIcon: eloIcon
              size: buttonSize
              actionScheme: buttonActionScheme
              action: clickAction
              tooltipManager: tooltipManager
              tooltip: tooltip
           }

   public override function create(): Node {
      searchButton
   }

   override public function dropEntered(object: Object, canAccept: Boolean): Void {
      if (canAccept) {
         searchButton.turnedOn = true;
      }
   }

   override public function dropLeft(object: Object): Void {
      searchButton.turnedOn = false;
   }

   override public function canAcceptDrop(object: Object): Boolean {
      if (object instanceof IMetadata) {
         return true;
      }
      return false;
   }

   override public function acceptDrop(object: Object): Void {
      if (object instanceof IMetadata) {
         def metadata = object as IMetadata;
         def scyElo = ScyElo.loadMetadata(metadata.getMetadataValueContainer(identifierKey).getValue() as URI, tbi);
         dropAction(scyElo);
      }
   }

}
