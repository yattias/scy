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
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.desktoputils.FpsDisplay;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.scydesktop.uicontrols.EloIconButton;
import java.util.LinkedHashMap;
import java.util.Locale;
import roolo.elo.api.IELO;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleLayer;
import eu.scy.client.desktop.scydesktop.tooltips.BubbleKey;
import javafx.scene.control.ListCell;
import eu.scy.common.mission.ArchivedElo;
import eu.scy.client.desktop.desktoputils.XFX;
import eu.scy.client.desktop.scydesktop.scywindows.window.ProgressOverlay;
import eu.scy.common.mission.ArchivedEloTitleComparator;
import eu.scy.common.scyelo.MultiScyEloLoader;

/**
 * @author sikken
 */
public class EloManagement extends CustomNode {

   def logger = Logger.getLogger(this.getClass());
   def archiverTypeName = "archive";
   def newEloFromTemplateTypeName = "new_template";
   def searcherTypeName = "search";
   def newBlankEloTypeName = "new_author";
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
   def tbi = scyDesktop.config.getToolBrokerAPI();
   var newFromEloTemplateButton: EloIconButton;
   var searcher: Searcher;
   var createBlankEloButton: EloIconButton;
   var archiver: Archiver;
   var eloTemplateUriDisplays: ArchivedElo[];
   var createNewEloFromTemplateModalDialogBox: ModalDialogBox;
   var createNewBlankEloModalDialogBox: ModalDialogBox;
   var undoArchiveModalDialogBox: ModalDialogBox;
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

