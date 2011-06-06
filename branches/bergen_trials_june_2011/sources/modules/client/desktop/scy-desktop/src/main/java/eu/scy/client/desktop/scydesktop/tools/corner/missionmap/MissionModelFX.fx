/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

import eu.scy.common.mission.MissionModel;
import eu.scy.common.mission.Las;
import java.util.List;
import java.lang.String;
import java.net.URI;
import org.apache.log4j.Logger;
import eu.scy.common.mission.MissionAnchor;
import eu.scy.common.mission.MissionModelElo;
import eu.scy.common.scyelo.RooloServices;
import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.actionlogging.api.IAction;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.desktoputils.XFX;

/**
 * @author SikkenJ
 *
 * This is a wrapper around the Mission model of common/mission.
 * The content of common/mission is placed in the javafx properties.
 * Any changes to the javafx properties, have to copied to the common/mission.
 * And as the MissionModelFX is implementing MissionModel, any changes MissionModelFX changes should be applied to the javafx properties
 */
public class MissionModelFX extends MissionModel {

   def logger = Logger.getLogger(this.getClass());
   def missionUtils = MissionUtils {};
   public var tbi: ToolBrokerAPI;
   public var missionModel: MissionModel on replace { newMissionModel() };
   public var loEloUris: URI[];
   public var lasses: LasFX[];
   public-read var activeLas: LasFX;
   public-read var missionMapBackgroundImageUri: URI;
   public-read var missionMapInstructionUri: URI;
   public-read var missionMapButtonIconType: String;
   public var saveUpdatedModel = false;
   public var scyDesktop: ScyDesktop;
   var contentChanged = false;

   function newMissionModel(): Void {
      createAllFxVersions();
      // all FX versions are now created, now put them in the fx properties
      loEloUris = missionUtils.getUriSequence(missionModel.getLoEloUris());
      delete  lasses;
      if (missionModel.getLasses() != null) {
         lasses = for (lasObject in missionModel.getLasses()) {
               missionUtils.getLasFX(lasObject as Las)
            }
      }
      missionMapBackgroundImageUri = missionModel.getMissionMapBackgroundImageUri();
      missionMapInstructionUri = missionModel.getMissionMapInstructionUri();
      missionMapButtonIconType = missionModel.getMissionMapButtonIconType();
      // the active las is set a bit later, so that the fullscreen mission map is shown first
   }

   public function initActiveLas(): Void {
      def previousId = activeLas.id;
      activeLas = missionUtils.getLasFX(missionModel.getSelectedLas());
      logger.info("new activeLas {activeLas}");
      missionModel.setSelectedLas(activeLas);
      logLasChange(previousId, activeLas.id);
      updateElo();
   }

   function createAllFxVersions() {
      if (missionModel.getLasses() != null) {
         for (lasObject in missionModel.getLasses()) {
            createLasFX(lasObject as Las);
         }
      }
   }

   function createLasFX(las: Las) {
      missionUtils.createLasFX(las);
      createMissionAnchorFX(las.getMissionAnchor());
      if (las.getIntermediateAnchors() != null) {
         for (anchorObject in las.getIntermediateAnchors()) {
            createMissionAnchorFX(anchorObject as MissionAnchor)
         }
      }
   }

   function createMissionAnchorFX(missionAnchor: MissionAnchor) {
      missionUtils.createMissionAnchorFX(missionAnchor);
   }

   public function anchorSelected(las: LasFX, anchor: MissionAnchorFX): Void {
      if (las == activeLas and las.selectedAnchor == anchor) {
         // nothing changed
         return;
      } else {
         if (anchor == null) {
            las.selectedAnchor = las.mainAnchor;
         } else {
            las.selectedAnchor = anchor;
         }
         def previousId = activeLas.id;
         XFX.deferActionAndWait(function(): Void {
            activeLas = las;
         });
         logger.debug("new activeLas {activeLas}");
         missionModel.setSelectedLas(activeLas);
         logLasChange(previousId, activeLas.id);
         updateElo();
         scyDesktop.edgesManager.findLinks(null);
      }
   }

   public function selectLas(las: LasFX): Void {
      if (las == activeLas) {
         // nothing changed
         return;
      } else {
         def previousId = activeLas.id;
         activeLas = las;
         logger.debug("new activeLas {activeLas}");
         missionModel.setSelectedLas(activeLas);
         logLasChange(previousId, activeLas.id);
         updateElo();
      }
   }

