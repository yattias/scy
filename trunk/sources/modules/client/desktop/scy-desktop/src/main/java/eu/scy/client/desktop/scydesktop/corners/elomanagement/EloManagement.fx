/*
 * EloManagement.fx
 *
 * Created on 28-feb-2010, 13:49:32
 */
package eu.scy.client.desktop.scydesktop.corners.elomanagement;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindowControl;
import eu.scy.client.desktop.scydesktop.tooltips.TooltipManager;
import org.apache.log4j.Logger;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataKey;
import javafx.scene.layout.VBox;
import eu.scy.client.desktop.desktoputils.art.EloIcon;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;
import javafx.util.Sequences;
import java.net.URI;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import java.lang.System;
import roolo.elo.metadata.keys.Contribute;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.ImageWindowStyler;
import eu.scy.client.desktop.desktoputils.i18n.Composer;
import eu.scy.client.desktop.desktoputils.art.WindowColorScheme;
import eu.scy.client.desktop.desktoputils.FpsDisplay;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.scydesktop.uicontrols.EloIconButton;
import javafx.util.StringLocalizer;
import java.util.LinkedHashMap;
import java.util.Locale;
import roolo.elo.api.IELO;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.tools.search.EloSearchNode;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.tools.search.ScySearchResult;
import eu.scy.client.desktop.scydesktop.tools.search.ScySearchResultTitleComparator;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleLayer;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleKey;
import eu.scy.client.desktop.desktoputils.XFX;

/**
 * @author sikken
 */
public class EloManagement extends CustomNode {

