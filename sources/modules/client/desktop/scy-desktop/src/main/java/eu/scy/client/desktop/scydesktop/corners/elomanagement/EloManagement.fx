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
import eu.scy.client.desktop.scydesktop.scywindows.EloIcon;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;
import javafx.util.Sequences;
import java.net.URI;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.api.search.IQuery;
import roolo.api.search.AndQuery;
import roolo.cms.repository.mock.BasicMetadataQuery;
import roolo.cms.repository.search.BasicSearchOperations;
import java.lang.System;
import roolo.elo.metadata.keys.Contribute;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.ImageWindowStyler;
import eu.scy.client.desktop.scydesktop.uicontrols.MultiImageButton;
import eu.scy.client.desktop.scydesktop.utils.i18n.Composer;
import eu.scy.client.desktop.scydesktop.scywindows.EloInfoControl;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.utils.EmptyBorderNode;
import javafx.scene.Group;
import eu.scy.client.desktop.scydesktop.utils.FpsDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import eu.scy.common.scyelo.ScyElo;
import java.util.ArrayList;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.searchers.SameEloSearcher;
import roolo.api.search.ISearchResult;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.searchers.SameTechnicalFormatSearcher;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.searchers.SameAuthorSearcher;

/**
 * @author sikken
 */
public class EloManagement extends CustomNode {

   def logger = Logger.getLogger(this.getClass());
   public var scyDesktop: ScyDesktop;
   public var scyWindowControl: ScyWindowControl;
   public var repository: IRepository;
   public var metadataTypeManager: IMetadataTypeManager;
   public var titleKey: IMetadataKey;
   public var technicalFormatKey: IMetadataKey;
   public var eloFactory: IELOFactory;
   public var templateEloUris: URI[];
   public var tooltipManager: TooltipManager;
   public var windowStyler: WindowStyler;
   public var eloInfoControl: EloInfoControl;
   def showCreateBlankElo = scyDesktop.initializer.authorMode;
   def authorKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR);
   def userId = scyDesktop.config.getToolBrokerAPI().getLoginUserName();
   def tbi = scyDesktop.config.getToolBrokerAPI();