   function logLasChange(oldLasId: String, newLasId: String): Void {
      def action: IAction = new Action();
      action.setType("las_changed");
      action.setUser(tbi.getLoginUserName());
      action.addContext(ContextConstants.tool, "scy-desktop");
      action.addContext(ContextConstants.mission, scyDesktop.missionRunConfigs.missionRuntimeModel.getMissionRuntimeElo().getUri().toString());
      action.addContext(ContextConstants.session, "n/a");
      action.addContext(ContextConstants.eloURI, "n/a");
      action.addAttribute("oldLasId", oldLasId);
      action.addAttribute("newLasId", newLasId);
      tbi.getActionLogger().log(action);
      logger.info("logged LasChange-action: {action}");
   }

   public function removeElo(eloUri: URI): Void {
      if (activeLas != null) {
         delete eloUri from activeLas.otherEloUris;
         updateElo();
      }
   }

   public function eloUriChanged(oldEloUri: URI, newEloUri: URI) {
      logger.info("eloUri changed from {oldEloUri} to {newEloUri}");
      if (oldEloUri == null) {
         // it's a new elo, being saved
         if (activeLas != null) {
            insert newEloUri into activeLas.otherEloUris;
         } else {
            insert newEloUri into loEloUris;
         }
         contentChanged = true;
      }
      else {
         loEloUris = updateEloUris(loEloUris, oldEloUri, newEloUri);
         for (las in lasses) {
            updateLasEloUri(las, oldEloUri, newEloUri)
         }
      }
      if (contentChanged) {
         updateElo();
      }
   }

   function updateEloUris(uris: URI[], oldEloUri: URI, newEloUri: URI): URI[] {
      var updatedUris: URI[];
      for (uri in uris) {
         if (uri == oldEloUri) {
            insert newEloUri into updatedUris;
            contentChanged = true;
         } else {
            insert uri into updatedUris;
         }
      }
      return updatedUris;
   }

   function updateLasEloUri(las: LasFX, oldEloUri: URI, newEloUri: URI) {
      updateAnchorEloUri(las.mainAnchor, oldEloUri, newEloUri);
      for (intermediateAnchor in las.intermediateAnchors) {
         updateAnchorEloUri(intermediateAnchor, oldEloUri, newEloUri);
      }
      las.loEloUris = updateEloUris(las.loEloUris, oldEloUri, newEloUri);
      las.otherEloUris = updateEloUris(las.otherEloUris, oldEloUri, newEloUri);
   }

   function updateAnchorEloUri(anchor: MissionAnchorFX, oldEloUri: URI, newEloUri: URI) {
      if (anchor.eloUri == oldEloUri) {
         anchor.eloUri = newEloUri;
         contentChanged = true;
      }
      anchor.loEloUris = updateEloUris(anchor.loEloUris, oldEloUri, newEloUri);
   }

   override public function updateElo(): Void {
      if (saveUpdatedModel) {
         missionModel.updateElo();
      }
      contentChanged = false;
   }

   override public function loadMetadata(rooloServices: RooloServices): Void {
      missionModel.loadMetadata(rooloServices);
   }

   override public function getMissionModelElo(): MissionModelElo {
      return missionModel.getMissionModelElo()
   }

   override public function getSelectedLas(): Las {
      return missionModel.getSelectedLas()
   }

   override public function getLoEloUris(): List {
      return missionModel.getLoEloUris()
   }

   override public function setSelectedLas(selectedLas: Las): Void {
      def previousId = activeLas.id;
      missionModel.setSelectedLas(selectedLas);
      this.activeLas = missionUtils.getLasFX(selectedLas);
      logger.debug("new activeLas {activeLas}");
      missionModel.setSelectedLas(activeLas);
      logLasChange(previousId, activeLas.id);
      updateElo();
   }

   override public function getEloUris(allElos: Boolean): List {
      return missionModel.getEloUris(allElos);
   }

   override public function getLasses(): List {
      return missionModel.getLasses();
   }

   override public function getMissionMapBackgroundImageUri(): URI {
      return missionModel.getMissionMapBackgroundImageUri();
   }

    override public function getMissionMapButtonIconType () : String {
        return missionModel.getMissionMapButtonIconType();
    }

    override public function getMissionMapInstructionUri () : URI {
        return missionModel.getMissionMapInstructionUri();
    }


}
