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
import eu.scy.client.desktop.scydesktop.AskUserForMissionNode;
import javafx.scene.control.ListCell;
import javafx.scene.control.Label;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelXml;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelFX;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionAnchorFX;
import eu.scy.common.mission.EloFunctionalRole;
import java.net.URI;
import roolo.elo.api.IELO;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.common.mission.EloToolConfigsElo;
import eu.scy.client.desktop.scydesktop.utils.i18n.Composer;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.common.mission.TemplateElosElo;

/**
 * @author SikkenJ
 */
public class MissionLocator {

   def logger = Logger.getLogger(this.getClass());
   public-init var tbi: ToolBrokerAPI;
   public-init var userName: String;
   public-init var initializer: Initializer;
   public-init var window: ScyWindow;
   public-init var startMission: function(missionRunConfigs: MissionRunConfigs): Void;
   public-init var cancelMission: function(): Void;
   var askUserForMissionNode: AskUserForMissionNode;
//   def titleKey = tbi.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TITLE);
   def technicalFormatKey = tbi.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
   def anchorIdKey = tbi.getMetaDataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.ANCHOR_ID);
   def lasKey = tbi.getMetaDataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.LAS);
   def containsAssignmentEloKey = tbi.getMetaDataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.CONTAINS_ASSIGMENT_ELO);
   def functionalTypeKey = tbi.getMetaDataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.FUNCTIONAL_TYPE);
   def missionIdKey = tbi.getMetaDataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.MISSION);
   def missionRunningKey = tbi.getMetaDataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.MISSION_RUNNING);
   var eloToolConfigsElo: EloToolConfigsElo;
   var templateElosElo: TemplateElosElo;
   var missionMapModel: MissionModelFX;

   public function locateMission(): Void {
      var missions = MissionLocatorUtils.findMissions(tbi, userName);
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
      // multiple missions possible, ask the user which one
      askUserForMission(missions);
   }

   function startNewMission(missionSpecificationElo: MissionSpecificationElo) {
      logger.info("prepare new mission runtime for {missionSpecificationElo.getUri()}");
      // we are starting with a new mission, so we have to create a new MissionRuntimeElo
      def missionSpecification = missionSpecificationElo.getTypedContent();
      // create the elo tool configs
      var elo = tbi.getRepository().retrieveELO(missionSpecification.getEloToolConfigsEloUri());
      var metadata = tbi.getRepository().addForkedELO(elo);
      tbi.getELOFactory().updateELOWithResult(elo, metadata);
      eloToolConfigsElo = new EloToolConfigsElo(elo, tbi);
      // create the personal mission map model
      def personalMissionMapModelElo = createPersonalMissionMapModelElo(missionSpecification.getMissionMapModelEloUri());
      // create the template elos elo
      elo = tbi.getRepository().retrieveELO(missionSpecification.getTemplateElosEloUri());
      metadata = tbi.getRepository().addForkedELO(elo);
      tbi.getELOFactory().updateELOWithResult(elo, metadata);
      templateElosElo = new TemplateElosElo(elo, tbi);

      def missionRuntimeElo = new MissionRuntimeElo(tbi.getELOFactory().createELO(), tbi);
      missionRuntimeElo.setTitle(missionSpecificationElo.getTitle());
      missionRuntimeElo.setDescription(missionSpecificationElo.getDescription());
      missionRuntimeElo.setMissionRunning(userName);
      missionRuntimeElo.setMissionSpecificationElo(missionSpecificationElo.getUri());
      missionRuntimeElo.getTypedContent().setMissionSpecificationEloUri(missionSpecificationElo.getUri());
      missionRuntimeElo.getTypedContent().setMissionMapModelEloUri(personalMissionMapModelElo.getUri());
      missionRuntimeElo.getTypedContent().setEloToolConfigsEloUri(eloToolConfigsElo.getUri());
      missionRuntimeElo.getTypedContent().setTemplateElosEloUri(templateElosElo.getUri());
      metadata = tbi.getRepository().addNewELO(missionRuntimeElo.getUpdatedElo());
      tbi.getELOFactory().updateELOWithResult(missionRuntimeElo.getElo(), metadata);
      startMission(MissionRunConfigs {
         tbi: tbi
         missionRuntimeElo: missionRuntimeElo
         eloToolConfigsElo: eloToolConfigsElo
         missionMapModel: missionMapModel
         templateElosElo: templateElosElo
      });
   }

   function continueMission(missionRuntimeElo: MissionRuntimeElo) {
      if (missionRuntimeElo.getTypedContent().getMissionMapModelEloUri() != null) {
         def elo = tbi.getRepository().retrieveELO(missionRuntimeElo.getTypedContent().getMissionMapModelEloUri());
         missionMapModel = MissionModelXml.convertToMissionModel(elo.getContent().getXmlString());
      }
      if (missionRuntimeElo.getTypedContent().getEloToolConfigsEloUri() != null) {
         def elo = tbi.getRepository().retrieveELO(missionRuntimeElo.getTypedContent().getEloToolConfigsEloUri());
         eloToolConfigsElo = new EloToolConfigsElo(elo, tbi);
      }
      if (missionRuntimeElo.getTypedContent().getTemplateElosEloUri() != null) {
         def elo = tbi.getRepository().retrieveELO(missionRuntimeElo.getTypedContent().getTemplateElosEloUri());
         templateElosElo = new TemplateElosElo(elo, tbi);
      }
      startMission(MissionRunConfigs {
         tbi: tbi
         missionRuntimeElo: missionRuntimeElo
         eloToolConfigsElo: eloToolConfigsElo
         missionMapModel: missionMapModel
         templateElosElo: templateElosElo
      });
   }

   function startBlankMission(): Void {
      def missionRuntimeElo = new MissionRuntimeElo(tbi.getELOFactory().createELO(), tbi);
      missionRuntimeElo.setTitle(##"Empty mission");
      missionRuntimeElo.setMissionRunning(userName);
      missionRuntimeElo.getTypedContent().setMissionSpecificationEloUri(null);
      missionRuntimeElo.getTypedContent().setMissionMapModelEloUri(null);
      missionRuntimeElo.getTypedContent().setEloToolConfigsEloUri(null);
      var metadata = tbi.getRepository().addNewELO(missionRuntimeElo.getUpdatedElo());
      tbi.getELOFactory().updateELOWithResult(missionRuntimeElo.getElo(), metadata);
      startMission(MissionRunConfigs {
         tbi: tbi
         missionRuntimeElo: missionRuntimeElo
         eloToolConfigsElo: null
         missionMapModel: null
         templateElosElo: null
      });
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

   function createPersonalMissionMapModelElo(missionMapModelEloUri: URI): IELO {
      def missionMapModelElo = tbi.getRepository().retrieveELO(missionMapModelEloUri);
      missionMapModel = MissionModelXml.convertToMissionModel(missionMapModelElo.getContent().getXmlString());
      makePersonalMissionMapModel(missionMapModel);
      missionMapModelElo.getContent().setXmlString(MissionModelXml.convertToXml(missionMapModel));
      missionMapModelElo.getMetadata().getMetadataValueContainer(missionRunningKey).setValue(userName);
      //      missionMapModelElo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(MissionEloType.MISSION_MAP_MODEL.getType());
      missionMapModelElo.getMetadata().getMetadataValueContainer(missionIdKey).setValue(missionMapModel.id);
      var metadata = tbi.getRepository().addForkedELO(missionMapModelElo);
      tbi.getELOFactory().updateELOWithResult(missionMapModelElo, metadata);
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