   def logger = Logger.getLogger(this.getClass());
   public var scyDesktop: ScyDesktop;
   public var scyWindowControl: ScyWindowControl on replace { archiver.scyWindowControl = scyWindowControl };
   public var repository: IRepository;
   public var metadataTypeManager: IMetadataTypeManager;
   public var titleKey: IMetadataKey;
   public var technicalFormatKey: IMetadataKey;
   public var eloFactory: IELOFactory;
   public var templateEloUris: URI[];
   public var tooltipManager: TooltipManager;
   public var windowStyler: WindowStyler;
   public var buttonSize = -1.0;
   public var buttonActionScheme = -1;
   def showCreateBlankElo = scyDesktop.initializer.authorMode;
   def authorKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR);
   def creatorKey = metadataTypeManager.getMetadataKey(ScyRooloMetadataKeyIds.CREATOR);
   def templateKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TEMPLATE);
   def userId = scyDesktop.config.getToolBrokerAPI().getLoginUserName();
   def tbi = scyDesktop.config.getToolBrokerAPI();
   var newFromEloTemplateButton: EloIconButton;
   var searcher: Searcher;
   var createBlankEloButton: EloIconButton;
   var archiver: Archiver;
   public-read var searchWrapper: SearchWrapper;

   init {
      if (eloFactory == null) {
         eloFactory = scyDesktop.config.getEloFactory();
      }
      if (scyWindowControl == null) {
         scyWindowControl = scyDesktop.scyWindowControl;
         archiver.scyWindowControl = scyWindowControl;
      }
      if (tooltipManager == null) {
         tooltipManager = scyDesktop.tooltipManager;
      }
      if (windowStyler == null) {
         windowStyler = scyDesktop.windowStyler;
      }
      if (templateEloUris == null) {
         templateEloUris = scyDesktop.templateEloUris;
      }
      if (buttonSize < 0) {
         buttonSize = scyDesktop.desktopButtonSize;
      }
      if (buttonActionScheme < 0) {
         buttonActionScheme = scyDesktop.desktopButtonActionScheme;
      }

      findTemplateEloInformation();
   }

   public override function create(): Node {
      var fpdDisplay: Node;
      if (showCreateBlankElo) {
         fpdDisplay = FpsDisplay {
                 }
      }
      archiver = Archiver {
                 tbi: scyDesktop.config.getToolBrokerAPI()
                 missionMapModel: scyDesktop.missionModelFX
                 scyWindowControl: scyWindowControl
                 eloIcon: windowStyler.getScyEloIcon("archive")
                 buttonSize: buttonSize
                 buttonActionScheme: buttonActionScheme
                 tooltipManager: tooltipManager
                 tooltip: ##"drag icon here, to remove ELO"
              }
      newFromEloTemplateButton = EloIconButton {
                 size: buttonSize
                 eloIcon: windowStyler.getScyEloIcon("new_template")
                 actionScheme: buttonActionScheme
                 action: createNewEloFromTemplateAction
                 tooltipManager: tooltipManager
                 tooltip: ##"create new ELO"
              }
      searcher = Searcher {
                 tbi: scyDesktop.config.getToolBrokerAPI()
                 eloIcon: windowStyler.getScyEloIcon("search")
                 buttonSize: buttonSize
                 buttonActionScheme: buttonActionScheme
                 clickAction: textQuerySearchAction
                 dropAction: eloBasedSearchAction
                 tooltipManager: tooltipManager
                 tooltip: ##"search for ELOs or\ndrag icon here, to start search"
              }
      createBlankEloButton = EloIconButton {
                 size: buttonSize
                 eloIcon: windowStyler.getScyEloIcon("new_author")
                 actionScheme: buttonActionScheme
                 action: createNewBlankEloAction
                 tooltipManager: tooltipManager
                 tooltip: ##"create new blank ELO"
              }
      scyDesktop.dragAndDropManager.addDropTaget(archiver);
      scyDesktop.dragAndDropManager.addDropTaget(searcher);
      createBubbles();
      VBox {
         spacing: 5;
         content: [
            archiver,
            newFromEloTemplateButton,
            searcher,
            if (showCreateBlankElo) {
               createBlankEloButton
            } else {
               null
            }
            fpdDisplay
         ]
      }
   }

   function createBubbles() {
      scyDesktop.bubbleManager.createBubble(archiver, BubbleLayer.DESKTOP, BubbleKey.ARCHIVER_DRAG, archiver.eloIcon.windowColorScheme);
      scyDesktop.bubbleManager.createBubble(newFromEloTemplateButton, BubbleLayer.DESKTOP, BubbleKey.NEW_ELO_FROM_TEMPLATE, newFromEloTemplateButton.eloIcon.windowColorScheme);
      scyDesktop.bubbleManager.createBubble(searcher, BubbleLayer.DESKTOP, BubbleKey.SEARCH_CLICK, searcher.eloIcon.windowColorScheme);
      scyDesktop.bubbleManager.createBubble(searcher, BubbleLayer.DESKTOP, BubbleKey.SEARCH_DRAG, searcher.eloIcon.windowColorScheme);
      if (showCreateBlankElo) {
         scyDesktop.bubbleManager.createBubble(createBlankEloButton, BubbleLayer.DESKTOP, BubbleKey.NEW_BLANK_ELO, searcher.eloIcon.windowColorScheme);
      }
   }

   var eloTemplateUris: URI[];
   var eloTemplateUriDisplays: ScySearchResult[];

   function findTemplateEloInformation() {
      if (templateEloUris != null) {
         for (uri in templateEloUris) {
            insert uri as URI into eloTemplateUris;
         }
      }
      eloTemplateUriDisplays = for (uri in eloTemplateUris) {
                 createScySearchResult(uri)
              }
      eloTemplateUriDisplays = Sequences.sort(eloTemplateUriDisplays, new ScySearchResultTitleComparator()) as ScySearchResult[];
      newFromEloTemplateButton.disable = sizeof eloTemplateUris == 0;
   }

   function createScySearchResult(uri: URI): ScySearchResult {
      //FIXME change this.... dont load metadata into search results
      def scyElo = ScyElo.loadMetadata(uri, tbi);
      def eloIcon: EloIcon = windowStyler.getScyEloIcon(scyElo);
      def scySearchResult = new ScySearchResult(scyElo, 1.0);
      scySearchResult.setEloIcon(eloIcon);
      return scySearchResult;
   }

   function createNewEloFromTemplateAction(): Void {
      newFromEloTemplateButton.turnedOn = true;
      var createNewElo = CreateNewElo {
                 createAction: createNewEloFromTemplate
                 cancelAction: cancelNewElo
              }
      createNewElo.listView.cellFactory = createNewElo.simpleScyEloCellFactory;
      createNewElo.listView.items = eloTemplateUriDisplays;

      createNewElo.label.text = ##"Select template";
      var eloIcon = windowStyler.getScyEloIcon(ImageWindowStyler.generalNew);
      var windowColorScheme = windowStyler.getWindowColorScheme(ImageWindowStyler.generalNew);

      createModalDialog(windowColorScheme, eloIcon, ##"Create new", createNewElo);
   }

   function createNewEloFromTemplate(createNewElo: CreateNewElo): Void {
      var scySearchResult = createNewElo.listView.selectedItem as ScySearchResult;
      if (scySearchResult != null) {
         var eloTemplateUri = scySearchResult.getScyElo().getUri();
         var newElo = repository.retrieveELO(eloTemplateUri);
         if (newElo != null) {
            var titleContainer = newElo.getMetadata().getMetadataValueContainer(titleKey);
            var templateTitle = titleContainer.getValue() as String;
            setTitleAndLanguage(newElo, scyDesktop.newTitleGenerator.generateNewTitleFromName(templateTitle));
            newElo.getMetadata().getMetadataValueContainer(templateKey).setValue("true");
            newElo.getMetadata().getMetadataValueContainer(creatorKey).setValue(new Contribute(tbi.getLoginUserName(), System.currentTimeMillis()));
            var metadata = repository.addForkedELO(newElo);
            eloFactory.updateELOWithResult(newElo, metadata);
            scyWindowControl.addOtherScyWindow(newElo.getUri());
         } else {
            logger.error("can't find template elo, with uri: {eloTemplateUri}");
         }
      }
      createNewElo.modalDialogBox.close();
      newFromEloTemplateButton.turnedOn = false;
   }

   function setTitleAndLanguage(elo: IELO, title: String): Void {
      def titleMap = new LinkedHashMap();
      titleMap.put(Locale.getDefault(), title);
      elo.getMetadata().getMetadataValueContainer(titleKey).setValuesI18n(titleMap);
      elo.getContent().setLanguage(Locale.getDefault());
   }

   function createNewBlankEloAction(): Void {
      createBlankEloButton.turnedOn = true;
      var createNewElo = CreateNewElo {
                 createAction: createNewBlankElo
                 cancelAction: cancelNewElo
              }
      var typeNames = scyDesktop.newEloCreationRegistry.getEloTypeNames();
      createNewElo.listView.items = Sequences.sort(typeNames);
      createNewElo.label.text = ##"Select type";
      var eloIcon = windowStyler.getScyEloIcon(ImageWindowStyler.generalNew);
      var windowColorScheme = windowStyler.getWindowColorScheme(ImageWindowStyler.generalNew);

      createModalDialog(windowColorScheme, eloIcon, ##"Create new", createNewElo);
   }

   function createNewBlankElo(createNewElo: CreateNewElo): Void {
      var eloTypeName = createNewElo.listView.selectedItem as String;
      if (eloTypeName != null) {
         var eloType = scyDesktop.newEloCreationRegistry.getEloType(eloTypeName);
         if (eloType != null) {
            var title = scyDesktop.newTitleGenerator.generateNewTitleFromType(eloType);
            var scyWindow = scyWindowControl.addOtherScyWindow(eloType);
            scyWindow.title = title;
            scyWindowControl.makeMainScyWindow(scyWindow);
         }
      }
      createNewElo.modalDialogBox.close();
   }

   function cancelNewElo(createNewElo: CreateNewElo): Void {
      createNewElo.modalDialogBox.close();
      newFromEloTemplateButton.turnedOn = false;
      createBlankEloButton.turnedOn = false;
   }

   function createModalDialog(windowColorScheme: WindowColorScheme, eloIcon: EloIcon, title: String, modalDialogNode: ModalDialogNode): Void {
      eloIcon.selected = true;
      Composer.localizeDesign(modalDialogNode.getContentNodes(), StringLocalizer {});
      modalDialogNode.modalDialogBox = ModalDialogBox {
                 //            content: EmptyBorderNode {
                 //               content: modalDialogNode.getContentGroup();
                 //            }
                 content: modalDialogNode.getContentGroup()
                 title: title
                 eloIcon: eloIcon
                 windowColorScheme: windowColorScheme
                 closeAction: function(): Void {
                    newFromEloTemplateButton.turnedOn = false;
                    createBlankEloButton.turnedOn = false;
                    searcher.turnedOn = false;
                 }
              }
   }

   function cancelModalDialog(modalDialogNode: ModalDialogNode): Void {
      modalDialogNode.modalDialogBox.close();
      newFromEloTemplateButton.turnedOn = false;
      createBlankEloButton.turnedOn = false;
      searcher.turnedOn = false;
      archiver.turnedOn = false;
   }

   function createModalDialogForContent(windowColorScheme: WindowColorScheme, eloIcon: EloIcon, title: String, content: Node): ModalDialogBox {
      eloIcon.selected = true;
      ModalDialogBox {
         content: content
         title: title
         eloIcon: eloIcon
         windowColorScheme: windowColorScheme
         closeAction: function(): Void {
            newFromEloTemplateButton.turnedOn = false;
            createBlankEloButton.turnedOn = false;
            searcher.turnedOn = false;
         }
      }
   }

   function textQuerySearchAction(): Void {
      if (searchWrapper == null or searchWrapper.isSaved or searchWrapper.isEloBased) {
         searchWrapper = SearchWrapper {
                    scyWindowControl: scyWindowControl
                 }
      } else {
         scyWindowControl.addOtherScyWindow(searchWrapper.searchWindow);
      }

      scyWindowControl.makeMainScyWindow(searchWrapper.searchWindow);
   }

   function eloBasedSearchAction(scyElo: ScyElo): Void {
      def eloBasedSearchWrapper = SearchWrapper {
                 scyWindowControl: scyWindowControl
                 baseElo: scyElo
              }
      scyWindowControl.makeMainScyWindow(eloBasedSearchWrapper.searchWindow);
   }
}
