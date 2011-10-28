   /*
 * SimpleScyDesktopEloSaver.fx
 *
 * Created on 26-feb-2010, 12:02:24
 */

package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import eu.scy.client.desktop.scydesktop.ScyToolActionLogger;
import eu.scy.client.desktop.scydesktop.config.Config;
import eu.scy.client.desktop.scydesktop.scywindows.NewTitleGenerator;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.scydesktop.tools.EloSaver;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import eu.scy.client.desktop.scydesktop.tools.MyEloChanged;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import eu.scy.client.desktop.scydesktop.tools.ScyTool;
import java.awt.image.BufferedImage;
import eu.scy.client.desktop.desktoputils.art.ArtSource;
import javafx.geometry.BoundingBox;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.design.EloSaveAsMixin;
import eu.scy.common.scyelo.EloFunctionalRole;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;
import org.apache.log4j.Logger;
import roolo.elo.api.IMetadataKey;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.design.MultiLanguageAuthorSaveAsNodeDesign;
import eu.scy.client.desktop.desktoputils.art.ImageLoader;
import eu.scy.common.scyelo.ScyElo;
import java.util.concurrent.CountDownLatch;
import javafx.lang.FX;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.layout.Resizable;
import javafx.util.StringLocalizer;
import eu.scy.client.desktop.desktoputils.ImageUtils;
import eu.scy.client.desktop.desktoputils.XFX;
import eu.scy.client.desktop.desktoputils.art.AnimationTiming;
import eu.scy.client.desktop.desktoputils.art.EloIcon;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.desktoputils.i18n.Composer;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.design.SimpleSaveAsNodeDesign;
import eu.scy.client.desktop.scydesktop.scywindows.window.ProgressOverlay;
import eu.scy.client.desktop.scydesktop.scywindows.window.StandardScyWindow;
import eu.scy.client.desktop.scydesktop.tooltips.impl.BubbleShower;
import eu.scy.client.desktop.scydesktop.tooltips.impl.SimpleTooltipManager;
import eu.scy.client.desktop.scydesktop.tooltips.impl.TextBubble;
import java.awt.EventQueue;
import java.lang.Exception;
import java.lang.IllegalArgumentException;
import java.lang.Object;
import java.lang.String;
import java.lang.System;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;
import roolo.elo.api.exceptions.ELONotLastVersionException;
import roolo.elo.metadata.keys.SocialTags;
import eu.scy.client.desktop.scydesktop.ScyDesktop;

/**
 * @author sikken
 */
public class FunctionalRoleContainer {

   public var functionalRole: EloFunctionalRole;
   var displayText: String;

   public override function toString(): String {
      displayText
   }

}
def resourceBundleWrapper = new ResourceBundleWrapper(SimpleScyDesktopEloSaver.class);
def emptyFunctionalRoleContainer = FunctionalRoleContainer {
           functionalRole: null
           displayText: ##"None"
        }

public class SimpleScyDesktopEloSaver extends EloSaver {