//   def newFromEloTemplateButton =  Button {
//      text: "New"
//      action: createNewEloFromTemplateAction
//   }
//   def searchButton =  Button {
//      text: "Search"
//      action: searchEloAction
//   }
//   def createBlankEloButton =  Button {
//      text: "Create (Dev/Author)"
//      action: createNewBlankEloAction
//   }
   def newFromEloTemplateButton: MultiImageButton = MultiImageButton {
         imageName: "new"
         action: createNewEloFromTemplateAction
      }
   def searcher: Searcher = Searcher {
         tbi: scyDesktop.config.getToolBrokerAPI()
         imageName: "search"
         clickAction: searchEloAction
         dropAction: eloBasedSearchAction
      }
   def createBlankEloButton: MultiImageButton = MultiImageButton {
         imageName: "new_a"
         action: createNewBlankEloAction
      }
   def archiver = Archiver {
         tbi: scyDesktop.config.getToolBrokerAPI()
         missionMapModel: scyDesktop.missionModelFX
         scyWindowControl: scyWindowControl
      }
   def eloBasedSearchers = new ArrayList();

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
      if (eloInfoControl == null) {
         eloInfoControl = scyDesktop.eloInfoControl;
      }
      //newFromEloTemplateButton.disable = templateEloUris == null or templateEloUris.isEmpty();
      findTemplateEloInformation();
      scyDesktop.dragAndDropManager.addDropTaget(archiver);
      scyDesktop.dragAndDropManager.addDropTaget(searcher);
      eloBasedSearchers.add(new SameEloSearcher());
      eloBasedSearchers.add(new SameTechnicalFormatSearcher(tbi));
      eloBasedSearchers.add(new SameAuthorSearcher(tbi));
   }

   public override function create(): Node {
      var fpdDisplay: Node;
      if (showCreateBlankElo) {
         fpdDisplay = FpsDisplay {
            }
      }

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

   var eloTemplateUris: URI[];
   var eloTemplateUriDisplays: UriDisplay[];

   function findTemplateEloInformation() {
      if (templateEloUris != null) {
         for (uri in templateEloUris) {
            insert uri as URI into eloTemplateUris;
         }
      }
      eloTemplateUriDisplays = for (uri in eloTemplateUris) {
            var uriDisplay = createUriDisplay(uri, false);
            if (uriDisplay == null) {
               logger.error("Cannot find template elo with uri: {uri}");
            }
            uriDisplay;
         }
      eloTemplateUriDisplays = Sequences.sort(eloTemplateUriDisplays, UriDisplayComparator {}) as UriDisplay[];
      newFromEloTemplateButton.disable = sizeof eloTemplateUris == 0;
   }

   function createUriDisplay(uri: URI, showAuthor: Boolean): UriDisplay {
      if (uri == null) {
         return UriDisplay {
               uri: uri
               display: ##"Nothing found"
            };
      }
      var metadata = repository.retrieveMetadata(uri);
      if (metadata == null) {
         return null;
      }
      var title = eloInfoControl.getEloTitle(uri);
      var technicalFormat = metadata.getMetadataValueContainer(technicalFormatKey).getValue() as String;
      var author = metadata.getMetadataValueContainer(authorKey).getValue() as Contribute;
      var typeName = scyDesktop.newEloCreationRegistry.getEloTypeName(technicalFormat);
      var authorDisplay = "";
      if (showAuthor) {
         var authorName = author.getVCard();
         if (authorName == null) {
            authorName = ##"unknown"
         }
         authorDisplay = "{authorName}: "
      }

      UriDisplay {
         uri: uri
         display: "{authorDisplay}{title} ({typeName})"
      }
   }

   function createNewEloFromTemplateAction(): Void {
      newFromEloTemplateButton.turnedOn = true;
      var createNewElo = CreateNewElo {
            createAction: createNewEloFromTemplate
            cancelAction: cancelNewElo
         }
      createNewElo.listView.items = eloTemplateUriDisplays;

      createNewElo.label.text = ##"Select template";
      var eloIcon = windowStyler.getScyEloIcon(ImageWindowStyler.generalNew);
      var windowColorScheme = windowStyler.getWindowColorScheme(ImageWindowStyler.generalNew);

      createModalDialog(windowColorScheme, eloIcon, ##"Create new", createNewElo);
   }

   function createNewEloFromTemplate(createNewElo: CreateNewElo): Void {
      var uriDisplay = createNewElo.listView.selectedItem as UriDisplay;
      if (uriDisplay != null) {
         var eloTemplateUri = uriDisplay.uri;
         var newElo = repository.retrieveELO(eloTemplateUri);
         if (newElo != null) {
            var titleContainer = newElo.getMetadata().getMetadataValueContainer(titleKey);
            var templateTitle = titleContainer.getValue() as String;
            titleContainer.setValue(scyDesktop.newTitleGenerator.generateNewTitleFromName(templateTitle));
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
      Composer.localizeDesign(modalDialogNode.getContentNodes());
      modalDialogNode.modalDialogBox = ModalDialogBox {
            content: EmptyBorderNode {
               content: Group {
                  content: modalDialogNode.getContentNodes();
               }
            }

//            targetScene: scyDesktop.scene
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

   function searchEloAction(): Void {
      searcher.turnedOn = true;
      var searchElos = SearchElos {
            cancelAction: cancelSearchElo
            searchAction: searchForElos
            openAction: openElo
         }
      var typeNames = scyDesktop.newEloCreationRegistry.getEloTypeNames();
      searchElos.typesListView.items = Sequences.sort(typeNames);
      searchElos.resultsListView.cellFactory = eloUriCellFactory;

      def eloIcon = windowStyler.getScyEloIcon(ImageWindowStyler.generalSearch);
      def windowColorScheme = windowStyler.getWindowColorScheme(ImageWindowStyler.generalSearch);

      createModalDialog(windowColorScheme, eloIcon, ##"Search", searchElos);
   }

   function searchForElos(searchElos: SearchElos): Void {
      setSearchResultState(searchElos, ##"searching...");
      searchElos.openButton.disable = true;
      // delay the search action, so the searching message can be shown
      Timeline {
         repeatCount: 1
         keyFrames: [
            KeyFrame {
               time: 25ms
               action: function(): Void {
                  doSearchForElos(searchElos);
               }
            }
         ]
      }.play()
   }

   function doSearchForElos(searchElos: SearchElos): Void {
      var searchQuery: AndQuery;
      var typeQuery = createTypeQuery(searchElos);
      var titleQuery = createTitleQuery(searchElos);
      var authorQuery = createAuthorQuery(searchElos);
      if (typeQuery != null) {
         searchQuery = new AndQuery(typeQuery);
      }
      if (titleQuery != null) {
         if (searchQuery == null) {
            searchQuery = new AndQuery(titleQuery);
         } else {
            searchQuery.addQuery(titleQuery);
         }
      }
      if (authorQuery != null) {
         if (searchQuery == null) {
            searchQuery = new AndQuery(authorQuery);
         } else {
            searchQuery.addQuery(authorQuery);
         }
      }
      var queryResults = repository.search(searchQuery);
      logger.info("search query: {searchQuery}\nNumber of results: {queryResults.size()}");
      if (queryResults.size() > 0) {
         var resultsUris = for (queryResult in queryResults) {
               var eloType = eloInfoControl.getEloType(queryResult.getUri());
               if (scyDesktop.newEloCreationRegistry.containsEloType(eloType)) {
                  createUriDisplay(queryResult.getUri(), true);
               } else {
                  null
               }

            }
         searchElos.resultsListView.items = resultsUris;
         searchElos.resultsListView.disable = false;
      } else {
         setSearchResultState(searchElos, ##"nothing found");
      }
   }

   function createTypeQuery(searchElos: SearchElos): IQuery {
      var eloTypeName = searchElos.typesListView.selectedItem as String;
      if (not searchElos.allTypesCheckBox.selected and eloTypeName != null) {
         var eloType = scyDesktop.newEloCreationRegistry.getEloType(eloTypeName);
         return new BasicMetadataQuery(technicalFormatKey, BasicSearchOperations.EQUALS, eloType, null);
      }
      return null;
   }

   function createTitleQuery(searchElos: SearchElos): IQuery {
      var searchTitle = searchElos.nameTextbox.rawText.trim();
      if (searchTitle.length() > 0) {
         return new BasicMetadataQuery(titleKey, BasicSearchOperations.REGULAR_EXPRESSIONS, ".*{searchTitle}.*", null);
      }
      return null;
   }

   function createAuthorQuery(searchElos: SearchElos): IQuery {
      // if both mine and others and checked or unchecked, nothing to do
      if (searchElos.mineCheckBox.selected or searchElos.othersCheckBox.selected) {
         if (not (searchElos.mineCheckBox.selected and searchElos.othersCheckBox.selected)) {
            if (searchElos.mineCheckBox.selected) {
               var authorValue = new Contribute(userId, System.currentTimeMillis());
               return new BasicMetadataQuery(authorKey, BasicSearchOperations.EQUALS, authorValue, null);
            }
            if (searchElos.othersCheckBox.selected) {
               var authorValue = new Contribute(userId, System.currentTimeMillis());
               return new BasicMetadataQuery(authorKey, BasicSearchOperations.NOT_EQUALS, authorValue, null);
            }
         }
      }
      return null;
   }

   function openElo(searchElos: SearchElos): Void {
      var uriDisplay = searchElos.resultsListView.selectedItem as UriDisplay;
      if (uriDisplay != null) {
         scyWindowControl.addOtherScyWindow(uriDisplay.uri);
      }

      searchElos.modalDialogBox.close();
      searcher.turnedOn = false;
   }

   function cancelSearchElo(searchElos: SearchElos): Void {
      searchElos.modalDialogBox.close();
      searcher.turnedOn = false;
   }

   function setSearchResultState(searchElos: SearchElos, statusMessage: String): Void {
      searchElos.resultsListView.items = UriDisplay {
            uri: null;
            display: statusMessage
         };
      searchElos.resultsListView.disable = true;
   }

   function eloUriCellFactory(): ListCell {
      var listCell: ListCell;
      listCell = ListCell {
            node: Label {
               text: bind (listCell.item as UriDisplay).display
            }
         }
   }

   function eloBasedSearchAction(scyElo: ScyElo): Void {
      FX.deferAction(function():Void{
            searcher.turnedOn = true;
         });
      var eloBasedSearchDesign = EloBasedSearchDesign {
            newEloCreationRegistry: scyDesktop.newEloCreationRegistry
            cancelAction: cancelModalDialog
            doSearch: doEloBasedSearch
            baseOnAction: doEloBasedBaseOn
            openAction: doEloBasedOpen
         }
      eloBasedSearchDesign.baseElo = scyElo;
      if (eloBasedSearchers != null) {
         eloBasedSearchDesign.relationListView.items = for (eloBasedSearcher in eloBasedSearchers) {
               eloBasedSearcher as EloBasedSearcher
            }
      }
      def eloIcon = windowStyler.getScyEloIcon(ImageWindowStyler.generalSearch);
      def windowColorScheme = windowStyler.getWindowColorScheme(ImageWindowStyler.generalSearch);

      createModalDialog(windowColorScheme, eloIcon, ##"Search", eloBasedSearchDesign);
   }

   function doEloBasedSearch(eloBasedSearchDesign: EloBasedSearchDesign): Void {
      if (eloBasedSearchDesign.selectedEloBasedSearcher == null) {
         delete  eloBasedSearchDesign.resultsListView.items;
         return;
      }

      def uriSearchResults = eloBasedSearchDesign.selectedEloBasedSearcher.findElos(eloBasedSearchDesign.baseElo);
      var searchResults: ScySearchResult[] = [];
      if (uriSearchResults != null) {
         for (uriSearchResultObject in uriSearchResults) {
            def uriSearchResult = uriSearchResultObject as ISearchResult;
            def eloType = eloInfoControl.getEloType(uriSearchResult.getUri());
            if (scyDesktop.newEloCreationRegistry.containsEloType(eloType)) {
               def scyElo = ScyElo.loadMetadata(uriSearchResult.getUri(), tbi);
               def searchResult = ScySearchResult {
                     scyElo: scyElo
                     relevance: uriSearchResult.getRelevance();
                  }
               insert searchResult into searchResults;
            }
         }
      }
      eloBasedSearchDesign.resultsListView.items = searchResults

   }

   function doEloBasedBaseOn(eloBasedSearchDesign: EloBasedSearchDesign): Void {
      eloBasedSearchDesign.baseElo = eloBasedSearchDesign.selectedSearchResult.scyElo;
      doEloBasedSearch(eloBasedSearchDesign);
   }

   function doEloBasedOpen(eloBasedSearchDesign: EloBasedSearchDesign): Void {
      scyWindowControl.addOtherScyWindow(eloBasedSearchDesign.selectedSearchResult.scyElo.getUri());
      cancelModalDialog(eloBasedSearchDesign);
   }

}
