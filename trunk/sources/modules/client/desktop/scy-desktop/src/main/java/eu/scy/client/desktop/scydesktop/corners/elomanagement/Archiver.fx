/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelFX;
import java.lang.Object;
import roolo.elo.api.IMetadata;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import org.apache.log4j.Logger;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowControl;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import java.lang.Void;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.draganddrop.DropTarget2;
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.uicontrols.EloIconButton;

/**
 * @author SikkenJ
 */
public class Archiver extends CustomNode, DropTarget2 {
   def logger = Logger.getLogger(this.getClass());

   public-init var missionMapModel: MissionModelFX;
   public-init var tbi: ToolBrokerAPI;
   public-init var eloIcon: EloIcon;
   public-init var buttonSize = 30.0;
   public-init var buttonActionScheme = 1;
   public-init var tooltipManager: TooltipManager;
   public-init var tooltip: String;
   public var scyWindowControl: ScyWindowControl;
   public var turnedOn = false on replace{
         archiverButton.turnedOn = turnedOn
      };

   def identifierKey = tbi.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER);

   def archiverButton: EloIconButton = EloIconButton {
              eloIcon: eloIcon
              size: buttonSize
              actionScheme: buttonActionScheme
              action: archiveAction
              tooltipManager: tooltipManager
              tooltip: tooltip
           }

   public override function create(): Node {
      Group {
         content: [
            archiverButton
         ]
      }
   }

   function archiveAction():Void{

   }

   override public function dropEntered(object: Object, canAccept: Boolean): Void{
      if (canAccept){
         archiverButton.turnedOn = true;
      }
   }

   override public function dropLeft(object: Object): Void{
      archiverButton.turnedOn = false;
   }

   override public function canAcceptDrop(object: Object): Boolean {
      def eloUri = getArchivebleEloUri(object);
      if (eloUri!=null){
         return true;
      }
      if (object instanceof ScyWindow){
         def window = object as ScyWindow;
         return window.eloUri==null
      }
      return false;
   }

   override public function acceptDrop(object: Object): Void {
      def eloUri = getArchivebleEloUri(object);
      if (eloUri!=null){
         archieveElo(eloUri);
         logger.info("archieved elo: {eloUri}");
         return
      }
      if (object instanceof ScyWindow){
         archieveScyWindow(object as ScyWindow);
      }
   }

   function archieveElo(eloUri: URI):Void{
      scyWindowControl.removeOtherScyWindow(eloUri);
   }

   function archieveScyWindow(window: ScyWindow):Void{
      scyWindowControl.removeOtherScyWindow(window);
   }

   function getArchivebleEloUri(object: Object): URI{
      def eloUri = getEloUri(object);
      if (eloUri!=null){
         // there is a elo passed on
         def coreEloUris = missionMapModel.getEloUris(false);
         if (not coreEloUris.contains(eloUri)){
            // it is not one of the core elos
            return eloUri;
         }
      }
      return null;
   }

   function getEloUri(object: Object): URI{
      if (object instanceof IMetadata){
         def metadata = object as IMetadata;
         return metadata.getMetadataValueContainer(identifierKey).getValue() as URI
      }
      return null;
   }


}
