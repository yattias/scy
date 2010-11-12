/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.mission;

import org.apache.log4j.Logger;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import javafx.scene.Group;
import eu.scy.client.desktop.scydesktop.utils.EmptyBorderNode;
import javafx.scene.control.ListCell;
import javafx.scene.control.Label;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelXml;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelFX;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionAnchorFX;
import eu.scy.common.scyelo.EloFunctionalRole;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.common.mission.EloToolConfigsElo;
import eu.scy.client.desktop.scydesktop.utils.i18n.Composer;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.common.mission.TemplateElosElo;
import eu.scy.common.mission.RuntimeSettingsElo;
import eu.scy.common.mission.impl.BasicRuntimeSettingsEloContent;
import eu.scy.common.mission.MissionEloType;
import eu.scy.client.desktop.scydesktop.hacks.RepositoryWrapper;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.design.SimpleSaveAsNodeDesign;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.design.EloSaveAsMixin;

/**
 * @author SikkenJ
 */
public class MissionLocator {

   def logger = Logger.getLogger(this.getClass());
   public-init var tbi: ToolBrokerAPI;
   public-init var userName: String;
   public-init var initializer: Initializer;
   public-init var missions: Missions;
   public-init var window: ScyWindow;
   public-init var startMission: function(missionRunConfigs: MissionRunConfigs): Void;
   public-init var cancelMission: function(): Void;
   var askUserForMissionNode: AskUserForMissionNode;
   def technicalFormatKey = tbi.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
   def anchorIdKey = tbi.getMetaDataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.ANCHOR_ID);
   def lasKey = tbi.getMetaDataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.LAS);
   def containsAssignmentEloKey = tbi.getMetaDataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.CONTAINS_ASSIGMENT_ELO);
   def functionalTypeKey = tbi.getMetaDataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.FUNCTIONAL_TYPE);
   def missionIdKey = tbi.getMetaDataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.MISSION);
   def missionRunningKey = tbi.getMetaDataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.MISSION_RUNNING);
   var eloToolConfigsElo: EloToolConfigsElo;
   var templateElosElo: TemplateElosElo;
   var runtimeSettingsElo: RuntimeSettingsElo;
   var missionMapModel: MissionModelFX;

   public function locateMission(): Void {
      if (missions.isEmpty()) {
         startBlankMission();
      }
      if (missions.size() == 1 and not initializer.authorMode) {
         if (missions.missionRuntimeElos.isEmpty()) {
            startNewMission(missions.missionSpecificationElos.get(0));
         } else {
            continueMission(missions.missionRuntimeElos.get(0));
         }
         return;
      }
      if (initializer.defaultMission!=""){
         // a default mission is specified, try to use that
         def defaultMissionRuntimeElo = missions.findMissionRuntimeEloByTitle(initializer.defaultMission);
         if (defaultMissionRuntimeElo!=null){
            continueMission(defaultMissionRuntimeElo);
            return;
         }
         def defaultMissionSpecificationElo = missions.findMissionSpecificationEloByTitle(initializer.defaultMission);
         if (defaultMissionSpecificationElo!=null){
            startNewMission(defaultMissionSpecificationElo);
            return;
         }
         logger.info("failed to find the default mission: {initializer.defaultMission}");
      }

      // multiple missions possible, ask the user which one
      askUserForMission(missions);
   }

   function startNewMission(missionSpecificationElo: MissionSpecificationElo) {
      if (not initializer.dontUseMissionRuntimeElos){
         // this is the normal way
         logger.info("prepare new mission runtime for {missionSpecificationElo.getUri()}");
      }
      else {
         // this is a special way, try not to change the elos while running,
         // this is meant for when an author is checking the entered mission
         // try to use the specificatins elos directly
         // or do not save new elos
         logger.info("starting mission (without runtime elos) from {missionSpecificationElo.getUri()}");
      }
      // we are starting with a new mission, so we have to create a new MissionRuntimeElo
      def missionRuntimeElo = MissionRuntimeElo.createElo(tbi);
      missionRuntimeElo.setTitle(missionSpecificationElo.getTitle());
      missionRuntimeElo.setDescription(missionSpecificationElo.getDescription());
      missionRuntimeElo.setMissionRunning(userName);
      if (not initializer.dontUseMissionRuntimeElos){
         missionRuntimeElo.saveAsNewElo();
      }
      injectMissionRuntimeEloInRepository(missionRuntimeElo.getUriFirstVersion());
      def missionSpecification = missionSpecificationElo.getTypedContent();
      // create the elo tool configs
      eloToolConfigsElo = EloToolConfigsElo.loadElo(missionSpecification.getEloToolConfigsEloUri(), tbi);
      if (not initializer.dontUseMissionRuntimeElos){
         eloToolConfigsElo.saveAsForkedElo();
      }
      // create the personal mission map model
      var personalMissionMapModelElo: ScyElo;
      if (not initializer.dontUseMissionRuntimeElos){
         personalMissionMapModelElo = createPersonalMissionMapModelElo(missionSpecification.getMissionMapModelEloUri());
      }
      else{
         // misuse the specification mission map model ELO as personal mission map model ELO
         personalMissionMapModelElo = ScyElo.loadElo(missionSpecification.getMissionMapModelEloUri(), tbi);
         missionMapModel = MissionModelXml.convertToMissionModel(personalMissionMapModelElo.getContent().getXmlString());
         // don't set the elo and the eloFactory, so that it cannot update itself
      }
      // create the template elos elo
      templateElosElo = TemplateElosElo.loadElo(missionSpecification.getTemplateElosEloUri(), tbi);
      if (not initializer.dontUseMissionRuntimeElos){
         templateElosElo.saveAsForkedElo();
      }
      // create an empty runtime settings elo
      runtimeSettingsElo = RuntimeSettingsElo.loadElo(missionSpecification.getRuntimeSettingsEloUri(), tbi);
      if (runtimeSettingsElo != null) {
         runtimeSettingsElo.setTypeContent(new BasicRuntimeSettingsEloContent());
         if (not initializer.dontUseMissionRuntimeElos){
            runtimeSettingsElo.saveAsForkedElo();
         }
      }
      else {
         runtimeSettingsElo = RuntimeSettingsElo.createElo(tbi);
         runtimeSettingsElo.setTitle(missionSpecificationElo.getTitle());
         if (not initializer.dontUseMissionRuntimeElos){
            runtimeSettingsElo.saveAsNewElo();
         }
      }

      missionRuntimeElo.setMissionSpecificationElo(missionSpecificationElo.getUri());
      missionRuntimeElo.getTypedContent().setMissionSpecificationEloUri(missionSpecificationElo.getUri());
      missionRuntimeElo.getTypedContent().setMissionMapModelEloUri(personalMissionMapModelElo.getUri());
      missionRuntimeElo.getTypedContent().setEloToolConfigsEloUri(eloToolConfigsElo.getUri());
      missionRuntimeElo.getTypedContent().setTemplateElosEloUri(templateElosElo.getUri());
      missionRuntimeElo.getTypedContent().setRuntimeSettingsEloUri(runtimeSettingsElo.getUri());
      if (not initializer.dontUseMissionRuntimeElos){
         missionRuntimeElo.updateElo();
      }
      startMission(MissionRunConfigs {
         tbi: tbi
         missionRuntimeElo: missionRuntimeElo
         eloToolConfigsElo: eloToolConfigsElo
         missionMapModel: missionMapModel
         templateElosElo: templateElosElo
         runtimeSettingsElo: runtimeSettingsElo
      });
   }

   function continueMission(missionRuntimeElo: MissionRuntimeElo) {
      injectMissionRuntimeEloInRepository(missionRuntimeElo.getUriFirstVersion());
      if (missionRuntimeElo.getTypedContent().getMissionMapModelEloUri() != null) {
         def scyElo = ScyElo.loadLastVersionElo(missionRuntimeElo.getTypedContent().getMissionMapModelEloUri(), tbi);
         missionMapModel = MissionModelXml.convertToMissionModel(scyElo.getContent().getXmlString());
         missionMapModel.elo = scyElo.getElo();
         missionMapModel.eloFactory = tbi.getELOFactory();
      }
      if (missionRuntimeElo.getTypedContent().getEloToolConfigsEloUri() != null) {
         eloToolConfigsElo = EloToolConfigsElo.loadLastVersionElo(missionRuntimeElo.getTypedContent().getEloToolConfigsEloUri(), tbi);
      }
      if (missionRuntimeElo.getTypedContent().getTemplateElosEloUri() != null) {
         templateElosElo = TemplateElosElo.loadLastVersionElo(missionRuntimeElo.getTypedContent().getTemplateElosEloUri(), tbi);
      }
      if (missionRuntimeElo.getTypedContent().getRuntimeSettingsEloUri() != null) {
         runtimeSettingsElo = RuntimeSettingsElo.loadLastVersionElo(missionRuntimeElo.getTypedContent().getRuntimeSettingsEloUri(), tbi);
      }
      startMission(MissionRunConfigs {
         tbi: tbi
         missionRuntimeElo: missionRuntimeElo
         eloToolConfigsElo: eloToolConfigsElo
         missionMapModel: missionMapModel
         templateElosElo: templateElosElo
         runtimeSettingsElo: runtimeSettingsElo
      });
   }

   function startBlankMission(): Void {
      askUserForBlankMissionName(MissionRuntimeElo.createElo(tbi));
   }

   function askUserForBlankMissionName(missionRuntimeElo: MissionRuntimeElo){
      var eloSaveAsPanel = SimpleSaveAsNodeDesign {
            saveAction: createBlankMissionAction
            cancelAction: cancelBlankMissionAction
            scyElo: missionRuntimeElo
         }
      eloSaveAsPanel.setTitle(##"Empty mission");
      Composer.localizeDesign(eloSaveAsPanel.getDesignRootNodes());
      eloSaveAsPanel.modalDialogBox = ModalDialogBox {
            content: Group {
               content: eloSaveAsPanel.getDesignRootNodes()
            }
            title: ##"Enter title"
            eloIcon: window.eloIcon
            windowColorScheme: window.windowColorScheme
         }
   }

   function createBlankMissionAction(eloSaveAsMixin: EloSaveAsMixin): Void{
      eloSaveAsMixin.modalDialogBox.close();
      def missionRuntimeElo = eloSaveAsMixin.scyElo as MissionRuntimeElo;
      missionRuntimeElo.setTitle(eloSaveAsMixin.getTitle());
      missionRuntimeElo.setMissionRunning(userName);
      if (not initializer.dontUseMissionRuntimeElos){
         missionRuntimeElo.saveAsNewElo();
      }
      injectMissionRuntimeEloInRepository(missionRuntimeElo.getUriFirstVersion());

      missionMapModel = MissionModelFX {
         }
      def missionMapModelElo = ScyElo.createElo(MissionEloType.MISSION_MAP_MODEL.getType(), tbi);
      missionMapModelElo.setTitle(missionRuntimeElo.getTitle());
      missionMapModelElo.getContent().setXmlString(MissionModelXml.convertToXml(missionMapModel));
      missionMapModelElo.getMetadata().getMetadataValueContainer(missionRunningKey).setValue(userName);
      if (not initializer.dontUseMissionRuntimeElos){
         missionMapModelElo.saveAsNewElo();
         missionMapModel.elo = missionMapModelElo.getElo();
         missionMapModel.eloFactory = tbi.getELOFactory();
      }

      runtimeSettingsElo = RuntimeSettingsElo.createElo(tbi);
      runtimeSettingsElo.setTitle(missionRuntimeElo.getTitle());
      if (not initializer.dontUseMissionRuntimeElos){
         runtimeSettingsElo.saveAsNewElo();
      }

      missionRuntimeElo.getTypedContent().setMissionSpecificationEloUri(null);
      missionRuntimeElo.getTypedContent().setMissionMapModelEloUri(missionMapModelElo.getUri());
      missionRuntimeElo.getTypedContent().setEloToolConfigsEloUri(null);
      missionRuntimeElo.getTypedContent().setRuntimeSettingsEloUri(runtimeSettingsElo.getUri());
      if (not initializer.dontUseMissionRuntimeElos){
         missionRuntimeElo.updateElo();
      }
      startMission(MissionRunConfigs {
         tbi: tbi
         missionRuntimeElo: missionRuntimeElo
         eloToolConfigsElo: null
         missionMapModel: missionMapModel
         templateElosElo: null
         runtimeSettingsElo: runtimeSettingsElo
      });
   }

   function cancelBlankMissionAction(eloSaveAsMixin: EloSaveAsMixin): Void{
      eloSaveAsMixin.modalDialogBox.close();
   }

   function injectMissionRuntimeEloInRepository(missionRuntimeEloUri: URI) {
      println("tbi.getRepository(): {tbi.getRepository().getClass()}");
      if (tbi.getRepository() instanceof RepositoryWrapper) {
         var repositoryWrapper = tbi.getRepository() as RepositoryWrapper;
         repositoryWrapper.setMissionRuntimeEloUri(missionRuntimeEloUri);
      }
   }

   function askUserForMission(missions: Missions) {
      askUserForMissionNode = AskUserForMissionNode {
         }
      askUserForMissionNode.blankButton.visible = initializer.authorMode;
      askUserForMissionNode.blankButton.action = blankMissionAction;
      askUserForMissionNode.goButton.action = missionSelected;
      askUserForMissionNode.cancelButton.action = cancelAction;
      askUserForMissionNode.startedMissionListView.cellFactory = missionCellFactory;
      var startedMissions: MissionRuntimeElo[] = missions.getMissionRuntimeElosArray();
      askUserForMissionNode.startedMissionListView.items = startedMissions;
      askUserForMissionNode.newMissionListView.cellFactory = missionCellFactory;
      var newMissions: MissionSpecificationElo[] = missions.getMissionSpecificationElosArray();
      askUserForMissionNode.newMissionListView.items = newMissions;
      Composer.localizeDesign(askUserForMissionNode.getDesignRootNodes());
      window.scyContent = EmptyBorderNode {
            content: Group {
               content: askUserForMissionNode.getDesignRootNodes()
            }
         }
   }

   function missionSelected(): Void {
      if (askUserForMissionNode.startedMissionListView.selectedItem != null) {
         continueMission(askUserForMissionNode.startedMissionListView.selectedItem as MissionRuntimeElo)
      } else {
         startNewMission(askUserForMissionNode.newMissionListView.selectedItem as MissionSpecificationElo)
      }
   }

   function cancelAction() {
      cancelMission();
   }

   function blankMissionAction() {
      startBlankMission();
   }

   function missionCellFactory(): ListCell {
      var listCell: ListCell;
      listCell = ListCell {
            node: Label {
               text: bind (listCell.item as ScyElo).getTitle()
            }
         }
   }

   function createPersonalMissionMapModelElo(missionMapModelEloUri: URI): ScyElo {
      def missionMapModelElo = ScyElo.loadElo(missionMapModelEloUri, tbi);
      missionMapModel = MissionModelXml.convertToMissionModel(missionMapModelElo.getContent().getXmlString());
      makePersonalMissionMapModel(missionMapModel);
      missionMapModelElo.getContent().setXmlString(MissionModelXml.convertToXml(missionMapModel));
      missionMapModelElo.getMetadata().getMetadataValueContainer(missionRunningKey).setValue(userName);
      missionMapModelElo.saveAsForkedElo();
      missionMapModel.elo = missionMapModelElo.getElo();
      missionMapModel.eloFactory = tbi.getELOFactory();
      missionMapModelElo
   }

   function makePersonalMissionMapModel(missionMapModel: MissionModelFX): Void {
      if (initializer.usingRooloCache) {
         // load all anchor elos in one call into the roolo cache
         var anchorEloUris = missionMapModel.getEloUris(true);
         tbi.getRepository().retrieveELOs(anchorEloUris);
      }
      for (las in missionMapModel.lasses) {
         makePersonalMissionAnchor(las.mainAnchor);
         for (anchor in las.intermediateAnchors) {
            makePersonalMissionAnchor(anchor);
         }
      }
   }

   function makePersonalMissionAnchor(missionAnchor: MissionAnchorFX) {
      var missionAnchorElo = tbi.getRepository().retrieveELO(missionAnchor.eloUri);
      if (missionAnchorElo != null) {
         missionAnchorElo.getMetadata().getMetadataValueContainer(anchorIdKey).setValue(missionAnchor.id);
         missionAnchorElo.getMetadata().getMetadataValueContainer(lasKey).setValue(missionAnchor.las.id);
         var assignmentEloUri = findAssignmentEloUri(missionAnchor);
         if (assignmentEloUri != null) {
            missionAnchorElo.getMetadata().getMetadataValueContainer(containsAssignmentEloKey).setValue(assignmentEloUri);
         }
         var eloConfig = eloToolConfigsElo.getTypedContent().getEloToolConfig(missionAnchorElo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue() as String);
         if (not eloConfig.isContentStatic()) {
            var forkedMissionAnchorEloMetadata = tbi.getRepository().addForkedELO(missionAnchorElo);
            tbi.getELOFactory().updateELOWithResult(missionAnchorElo, forkedMissionAnchorEloMetadata);
            missionAnchor.eloUri = missionAnchorElo.getUri();
         }
      } else {
         logger.error("failed to load existing anchor elo, uri: {missionAnchor.eloUri}");
         missionAnchor.eloUri = null;
      }
   }

   function findAssignmentEloUri(missionAnchor: MissionAnchorFX): URI {
      for (loEloUri in missionAnchor.loEloUris) {
         var loMetadata = tbi.getRepository().retrieveMetadata(loEloUri);
         var functionalType = loMetadata.getMetadataValueContainer(functionalTypeKey).getValue() as String;
         if (EloFunctionalRole.INFORMATION_ASSIGNMENT.equals(functionalType)) {
            return loEloUri;
         }
      }
      if (sizeof missionAnchor.loEloUris > 0) {
         return missionAnchor.loEloUris[0];
      }
      return null;
   }

}