   def logger = Logger.getLogger(this.getClass());
   public var newTitleGenerator: NewTitleGenerator;
   public var myEloChanged: MyEloChanged;
   public var toolBrokerAPI: ToolBrokerAPI on replace {
              eloFactory = toolBrokerAPI.getELOFactory();
              repository = toolBrokerAPI.getRepository();
           };
   public var repository: IRepository;
   public var metadataTypeManager: IMetadataTypeManager;
   public var eloFactory: IELOFactory;
   public var titleKey: IMetadataKey;
   public var technicalFormatKey: IMetadataKey;
   public var window: ScyWindow;
   public var config: Config;
   public var windowStyler: WindowStyler;
   public var scyToolActionLogger: ScyToolActionLogger;
   public var authorMode = false;
   public var functionalRoles: EloFunctionalRole[];
   public var loginName: String;
   public-init var imageLoader: ImageLoader;
   public-init var scyDesktop: ScyDesktop;
   var socialtagsKey = config.getMetadataTypeManager().getMetadataKey("socialTags");
//   def authorKey = config.getMetadataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR);
   var dateFirstUserSaveKey = config.getMetadataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.DATE_FIRST_USER_SAVE);
   var creatorKey: IMetadataKey = config.getMetadataTypeManager().getMetadataKey(ScyRooloMetadataKeyIds.CREATOR);
   var functionalRoleContainers: FunctionalRoleContainer[];

   init {
      functionalRoleContainers = for (functionalRole in functionalRoles) {
                 FunctionalRoleContainer {
                    functionalRole: functionalRole
                    displayText: resourceBundleWrapper.getString(functionalRole.toString())
                 }
              }
      insert emptyFunctionalRoleContainer before functionalRoleContainers[0];
   }

   public override function toString(): String {
      "SimpleScyDesktopEloSaver\{myEloChanged={myEloChanged}\}"
   }

   public override function eloSaveAs(elo: IELO, eloSaverCallBack: EloSaverCallBack): Void {
      waitIfPreviousSaveStillBusyAndStartSave();
      startEloSaveAs(elo, eloSaverCallBack);
   }

   function startEloSaveAs(elo: IELO, eloSaverCallBack: EloSaverCallBack): Void {
      var forking = elo.getUri() != null;
      var currentEloTitle = elo.getMetadata().getMetadataValueContainer(titleKey).getValue() as String;
      var suggestedEloTitle = currentEloTitle;
      if (suggestedEloTitle == null) {
         suggestedEloTitle = window.title;
      }
      if (forking) {
         suggestedEloTitle = "Copy of {suggestedEloTitle}";
      }
      showEloSaveAsPanel(elo, suggestedEloTitle, true, false, eloSaverCallBack);
   }

   var eloSaveAsPanel: EloSaveAsMixin;

   function showEloSaveAsPanel(elo: IELO, suggestedEloTitle: String, isMyEloTo: Boolean, authorUpdate: Boolean, eloSaverCallBack: EloSaverCallBack): Void {
      def scyElo = new ScyElo(elo, config.getToolBrokerAPI());
      if (authorMode) {
         eloSaveAsPanel = MultiLanguageAuthorSaveAsNodeDesign {
                    tbi: toolBrokerAPI
                    saveAction: saveAsAction
                    cancelAction: cancelAction
                    elo: elo
                    scyElo: scyElo
                    myElo: isMyEloTo
                    eloSaverCallBack: eloSaverCallBack
                    authorUpdate: authorUpdate
                 }
      } else {
         eloSaveAsPanel = SimpleSaveAsNodeDesign {
                    saveAction: saveAsAction
                    cancelAction: cancelAction
                    elo: elo
                    scyElo: scyElo
                    myElo: isMyEloTo
                    eloSaverCallBack: eloSaverCallBack
                 }
      }
      eloSaveAsPanel.setTitle(suggestedEloTitle);
      eloSaveAsPanel.setFunctionalRoleContainers(functionalRoleContainers);
      eloSaveAsPanel.setFunctionalRole(scyElo.getFunctionalRole());
      var eloIcon: EloIcon;
      var windowColorScheme: WindowColorScheme;
      if (window.scyElo != null) {
         eloIcon = windowStyler.getScyEloIcon(window.scyElo);
         windowColorScheme = windowStyler.getWindowColorScheme(window.scyElo);
      } else {
         eloIcon = windowStyler.getScyEloIcon(window.eloType);
         windowColorScheme = windowStyler.getWindowColorScheme(window.eloType);
      }
      eloIcon.selected = true;
      Composer.localizeDesign(eloSaveAsPanel.getDesignNodes(), StringLocalizer {});
      eloSaveAsPanel.modalDialogBox = ModalDialogBox {
                 content: Group {
                    content: eloSaveAsPanel.getDesignNodes()
                 }
                 //            targetScene: window.scene
                 title: if (authorUpdate) ##"Edit title" else ##"Enter title"
                 eloIcon: eloIcon
                 windowColorScheme: windowColorScheme
              }
      // delay the positioning long enough so that it will done after the things are placed on screen
      // but before the user actually sees the buttons shifting
      XFX.runActionAfter(eloSaveAsPanel.correctButtonPositions, 500ms);
   }

   function saveAsAction(eloSaveAsPanel: EloSaveAsMixin, showBubbleAfterSaving: Boolean): Void {
      def elo = eloSaveAsPanel.elo;
      def scyElo = eloSaveAsPanel.scyElo;
      addThumbnail(scyElo);

      ProgressOverlay.startShowWorking();

      XFX.runActionInBackgroundAndCallBack(function(): Object {
         updateTags(elo);
         eloSaveAsPanel.setTitleAndLanguagesInElo();
         scyElo.setFunctionalRole(eloSaveAsPanel.getFunctionalRole());
         def doSaveAs = function(): Void {
                    scyElo.setDateFirstUserSave(System.currentTimeMillis());
                    if (elo.getUri() != null) {
                       if (scyElo.getAuthors().size() > 1) {
                          scyElo.setAuthor(loginName);
                          FX.deferAction(function(): Void {
                             window.windowManager.scyDesktop.uninstallCollaborationTools(window);
                             window.ownershipManager.update();
                          });
                       }
                       scyElo.saveAsForkedElo();
                    } else {
                       scyElo.setCreator(loginName);
                       scyElo.saveAsNewElo();
                    }
                 }
         if (eloSaveAsPanel.authorUpdate) {
            // the call is  caused by an update action, so that the author can modify the titles
            // so do not create a new elo, but do just an update
            try {
               scyElo.updateElo();
            } catch (e: ELONotLastVersionException) {
               logger.error("unexpected ELONotLastVersionException for elo: {e.getURI()}, now doing a save as");
               doSaveAs();
            }
         } else {
            doSaveAs();
         }
         if (eloSaveAsPanel.myElo or eloSaveAsPanel.authorUpdate) {
            myEloChanged.myEloChanged(scyElo);
         }
         scyToolActionLogger.eloSaved(elo);
         return null;
      }, function(o: Object) {
         try {
            def elo = eloSaveAsPanel.elo;
            eloSaveAsPanel.eloSaverCallBack.eloSaved(elo);
            eloSaveAsPanel.modalDialogBox.close();
         } finally {
            saveEnded();
            ProgressOverlay.stopShowWorking();
         }
         if (showBubbleAfterSaving) {
            showEloSaved();
         }
      });
   }

   function cancelAction(eloSaveAsPanel: EloSaveAsMixin): Void {
      try {
         eloSaveAsPanel.eloSaverCallBack.eloSaveCancelled(eloSaveAsPanel.elo);
         eloSaveAsPanel.modalDialogBox.close();
      } finally {
         saveEnded();
      }
   }

   function updateTags(elo: IELO): Void {
      if (elo.getUri() == null) {
         // the elo is not yet stored, so there is nothing to update
         return
      }
      XFX.runActionInBackground(function() {
         var lastestElo = repository.retrieveELOLastVersion(elo.getUri());
         var latestMetadata: IMetadata = lastestElo.getMetadata();
         var mvc = latestMetadata.getMetadataValueContainer(socialtagsKey);
         var st: SocialTags = mvc.getValue() as SocialTags;
         if (st == null) {
            st = new SocialTags();
            mvc.setValue(st);
         }
         var currentMetadata: IMetadata = elo.getMetadata();
         currentMetadata.getMetadataValueContainer(socialtagsKey).setValue(st);
      }, "UpdateLatestELOForSocialTagUpdateThread");
   }

   public override function eloUpdate(elo: IELO, eloSaverCallBack: EloSaverCallBack): Void {
      if (elo == null) {
         throw new IllegalArgumentException("elo may not be null");
      }

      waitIfPreviousSaveStillBusyAndStartSave();
      if (elo.getUri() == null or elo.getUri().toString().length() == 0) {
         startEloSaveAs(elo, eloSaverCallBack);
         return
      }

      def scyElo = new ScyElo(elo, config.getToolBrokerAPI());
      if (authorMode and not window.isQuiting) {
         // force a save as dialog box, so that new titles can entered or existing ones modified
         showEloSaveAsPanel(elo, scyElo.getTitle(), true, true, eloSaverCallBack);
         return
      }

      def isMyEloTo = scyElo.getAuthors().contains(config.getToolBrokerAPI().getLoginUserName());
      if (not isMyEloTo and not window.isQuiting) {
         startEloSaveAs(elo, eloSaverCallBack);
         return
      }

      ProgressOverlay.startShowWorking();

      addThumbnail(scyElo);
      var eloUpdated = false;
      def doEloUpdate = function(): Void {
                 updateTags(elo);
                 // it is (also) my elo
                 var dateFirstUserSaveSet = false;
                 var creatorSet = false;
                 if (scyElo.getDateFirstUserSave() == null) {
                    scyElo.setDateFirstUserSave(System.currentTimeMillis());
                    dateFirstUserSaveSet = true;
                 }
                 if (scyElo.getCreator() == null) {
                    scyElo.setCreator(loginName);
                    creatorSet = true;
                 }
                 if (isMyEloTo) {
                    try {
                       scyElo.updateElo();
                       scyToolActionLogger.eloSaved(elo);
                       eloUpdated = true;
                    } catch (e: ELONotLastVersionException) {
                       logger.error("unexpected ELONotLastVersionException for elo: {e.getURI()}, now doing a save as");
                       if (dateFirstUserSaveSet) {
                          scyElo.getMetadata().deleteMetatadata(dateFirstUserSaveKey);
                       }
                       if (creatorSet) {
                          scyElo.getMetadata().deleteMetatadata(creatorKey);
                       }
                       FX.deferAction(function(): Void {
                          startEloSaveAs(elo, eloSaverCallBack);
                       });
                    }
                 } else {
                    // it is not mine, but as this window is being quit, i may not ask the user anything
                    scyElo.saveAsForkedElo();
                 }
              }
      def finishEloUpdate = function(): Void {
                 if (eloUpdated) {
                    try {
                       myEloChanged.myEloChanged(scyElo);
                       eloSaverCallBack.eloSaved(scyElo.getElo());
                       showEloSaved();
                    } finally {
                       ProgressOverlay.stopShowWorking();
                       saveEnded();
                    }
                 }
              }
      // run it only in a background thread, if we are ruuning on the EDT
      // during the close of scy-lab, all save actions are done in a background thread
      if (scyDesktop.quiting){
         try {
            doEloUpdate();
            myEloChanged.myEloChanged(scyElo);
         } finally {
            saveEnded()
         }
      } else if (EventQueue.isDispatchThread()) {
         XFX.runActionInBackgroundAndCallBack(doEloUpdate, function(o: Object): Void {
            finishEloUpdate()
         });
      } else {
         try {
            doEloUpdate();
         } finally {
            FX.deferAction(finishEloUpdate);
         }
      }
   }

   public override function otherEloSaveAs(elo: IELO, eloSaverCallBack: EloSaverCallBack): Void {
      if (elo == null) {
         throw new IllegalArgumentException("elo may not be null");
      }
      updateTags(elo);
      var forking = elo.getUri() != null;
      var currentEloTitle = elo.getMetadata().getMetadataValueContainer(titleKey).getValue() as String;
      var suggestedEloTitle = currentEloTitle;
      if (suggestedEloTitle == null) {
         var eloType = elo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue() as String;
         suggestedEloTitle = newTitleGenerator.generateNewTitleFromType(eloType);
      }
      if (forking) {
         suggestedEloTitle = "Copy of {suggestedEloTitle}";
      }
      showEloSaveAsPanel(elo, suggestedEloTitle, false, false, eloSaverCallBack);
   }

   function addThumbnail(scyElo: ScyElo): Void {
      var thumbnailImage: BufferedImage;
      if (window.scyContent instanceof ScyTool) {
         try {
            thumbnailImage = (window.scyContent as ScyTool).getThumbnail(ArtSource.thumbnailWidth, ArtSource.thumbnailHeight);
         } catch (e: Exception) {
            logger.error("exception in getThumbnail() from window.scyContent {window.scyContent.getClass().getName()}", e);
         }
      }
      if (thumbnailImage == null) {
         // no thumbnail returned by the tool, try to use an image of window content
         def bounds = BoundingBox {
                    width: ArtSource.thumbnailWidth
                    height: ArtSource.thumbnailHeight
                 }
         try {
            if (window.scyContent instanceof Resizable){
               def resizableScyContent = window.scyContent as Resizable;
               if (resizableScyContent.width<=0){
                  def newWidth = ArtSource.thumbnailWidth;
                  logger.warn("changing {window.scyContent.getClass().getName()}.wdith from {resizableScyContent.width} to {newWidth}");
                  resizableScyContent.width = newWidth
               }
               if (resizableScyContent.height<=0){
                  def newHeight = ArtSource.thumbnailHeight;
                  logger.warn("changing {window.scyContent.getClass().getName()}.height from {resizableScyContent.height} to {newHeight}");
                  resizableScyContent.height = newHeight
               }
            }
            thumbnailImage = ImageUtils.nodeToImage(window.scyContent, bounds);
            window.requestLayout();
         } catch (e: Exception) {
            logger.error("failed to create thumbnail image from window.scyContent {window.scyContent.getClass().getName()}", e);
         }
      }
      if (thumbnailImage!=null){
         scyElo.setThumbnail(thumbnailImage);
      } else {
         logger.warn("there is no thumbnail image to write for scyElo {scyElo.getUri()}");
      }

   }

   function showEloSaved(): Void {
      if (not window.isQuiting) {
         def showEloSavedMassegaeTime = 1s;
         var targetNode: Node = window;
         if (window instanceof StandardScyWindow) {
            targetNode = (window as StandardScyWindow).windowTitleBar.eloIcon;
         }

         def eloSavedBubbled = TextBubble {
                    bubbleText: ##"ELO saved"
                    windowColorScheme: window.windowColorScheme
                    targetNode: targetNode
                 }
         BubbleShower {
            //         simpleBubbleManager: this
            bubble: eloSavedBubbled
            tooltipGroup: SimpleTooltipManager.tooltipGroup
            bubbleContent: eloSavedBubbled.getBubbleContent()
            windowColorScheme: eloSavedBubbled.windowColorScheme
            sourceNode: eloSavedBubbled.targetNode
            showArrow: false
            startAppearingTime: 10ms
            fullAppearingTime: AnimationTiming.appearTime
            startDisappearingTime: AnimationTiming.appearTime + showEloSavedMassegaeTime
            fullDisappearingTime: AnimationTiming.appearTime + showEloSavedMassegaeTime + AnimationTiming.disappearTime
         }
      }
   }

   var busyLatch = new CountDownLatch(0);

   function waitIfPreviousSaveStillBusyAndStartSave(): Void {
      busyLatch.await();
      busyLatch = new CountDownLatch(1);
   }

   function saveEnded(): Void {
      busyLatch.countDown();
   }

}
