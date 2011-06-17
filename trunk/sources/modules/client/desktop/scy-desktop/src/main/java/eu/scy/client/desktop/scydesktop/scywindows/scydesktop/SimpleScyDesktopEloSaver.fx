   /*
 * SimpleScyDesktopEloSaver.fx
 *
 * Created on 26-feb-2010, 12:02:24
 */

package eu.scy.client.desktop.scydesktop.scywindows.scydesktop;

import javafx.scene.Group;
import eu.scy.client.desktop.scydesktop.ScyToolActionLogger;
import eu.scy.client.desktop.scydesktop.config.Config;
import eu.scy.client.desktop.desktoputils.art.EloIcon;
import eu.scy.client.desktop.scydesktop.scywindows.NewTitleGenerator;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.design.SimpleSaveAsNodeDesign;
import eu.scy.client.desktop.scydesktop.tools.EloSaver;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import eu.scy.client.desktop.scydesktop.tools.MyEloChanged;
import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;
import eu.scy.client.desktop.desktoputils.i18n.Composer;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.tools.ScyTool;
import java.awt.image.BufferedImage;
import eu.scy.client.desktop.desktoputils.art.ArtSource;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.desktoputils.ImageUtils;
import javafx.geometry.BoundingBox;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.design.EloSaveAsMixin;
import eu.scy.common.scyelo.EloFunctionalRole;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import java.lang.IllegalArgumentException;
import java.lang.System;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import roolo.elo.api.exceptions.ELONotLastVersionException;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;
import org.apache.log4j.Logger;
import roolo.elo.api.IMetadataKey;
import roolo.elo.metadata.keys.SocialTags;
import roolo.elo.api.IMetadata;
import javafx.util.StringLocalizer;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.design.MultiLanguageAuthorSaveAsNodeDesign;
import eu.scy.client.desktop.desktoputils.art.ImageLoader;

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

   public override function toString():String{
      "SimpleScyDesktopEloSaver\{myEloChanged={myEloChanged}\}"
   }

   public override function eloSaveAs(elo: IELO, eloSaverCallBack: EloSaverCallBack): Void {
      var forking = elo.getUri() != null;
      var currentEloTitle = elo.getMetadata().getMetadataValueContainer(titleKey).getValue() as String;
      var suggestedEloTitle = currentEloTitle;
      if (suggestedEloTitle == null) {
         suggestedEloTitle = window.title;
      }
      if (forking) {
         suggestedEloTitle = "Copy of {suggestedEloTitle}";
      }
      showEloSaveAsPanel(elo, suggestedEloTitle, true, eloSaverCallBack);
   }

   function showEloSaveAsPanel(elo: IELO, suggestedEloTitle: String, myElo: Boolean, eloSaverCallBack: EloSaverCallBack): Void {
      var eloSaveAsPanel: EloSaveAsMixin;
      def scyElo = new ScyElo(elo, config.getToolBrokerAPI());
      if (authorMode) {
         eloSaveAsPanel = MultiLanguageAuthorSaveAsNodeDesign {
                    tbi: toolBrokerAPI
                    saveAction: saveAction
                    cancelAction: cancelAction
                    elo: elo
                    scyElo: scyElo
                    myElo: myElo
                    eloSaverCallBack: eloSaverCallBack
                 }
      } else {
         eloSaveAsPanel = SimpleSaveAsNodeDesign {
                    saveAction: saveAction
                    cancelAction: cancelAction
                    elo: elo
                    scyElo: scyElo
                    myElo: myElo
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
      Composer.localizeDesign(eloSaveAsPanel.getDesignNodes(), StringLocalizer{});
      eloSaveAsPanel.modalDialogBox = ModalDialogBox {
                 content: Group {
                    content: eloSaveAsPanel.getDesignNodes()
                 }
                 //            targetScene: window.scene
                 title: ##"Enter title"
                 eloIcon: eloIcon
                 windowColorScheme: windowColorScheme
              }
   }

   function saveAction(eloSaveAsPanel: EloSaveAsMixin): Void {
      var elo = eloSaveAsPanel.elo;
      updateTags(elo);
      def scyElo = eloSaveAsPanel.scyElo;
      eloSaveAsPanel.setTitleAndLanguagesInElo();
//      scyElo.setTitle(eloSaveAsPanel.getTitle());
      scyElo.setFunctionalRole(eloSaveAsPanel.getFunctionalRole());
      addThumbnail(scyElo);
      scyElo.setDateFirstUserSave(System.currentTimeMillis());
      scyElo.setCreator(loginName);
      if (elo.getUri() != null) {
         scyElo.saveAsForkedElo();
      } else {
         scyElo.saveAsNewElo();
      }
      if (eloSaveAsPanel.myElo) {
         myEloChanged.myEloChanged(scyElo);
      }
      eloSaveAsPanel.eloSaverCallBack.eloSaved(elo);
      scyToolActionLogger.eloSaved(elo);
      eloSaveAsPanel.modalDialogBox.close();
   }

   function cancelAction(eloSaveAsPanel: EloSaveAsMixin): Void {
      eloSaveAsPanel.eloSaverCallBack.eloSaveCancelled(eloSaveAsPanel.elo);
      eloSaveAsPanel.modalDialogBox.close();
   }

   function updateTags(elo: IELO): Void {
      if (elo.getUri() == null) {
         // the elo is not yet stored, so there is nothing to update
         return
      }
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
   }

   public override function eloUpdate(elo: IELO, eloSaverCallBack: EloSaverCallBack): Void {
      if (elo == null) {
         throw new IllegalArgumentException("elo may not be null");
      }
      updateTags(elo);

      if (elo.getUri() != null) {
         def scyElo = new ScyElo(elo, config.getToolBrokerAPI());
         def myElo = scyElo.getAuthors().contains(config.getToolBrokerAPI().getLoginUserName());
         if (myElo or window.isQuiting) {
            // it is (also) my elo
            var dateFirstUserSaveSet = false;
            var creatorSet = false;
            addThumbnail(scyElo);
            if (scyElo.getDateFirstUserSave() == null) {
               scyElo.setDateFirstUserSave(System.currentTimeMillis());
               dateFirstUserSaveSet = true;
            }
            if (scyElo.getCreator() == null) {
               scyElo.setCreator(loginName);
               creatorSet = true;
            }
            if (myElo) {
               try {
                  scyElo.updateElo();
               } catch (e: ELONotLastVersionException) {
                  logger.error("unexpected ELONotLastVersionException for elo: {e.getURI()}, now doing a save as");
                  if (dateFirstUserSaveSet) {
                     scyElo.getMetadata().deleteMetatadata(dateFirstUserSaveKey);
                  }
                  if (creatorSet) {
                     scyElo.getMetadata().deleteMetatadata(creatorKey);
                  }
                  eloSaveAs(elo, eloSaverCallBack);
               }
            } else {
               // it is not my, but as this window is being quit, i may not ask the user anything
               scyElo.saveAsForkedElo();
            }
            myEloChanged.myEloChanged(scyElo);
            eloSaverCallBack.eloSaved(scyElo.getElo());
         } else {
            // it is not my elo, do a save as
            eloSaveAs(elo, eloSaverCallBack);
         }
      } else {
         eloSaveAs(elo, eloSaverCallBack);
      }
      scyToolActionLogger.eloSaved(elo);
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
      showEloSaveAsPanel(elo, suggestedEloTitle, false, eloSaverCallBack);
   }

   function addThumbnail(scyElo: ScyElo): Void {
      var thumbnailImage: BufferedImage;
      if (window.scyContent instanceof ScyTool) {
         thumbnailImage = (window.scyContent as ScyTool).getThumbnail(ArtSource.thumbnailWidth, ArtSource.thumbnailHeight);
      }
      if (thumbnailImage == null) {
         // no thumbnail returned by the tool, try to use an image of window content
         def bounds = BoundingBox {
                    width: ArtSource.thumbnailWidth
                    height: ArtSource.thumbnailHeight
                 }
         thumbnailImage = ImageUtils.nodeToImage(window.scyContent, bounds);
         window.requestLayout();
      }
      scyElo.setThumbnail(thumbnailImage);
   }

}