      XFX.runActionInBackgroundAfter(findTemplateEloInformation, 10);
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
                 eloIcon: windowStyler.getScyEloIcon(archiverTypeName)
                 clickAction: undoArchiveAction
                 buttonSize: buttonSize
                 buttonActionScheme: buttonActionScheme
                 tooltipManager: tooltipManager
                 tooltip: ##"drag icon here, to remove ELO"
              }
      newFromEloTemplateButton = EloIconButton {
                 size: buttonSize
                 eloIcon: windowStyler.getScyEloIcon(newEloFromTemplateTypeName)
                 actionScheme: buttonActionScheme
                 action: createNewEloFromTemplateAction
                 tooltipManager: tooltipManager
                 tooltip: ##"create new ELO"
                 disableButton: true
              }
      searcher = Searcher {
                 tbi: scyDesktop.config.getToolBrokerAPI()
                 eloIcon: windowStyler.getScyEloIcon(searcherTypeName)
                 buttonSize: buttonSize
                 buttonActionScheme: buttonActionScheme
                 clickAction: textQuerySearchAction
                 dropAction: eloBasedSearchAction
                 tooltipManager: tooltipManager
                 tooltip: ##"search for ELOs or\ndrag icon here, to start search"
              }
      createBlankEloButton = EloIconButton {
                 size: buttonSize
                 eloIcon: windowStyler.getScyEloIcon(newBlankEloTypeName)
                 actionScheme: buttonActionScheme
                 action: createNewBlankEloAction
                 tooltipManager: tooltipManager
                 tooltip: ##"create new blank ELO"
              }
      scyDesktop.dragAndDropManager.addDropTaget(archiver);
      scyDesktop.dragAndDropManager.addDropTaget(newFromEloTemplateButton);
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

   function findTemplateEloInformation(): Void {
      var eloTemplateUris: URI[];
      if (templateEloUris != null) {
         for (uri in templateEloUris) {
            insert uri as URI into eloTemplateUris;
         }
      }
      def multiScyEloLoader = new MultiScyEloLoader(templateEloUris, false, tbi);
      eloTemplateUriDisplays = for (uri in eloTemplateUris) {
                 createArchivedElo(uri, multiScyEloLoader)
              }
      eloTemplateUriDisplays = Sequences.sort(eloTemplateUriDisplays, new ArchivedEloTitleComparator()) as ArchivedElo[];
      if (sizeof eloTemplateUris > 0) {
         FX.deferAction(function(): Void {
            newFromEloTemplateButton.disableButton = false;
         })
      }
   }

   function createArchivedElo(uri: URI, multiScyEloLoader: MultiScyEloLoader): ArchivedElo {
      def scyElo = multiScyEloLoader.getScyElo(uri);
      if (scyElo == null) {
         logger.warn("could not find template elo: {uri}");
         return null;
      }
      if (not scyDesktop.newEloCreationRegistry.containsEloType(scyElo.getTechnicalFormat())) {
         logger.warn("skipped template elo, because the type ({scyElo.getTechnicalFormat()}) ) is not configured for the user: {uri}");
         return null;
      }
      def eloIcon: EloIcon = windowStyler.getScyEloIcon(scyElo);
      def scySearchResult = new ArchivedElo(scyElo);
      scySearchResult.setEloIcon(eloIcon);
      return scySearchResult;
   }

   function createNewEloFromTemplateAction(): Void {
      newFromEloTemplateButton.turnedOn = true;
      def templateListSelectectionNode = ListSelectionTool {
                 listItems: eloTemplateUriDisplays
                 cellFactory: templateEloCellFactory
                 titleLabel: ##"Select template"
                 okButtonLabel: ##"Create"
                 okButtonAction: createNewEloFromTemplate
                 cancelAction: closeCreateNewEloFromTemplateModalDialogBox
              }

      createNewEloFromTemplateModalDialogBox = createModalDialog(newEloFromTemplateTypeName, ##"Create new", templateListSelectectionNode);
   }

   function templateEloCellFactory(): ListCell {
      var listCell: ListCell;
      listCell = ListCell {
                 node: TemplateEloListCellNode {
                    newEloCreationRegistry: scyDesktop.newEloCreationRegistry
                    archivedElo: bind listCell.item as ArchivedElo
                 }
              }
      return listCell;
   }

   function createNewEloFromTemplate(templateDisplay: Object): Void {
      def archivedElo = templateDisplay as ArchivedElo;
      if (archivedElo != null) {
         ProgressOverlay.startShowWorking();
         def createNewElo = function(): Object {
                    def newScyElo = new ScyElo(archivedElo.getScyElo().getElo().clone(), tbi);
                    setTitleAndLanguage(newScyElo.getElo(), scyDesktop.newTitleGenerator.generateNewTitleFromName(newScyElo.getTitle()));
                    newScyElo.setTemplate(true);
                    newScyElo.setCreator(tbi.getLoginUserName());
                    newScyElo
                 }
         def showNewElo = function(object: Object): Void {
                    def newScyElo = object as ScyElo;
                    scyWindowControl.addOtherScyWindow(newScyElo);
                    ProgressOverlay.stopShowWorking();
                 }
         XFX.runActionInBackgroundAndCallBack(createNewElo, showNewElo);
      }
      closeCreateNewEloFromTemplateModalDialogBox()
   }

   function setTitleAndLanguage(elo: IELO, title: String): Void {
      def titleMap = new LinkedHashMap();
      titleMap.put(Locale.getDefault(), title);
      elo.getMetadata().getMetadataValueContainer(titleKey).setValuesI18n(titleMap);
      elo.getContent().setLanguage(Locale.getDefault());
   }

   function closeCreateNewEloFromTemplateModalDialogBox(): Void {
      createNewEloFromTemplateModalDialogBox.close();
      newFromEloTemplateButton.turnedOn = false;
   }

   function createModalDialog(eloTypeName: String, title: String, content: Node): ModalDialogBox {
      def eloIcon = windowStyler.getScyEloIcon(eloTypeName);
      def windowColorScheme = windowStyler.getWindowColorScheme(eloTypeName);
      eloIcon.selected = true;
      ModalDialogBox {
         content: content
         title: title
         eloIcon: eloIcon
         windowColorScheme: windowColorScheme
      }
   }

   function createNewBlankEloAction(): Void {
      createBlankEloButton.turnedOn = true;
      def typeNames = scyDesktop.newEloCreationRegistry.getEloTypeNames();
      def typeListSelectectionNode = ListSelectionTool {
                 listItems: Sequences.sort(typeNames)
                 titleLabel: ##"Select type"
                 okButtonLabel: ##"Create"
                 okButtonAction: createNewBlankElo
                 cancelAction: closeCreateNewBlankModalDialogBox
              }

      createNewBlankEloModalDialogBox = createModalDialog(newBlankEloTypeName, ##"Create new", typeListSelectectionNode);
   }

   function createNewBlankElo(typeNameDisplay: Object): Void {
      def eloTypeName = typeNameDisplay as String;
      if (eloTypeName != null) {
         def eloType = scyDesktop.newEloCreationRegistry.getEloType(eloTypeName);
         if (eloType != null) {
            ProgressOverlay.startShowWorking();
            def title = scyDesktop.newTitleGenerator.generateNewTitleFromType(eloType);
            def scyWindow = scyWindowControl.addOtherScyWindow(eloType);
            scyWindow.title = title;
            scyWindowControl.makeMainScyWindow(scyWindow);
            ProgressOverlay.stopShowWorking();
         }
      }
      closeCreateNewBlankModalDialogBox();
   }

   function closeCreateNewBlankModalDialogBox(): Void {
      createNewBlankEloModalDialogBox.close();
      createBlankEloButton.turnedOn = false;
   }

   function undoArchiveAction(): Void {
      archiver.turnedOn = true;
      def undoArchiveListSelectectionNode = ListSelectionTool {
                 listItems: getArchivedElos()
                 cellFactory: archivedEloCellFactory
                 titleLabel: ##"Select removed ELO"
                 okButtonLabel: ##"Place back"
                 okButtonAction: undoArchiveElo
                 cancelAction: closeUndoArchiveModalDialogBox
              }

      undoArchiveModalDialogBox = createModalDialog(archiverTypeName, ##"Place back", undoArchiveListSelectectionNode);
   }

   function undoArchiveElo(archiveEloObject: Object): Void {
      def archivedElo = archiveEloObject as ArchivedElo;
      if (archivedElo != null) {
         FX.deferAction(function(): Void {
            def lastVersionScyElo = ScyElo.loadLastVersionMetadata(archivedElo.getEloUri(), tbi);
            var scyWindow = scyWindowControl.addOtherScyWindow(lastVersionScyElo);
            scyWindowControl.makeMainScyWindow(scyWindow);
            scyDesktop.missionModelFX.removeArchivedElo(archivedElo);
         })
      }
      closeUndoArchiveModalDialogBox();
   }

   function getArchivedElos(): ArchivedElo[] {
      var nrOfLoadsNeeded = 0;
      for (archivedEloObject in scyDesktop.missionModelFX.getArchivedElos()) {
         def archivedElo = archivedEloObject as ArchivedElo;
         if (archivedElo.getScyElo() == null) {
            ++nrOfLoadsNeeded
         }
      }
      if (nrOfLoadsNeeded > 0) {
         XFX.deferActionAndWait(loadMissingArchivedScyElos);
      }
      for (archivedEloObject in scyDesktop.missionModelFX.getArchivedElos()) {
         def archivedElo = archivedEloObject as ArchivedElo;
         if (archivedElo.getEloIcon() == null) {
            archivedElo.setEloIcon(windowStyler.getScyEloIcon(archivedElo.getScyElo()))
         }
         archivedElo
      }
   }

   function loadMissingArchivedScyElos(): Void {
      ProgressOverlay.startShowWorking();
      for (archivedEloObject in scyDesktop.missionModelFX.getArchivedElos()) {
         def archivedElo = archivedEloObject as ArchivedElo;
         if (archivedElo.getScyElo() == null) {
            archivedElo.setScyElo(ScyElo.loadMetadata(archivedElo.getEloUri(), tbi))
         }
      }
      ProgressOverlay.stopShowWorking();
   }

   function archivedEloCellFactory(): ListCell {
      var listCell: ListCell;
      listCell = ListCell {
                 node: ArchivedEloListCellNode {
                    newEloCreationRegistry: scyDesktop.newEloCreationRegistry
                    archivedElo: bind listCell.item as ArchivedElo
                 }
              }
      return listCell;
   }

   function closeUndoArchiveModalDialogBox(): Void {
      undoArchiveModalDialogBox.close();
      archiver.turnedOn = false;
   }

   function textQuerySearchAction(): Void {
      if (searchWrapper == null or searchWrapper.isSaved or searchWrapper.isEloBased) {
         def history = searchWrapper.searchNode.getHistory();
         searchWrapper = SearchWrapper {
                    scyWindowControl: scyWindowControl
                    history: history
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
