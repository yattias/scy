/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.mission;

import org.apache.log4j.Logger;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import javafx.scene.Group;
import eu.scy.client.desktop.desktoputils.EmptyBorderNode;
import javafx.scene.control.ListCell;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionModelFX;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.desktoputils.i18n.Composer;
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
import eu.scy.common.mission.MissionModel;
import eu.scy.client.desktop.scydesktop.scywindows.window.ProgressOverlay;
import eu.scy.client.desktop.desktoputils.XFX;
import javafx.util.Sequences;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.DialogBox;
import javafx.util.StringLocalizer;
import javax.swing.JOptionPane;
import java.lang.Exception;
import eu.scy.client.desktop.scydesktop.login.MissionListCellDisplay;
import eu.scy.common.scyelo.MissionLanguageTitleComparator;

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
   def userRunningMissionKey = tbi.getMetaDataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.USER_RUNNING_MISSION);
   var missionMapModel: MissionModelFX;

   public function locateMission(): Void {
      if (initializer.singleEloMode) {
         startSingleEloMission();
         return;
      }

      if (missions.isEmpty() and initializer.authorMode) {
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
         def defaultMissionRuntimeElo = missions.findMissionRuntimeElo(initializer.defaultMission);
         if (defaultMissionRuntimeElo != null) {
            continueMission(defaultMissionRuntimeElo);
            return;
         }
         def defaultMissionSpecificationElo = missions.findMissionSpecificationElo(initializer.defaultMission);
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
      ProgressOverlay.startShowWorking();
      var missionRuntimeModel: MissionRuntimeModel;
      var missionRuntimeElo: MissionRuntimeElo;
      XFX.runActionInBackgroundAndCallBack(function(): Object {
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
         return missionModel;
      }, function(missionModel): Void {
         missionMapModel = MissionModelFX {
                    tbi: tbi
                    missionModel: missionModel as MissionModel
                    saveUpdatedModel: not initializer.dontUseMissionRuntimeElos
                 }
         startMission(MissionRunConfigs {
            tbi: tbi
            missionRuntimeModel: missionRuntimeModel
            missionMapModel: missionMapModel
         });
         ProgressOverlay.stopShowWorking();
      });

   }

   function continueMission(missionRuntimeElo: MissionRuntimeElo) {
      ProgressOverlay.startShowWorking();
      var missionRuntimeModel: MissionRuntimeModel;
      var missionRuntimeEloToUse = missionRuntimeElo;
      XFX.runActionInBackgroundAndCallBack(function(): Object {
         def lastVersionMissionRuntimeElo = tbi.getRepository().retrieveELOLastVersion(missionRuntimeElo.getUri());
         if (missionRuntimeElo.getUri()!=lastVersionMissionRuntimeElo.getUri()){
            missionRuntimeEloToUse = new MissionRuntimeElo(lastVersionMissionRuntimeElo,tbi);
            logger.error("not last version of mission runtime specified, switching to last version: {missionRuntimeElo.getUri()} -> {missionRuntimeEloToUse.getUri()}");
         }
         injectMissionRuntimeEloInRepository(missionRuntimeEloToUse.getUriFirstVersion());
         missionRuntimeModel = missionRuntimeEloToUse.getMissionRuntimeModel();
         def missionModel = missionRuntimeModel.getMissionModel();
         missionModel.loadMetadata(tbi);
         return missionModel;
      }, function(missionModel): Void {
         missionMapModel = MissionModelFX {
                    tbi: tbi
                    missionModel: missionModel as MissionModel
                    saveUpdatedModel: true
                 }
         startMission(MissionRunConfigs {
            tbi: tbi
            missionRuntimeModel: missionRuntimeModel
            missionMapModel: missionMapModel
         });
         ProgressOverlay.stopShowWorking();
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
      Composer.localizeDesign(eloSaveAsPanel.getDesignRootNodes(), StringLocalizer {});
      eloSaveAsPanel.modalDialogBox = ModalDialogBox {
                 content: Group {
                    content: eloSaveAsPanel.getDesignRootNodes()
                 }
                 title: ##"Enter title"
                 eloIcon: window.eloIcon.clone()
                 windowColorScheme: window.windowColorScheme
              }
   }

   function createBlankMissionAction(eloSaveAsMixin: EloSaveAsMixin): Void {

      XFX.runActionInBackgroundAndCallBack(function(): Object {
         eloSaveAsMixin.modalDialogBox.close();
         def missionRuntimeElo = eloSaveAsMixin.scyElo as MissionRuntimeElo;
         missionRuntimeElo.setTitle(eloSaveAsMixin.getTitle());
         missionRuntimeElo.setUserRunningMission(userName);
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
         missionMapModelElo.getMetadata().getMetadataValueContainer(userRunningMissionKey).setValue(userName);
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
         return new BasicMissionRuntimeModel(missionRuntimeElo, null, tbi, missionMapModelElo, null, null, runtimeSettingsElo, null);
      }, function(missionRuntimeModel): Void {
         startMission(MissionRunConfigs {
            tbi: tbi
            missionRuntimeModel: missionRuntimeModel as MissionRuntimeModel
            missionMapModel: missionMapModel
         });
         ProgressOverlay.stopShowWorking();
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
      var startedMissions: MissionRuntimeElo[] = Sequences.sort(missions.getMissionRuntimeElosArray(), new MissionLanguageTitleComparator()) as MissionRuntimeElo[];
      askUserForMissionNode.startedMissionListView.items = startedMissions;
      askUserForMissionNode.newMissionListView.cellFactory = missionCellFactory;
      var newMissions: MissionSpecificationElo[] = Sequences.sort(missions.getMissionSpecificationElosArray(), new MissionLanguageTitleComparator()) as MissionSpecificationElo[];
      askUserForMissionNode.newMissionListView.items = newMissions;
      Composer.localizeDesign(askUserForMissionNode.getDesignRootNodes(), StringLocalizer {});
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
                 node: MissionListCellDisplay {
                    mission: bind (listCell.item as ScyElo)
                 }
              }
   }

   function startSingleEloMission() {
      def eloUri = findSingleEloUri();
      def scyElo = ScyElo.loadElo(eloUri, tbi);
      if (scyElo != null) {
         var missionRuntimeModel: MissionRuntimeModel;
         def missionRuntimeEloUri = scyElo.getMissionRuntimeEloUri();
         if (missionRuntimeEloUri != null) {
            injectMissionRuntimeEloInRepository(missionRuntimeEloUri);
            def missionRuntimeElo = MissionRuntimeElo.loadLastVersionElo(scyElo.getMissionRuntimeEloUri(), tbi);
            missionRuntimeModel = missionRuntimeElo.getMissionRuntimeModel();
         }
         startMission(MissionRunConfigs {
            tbi: tbi
            missionRuntimeModel: missionRuntimeModel
            scyEloToLoad: scyElo
         });
      } else {
         logger.warn("Cannot find product with uri: {eloUri}");
         DialogBox.showMessageDialog(String.format(##"Cannot find product with url:%n%s%nSCY-Lab will quit.", eloUri), ##"Cannot find product", null, function(): Void {
            FX.exit();
         }, null);
      }
   }

   function findSingleEloUri(): URI {
      if (initializer.singleEloUri.equalsIgnoreCase("askuser")) {
         var uriEntered = false;
         var enteredUriString = "";
         var message = ##"Please enter the product url:";
         def uriStart = "roolo://";
         while (not uriEntered) {
            enteredUriString = JOptionPane.showInputDialog(null, message, enteredUriString);
            if (enteredUriString == "") {
               FX.exit();
            } else if (enteredUriString.startsWith(uriStart)) {
               try {
                  return new URI(enteredUriString)
               } catch (e: Exception) {
                  println("e: {e}");
                  message = String.format(##"The url is not valid: %s%nPlease enter valid product url:", e);
               }
            } else {
               message = String.format(##"The url is not valid: it should start with %s%nPlease enter valid product url:", uriStart);
            }
         }
         return null;
      } else {
         return new URI(initializer.singleEloUri);
      }
   }

}
