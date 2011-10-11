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
import eu.scy.client.desktop.scydesktop.draganddrop.DropTarget2;
import eu.scy.client.desktop.desktoputils.art.EloIcon;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import eu.scy.client.desktop.scydesktop.uicontrols.EloIconButton;
import eu.scy.client.desktop.scydesktop.scywindows.window.ProgressOverlay;
import eu.scy.client.desktop.desktoputils.XFX;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.mission.ArchivedElo;

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
   public-init var clickAction: function(): Void;
   public var scyWindowControl: ScyWindowControl;
   public var turnedOn = false on replace{
         archiverButton.turnedOn = turnedOn
      };

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
      clickAction()
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
      def eloUri = getArchivebleScyElo(object);
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
    ProgressOverlay.startShowWorking();
    XFX.runActionInBackgroundAndCallBack(function(): Object {
        def scyElo = getArchivebleScyElo(object);
        if (scyElo != null) {
            archiveScyElo(scyElo);
            logger.info("archieved elo: {scyElo}");
            return null;
        }
        if (object instanceof ScyWindow) {
            archiveScyWindow(object as ScyWindow);
        }
        return null;
    }, function(o : Object) {
        ProgressOverlay.stopShowWorking();
    });
}

   function archiveScyElo(scyElo: ScyElo):Void{
      scyWindowControl.removeOtherScyWindow(scyElo.getUri());
      missionMapModel.addArchivedElo(new ArchivedElo(scyElo));
   }

   function archiveScyWindow(window: ScyWindow):Void{
      scyWindowControl.removeOtherScyWindow(window);
   }

   function getArchivebleScyElo(object: Object): ScyElo{
      def scyElo = getScyElo(object);
      if (scyElo!=null){
         // there is a elo passed on
         def coreEloUris = missionMapModel.getEloUris(false);
         if (not coreEloUris.contains(scyElo.getUri())){
            // it is not one of the core elos
            return scyElo;
         }
      }
      return null;
   }

   function getScyElo(object: Object): ScyElo{
      if (object instanceof IMetadata){
         def metadata = object as IMetadata;
         return new ScyElo(metadata,tbi);
//         return metadata.getMetadataValueContainer(identifierKey).getValue() as URI
      }
      return null;
   }


}
