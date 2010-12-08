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
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelFX;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.utils.i18n.Composer;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.Initializer;
import eu.scy.common.mission.RuntimeSettingsElo;
import eu.scy.client.desktop.scydesktop.hacks.RepositoryWrapper;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.design.SimpleSaveAsNodeDesign;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.design.EloSaveAsMixin;
import eu.scy.common.mission.MissionModelElo;
import eu.scy.common.mission.MissionRuntimeModel;
import eu.scy.common.mission.impl.BasicMissionRuntimeModel;

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
   def missionRunningKey = tbi.getMetaDataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.MISSION_RUNNING);
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
      if (initializer.defaultMission != "") {
         // a default mission is specified, try to use that
         def defaultMissionRuntimeElo = missions.findMissionRuntimeEloByTitle(initializer.defaultMission);
         if (defaultMissionRuntimeElo != null) {
            continueMission(defaultMissionRuntimeElo);
            return;
         }
         def defaultMissionSpecificationElo = missions.findMissionSpecificationEloByTitle(initializer.defaultMission);
         if (defaultMissionSpecificationElo != null) {
            startNewMission(defaultMissionSpecificationElo);
            return;
         }
         logger.info("failed to find the default mission: {initializer.defaultMission}");
      }

      // multiple missions possible, ask the user which one
      askUserForMission(missions);
   }

   function startNewMission(missionSpecificationElo: MissionSpecificationElo) {
      var missionRuntimeModel: MissionRuntimeModel;
      var missionRuntimeElo: MissionRuntimeElo;
      def missionManagement = missionSpecificationElo.getMissionManagement();
      if (not initializer.dontUseMissionRuntimeElos) {
         // this is the normal way
         logger.info("prepare new mission runtime for {missionSpecificationElo.getUri()}");
         missionRuntimeModel = missionManagement.createMissionRuntimeModelElos(userName);
      } else {
         // this is a special way, try not to change the elos while running,
         // this is meant for when an author is checking the entered mission
         // try to use the specificatins elos directly
         // or do not save new elos
         logger.info("starting mission (without runtime elos) from {missionSpecificationElo.getUri()}");
         missionRuntimeModel = missionManagement.getMissionRuntimeModelElosOnSpecifiaction(userName);
      }
      def missionModel = missionRuntimeModel.getMissionModel();
      missionModel.loadMetadata(tbi);
      missionMapModel = MissionModelFX {
            tbi: tbi
            missionModel: missionModel
            saveUpdatedModel: not initializer.dontUseMissionRuntimeElos
         }
      startMission(MissionRunConfigs {
         tbi: tbi
         missionRuntimeModel: missionRuntimeModel
         missionMapModel: missionMapModel
      });
   }

   function continueMission(missionRuntimeElo: MissionRuntimeElo) {
      injectMissionRuntimeEloInRepository(missionRuntimeElo.getUriFirstVersion());
      def missionRuntimeModel = missionRuntimeElo.getMissionRuntimeModel();
      def missionModel = missionRuntimeModel.getMissionModel();
      missionModel.loadMetadata(tbi);
      missionMapModel = MissionModelFX {
            tbi: tbi
            missionModel: missionModel
            saveUpdatedModel: true
         }

      startMission(MissionRunConfigs {
         tbi: tbi
         missionRuntimeModel: missionRuntimeModel
         missionMapModel: missionMapModel
      });
   }

   function startBlankMission(): Void {
      askUserForBlankMissionName(MissionRuntimeElo.createElo(tbi));
   }

   function askUserForBlankMissionName(missionRuntimeElo: MissionRuntimeElo) {
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

   function createBlankMissionAction(eloSaveAsMixin: EloSaveAsMixin): Void {
      eloSaveAsMixin.modalDialogBox.close();
      def missionRuntimeElo = eloSaveAsMixin.scyElo as MissionRuntimeElo;
      missionRuntimeElo.setTitle(eloSaveAsMixin.getTitle());
      missionRuntimeElo.setMissionRunning(userName);
      if (not initializer.dontUseMissionRuntimeElos) {
         missionRuntimeElo.saveAsNewElo();
      }
      injectMissionRuntimeEloInRepository(missionRuntimeElo.getUriFirstVersion());

      def missionMapModelElo = MissionModelElo.createElo(tbi);
      missionMapModel = MissionModelFX {
            tbi: tbi
            missionModel: missionMapModelElo.getMissionModel()
         }
      missionMapModelElo.setTitle(missionRuntimeElo.getTitle());
      //      missionMapModelElo.set(userName);
      missionMapModelElo.getMetadata().getMetadataValueContainer(missionRunningKey).setValue(userName);
      if (not initializer.dontUseMissionRuntimeElos) {
         missionMapModelElo.saveAsNewElo();
         missionMapModel.saveUpdatedModel = true;
      }

      def runtimeSettingsElo = RuntimeSettingsElo.createElo(tbi);
      runtimeSettingsElo.setTitle(missionRuntimeElo.getTitle());
      if (not initializer.dontUseMissionRuntimeElos) {
         runtimeSettingsElo.saveAsNewElo();
      }

      missionRuntimeElo.getTypedContent().setMissionSpecificationEloUri(null);
      missionRuntimeElo.getTypedContent().setMissionMapModelEloUri(missionMapModelElo.getUri());
      missionRuntimeElo.getTypedContent().setEloToolConfigsEloUri(null);
      missionRuntimeElo.getTypedContent().setRuntimeSettingsEloUri(runtimeSettingsElo.getUri());
      if (not initializer.dontUseMissionRuntimeElos) {
         missionRuntimeElo.updateElo();
      }
      def missionRuntimeModel = new BasicMissionRuntimeModel(missionRuntimeElo, null, tbi, missionMapModelElo, null, null, runtimeSettingsElo);
      startMission(MissionRunConfigs {
         tbi: tbi
         missionRuntimeModel: missionRuntimeModel
         missionMapModel: missionMapModel
      });
   }

   function cancelBlankMissionAction(eloSaveAsMixin: EloSaveAsMixin): Void {
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

}
