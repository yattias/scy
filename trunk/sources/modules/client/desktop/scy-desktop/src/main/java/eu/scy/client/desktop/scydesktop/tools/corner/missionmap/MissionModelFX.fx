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
import java.util.Collection;
import eu.scy.client.desktop.scydesktop.scywindows.ShowMoreInfo;
import eu.scy.common.mission.ArchivedElo;
import java.util.concurrent.locks.ReentrantLock;
import java.lang.Thread;
import eu.scy.client.desktop.scydesktop.scywindows.MoreInfoTypes;
import java.awt.EventQueue;
import eu.scy.client.desktop.desktoputils.XFX;
import eu.scy.common.mission.UriScyElo;
import eu.scy.common.scyelo.ScyElo;

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
   public var loEloUris: UriScyElo[];
   public var lasses: LasFX[];
   public-read var activeLas: LasFX;
   public-read var missionMapBackgroundImageUri: URI;
   public-read var missionMapInstructionUri: URI;
   public-read var missionMapButtonIconType: String;
   public var saveUpdatedModel = false;
   public var scyDesktop: ScyDesktop;
   public var storedWindowStatesXmlsChanged = false;
   public var archivedElosChanged = false;
   public var showMoreInfo: ShowMoreInfo;
   var contentChanged = false;
   def updateEloLock = new ReentrantLock();

   function newMissionModel(): Void {
      createAllFxVersions();
      // all FX versions are now created, now put them in the fx properties
      loEloUris = missionUtils.getUriScyEloSequence(missionModel.getLoEloUris());
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
      setSelectedLas(missionModel.getSelectedLas());
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
         setSelectedLas(las);
         scyDesktop.edgesManager.findLinks(null);
      }
   }

   public function setSelectedLas(lasId: String): Void {
      setSelectedLas(getLasById(lasId));
   }

   function setSelectedLas(selectedLas: LasFX): Void {
      setSelectedLas(selectedLas.las)
   }

   override public function setSelectedLas(selectedLas: Las): Void {
      //      println("activeLas: {activeLas}");
      if (activeLas.las == selectedLas) {
         // noting changed
         return;
      }

      def previousId = activeLas.id;
      missionModel.setSelectedLas(selectedLas);
      this.activeLas = missionUtils.getLasFX(selectedLas);
      saveUpdatedModel = true;
      logger.debug("new activeLas {activeLas}");
      logLasChange(previousId, activeLas.id);
      updateElo();
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
         var uriScyEloToDelete:UriScyElo = null;
         for (uriScyElo in activeLas.otherEloUris){
            if (uriScyElo.getUri().equals(eloUri)){
               uriScyEloToDelete = uriScyElo;
            }
         }
         delete uriScyEloToDelete from activeLas.otherEloUris;
         updateElo();
      }
   }

   public function eloUriChanged(oldScyElo: ScyElo, newScyElo: ScyElo) {
      logger.info("eloUri changed from {oldScyElo} to {newScyElo}");
      if (oldScyElo == null) {
         // it's a new elo, being saved
         if (activeLas != null) {
            activeLas.otherEloUris = addIfNotPresent(activeLas.otherEloUris,newScyElo)
         } else {
            loEloUris = addIfNotPresent(loEloUris,newScyElo)
         }
      } else {
         loEloUris = updateEloUris(loEloUris, oldScyElo, newScyElo);
         for (las in lasses) {
            updateLasEloUri(las, oldScyElo, newScyElo)
         }
      }
      if (contentChanged) {
         updateElo();
      }
   }

   function addIfNotPresent(uriScyElos: UriScyElo[], newScyElo: ScyElo ): UriScyElo[]{
      for (uriScyElo in uriScyElos){
         if (uriScyElo.getUri()==newScyElo.getUri()){
            return uriScyElos
         }
      }
      var updatedUriScyElos = uriScyElos;
      insert new UriScyElo(newScyElo) into updatedUriScyElos;
          contentChanged = true;
     return updatedUriScyElos
   }

   function updateEloUris(uriScyElos: UriScyElo[], oldScyElo: ScyElo, newScyElo: ScyElo): UriScyElo[] {
//      var updatedUriScyElos: UriScyElo[];
      for (uriScyElo in uriScyElos) {
         if (uriScyElo.getUri() == oldScyElo.getUri()) {
            uriScyElo.setScyElo(newScyElo);
            contentChanged = true;
         } 
      }
      return uriScyElos;
   }

   function updateLasEloUri(las: LasFX, oldScyElo: ScyElo, newScyElo: ScyElo) {
      updateAnchorEloUri(las.mainAnchor, oldScyElo, newScyElo);
      for (intermediateAnchor in las.intermediateAnchors) {
         updateAnchorEloUri(intermediateAnchor, oldScyElo, newScyElo);
      }
      las.loEloUris = updateEloUris(las.loEloUris, oldScyElo, newScyElo);
      las.otherEloUris = updateEloUris(las.otherEloUris, oldScyElo, newScyElo);
   }

   function updateAnchorEloUri(anchor: MissionAnchorFX, oldScyElo: ScyElo, newScyElo: ScyElo) {
      if (anchor.eloUri == oldScyElo.getUri()) {
         anchor.eloUri = newScyElo.getUri();
         contentChanged = true;
      }
      anchor.loEloUris = updateEloUris(anchor.loEloUris, oldScyElo, newScyElo);
   }

   override public function updateElo(): Void {
      if (saveUpdatedModel or storedWindowStatesXmlsChanged or archivedElosChanged) {
         logger.info("saving mission model because: saveUpdatedModel={saveUpdatedModel} or storedWindowStatesXmlsChanged={storedWindowStatesXmlsChanged} or archivedElosChanged={archivedElosChanged}");
         if (updateEloLock.isLocked()) {
            logger.error("trying to update mission model, while an update is in progress. My thread name: {Thread.currentThread().getName()}");
         }
         def updateMissionModelElo = function(): Void {
                    updateEloLock.lock();
                    try {
                       missionModel.updateElo();
                    } finally {
                       updateEloLock.unlock();
                    }
                 }

         if (EventQueue.isDispatchThread()) {
            XFX.runActionInBackground(updateMissionModelElo);
         } else {
            updateMissionModelElo();
         }
      }
      contentChanged = false;
      storedWindowStatesXmlsChanged = false;
      archivedElosChanged = false;
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

   override public function getEloUris(allElos: Boolean): List {
      return missionModel.getEloUris(allElos);
   }

   override public function getLasses(): List {
      return missionModel.getLasses();
   }

   override public function getMissionMapBackgroundImageUri(): URI {
      return missionModel.getMissionMapBackgroundImageUri();
   }

   override public function getMissionMapButtonIconType(): String {
      return missionModel.getMissionMapButtonIconType();
   }

   override public function getMissionMapInstructionUri(): URI {
      return missionModel.getMissionMapInstructionUri();
   }

   override public function getWindowStatesXmlIds(): Collection {
      missionModel.getWindowStatesXmlIds()
   }

   override public function getWindowStatesXml(lasId: String): String {
      missionModel.getWindowStatesXml(lasId)
   }

   override public function setWindowStatesXml(lasId: String, xml: String): Void {
      if (scyDesktop.initializer.dontUseMissionRuntimeElos) {
         // we are running on the mission specification, don't save the window states
         return
      }
      if (xml != missionModel.getWindowStatesXml(lasId)) {
         storedWindowStatesXmlsChanged = true
      }
      missionModel.setWindowStatesXml(lasId, xml);
   }

   override public function getMissionAnchor(id: String): MissionAnchor {
      return missionModel.getMissionAnchor(id)
   }

   override public function getMissionAnchor(eloUri: URI): MissionAnchor {
      return missionModel.getMissionAnchor(eloUri)
   }

   override public function getMissionAnchors(): List {
      return missionModel.getMissionAnchors()
   }

   override public function getArchivedElos(): List {
      return missionModel.getArchivedElos();
   }

   override public function removeArchivedElo(archivedElo: ArchivedElo): Void {
      if (scyDesktop.initializer.dontUseMissionRuntimeElos) {
         // we are running on the mission specification, don't save the archived elos
         return
      }
      missionModel.removeArchivedElo(archivedElo);
      archivedElosChanged = true
   }

   override public function addArchivedElo(archivedElo: ArchivedElo): Void {
      if (scyDesktop.initializer.dontUseMissionRuntimeElos) {
         // we are running on the mission specification, don't save the archived elos
         return
      }
      missionModel.addArchivedElo(archivedElo);
      archivedElosChanged = true
   }

   function findMissionAnchor(eloUri): MissionAnchorFX {
      for (las in lasses) {
         if (las.mainAnchor.eloUri == eloUri) {
            return las.mainAnchor;
         }
         for (anchor in las.intermediateAnchors) {
            if (anchor.eloUri == eloUri) {
               return anchor;
            }
         }
      }
      return null
   }

   public function getLasById(lasId: String): LasFX {
      for (las in lasses) {
         if (las.id.equals(lasId)) {
            return las;
         }
      }
      return null;
   }

   public function showWebNews(eloUri: URI): Void {
      def missionAnchor = findMissionAnchor(eloUri);
      if (missionAnchor != null and missionAnchor.webNewsUri != null) {
         // we the web news uri to display, now display it...
         //         println("trying to open web news: {missionAnchor.webNewsUri}");
         showMoreInfo.showMoreInfo(missionAnchor.webNewsUri, MoreInfoTypes.WEB_NEWS, eloUri);
      }
   }

}
