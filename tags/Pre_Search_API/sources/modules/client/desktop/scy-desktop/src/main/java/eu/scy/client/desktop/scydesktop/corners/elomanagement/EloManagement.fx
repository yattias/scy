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
import java.lang.System;
import roolo.elo.metadata.keys.Contribute;
import eu.scy.client.desktop.scydesktop.scywindows.WindowStyler;
import eu.scy.client.desktop.scydesktop.imagewindowstyler.ImageWindowStyler;
import eu.scy.client.desktop.scydesktop.uicontrols.MultiImageButton;
import eu.scy.client.desktop.scydesktop.utils.i18n.Composer;
import eu.scy.client.desktop.scydesktop.art.WindowColorScheme;
import eu.scy.client.desktop.scydesktop.utils.FpsDisplay;
import eu.scy.common.scyelo.ScyElo;
import java.util.ArrayList;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.searchers.SameTechnicalFormatSearcher;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.searchers.SameAuthorSearcher;
import java.util.List;
import org.roolo.search.BasicMetadataQuery;
import org.roolo.search.BasicSearchOperations;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.searchers.SameMissionSearcher;

/**
 * @author sikken
 */
public class EloManagement extends CustomNode, EloBasedSearchFinished, QuerySearchFinished {

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
   def showCreateBlankElo = scyDesktop.initializer.authorMode;
   def authorKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.AUTHOR);
   def userId = scyDesktop.config.getToolBrokerAPI().getLoginUserName();
   def tbi = scyDesktop.config.getToolBrokerAPI();
   def newFromEloTemplateButton: MultiImageButton = MultiImageButton {
         imageName: "new1"
         action: createNewEloFromTemplateAction
      }
   def searcher: Searcher = Searcher {
         tbi: scyDesktop.config.getToolBrokerAPI()
         imageName: "search1"
         clickAction: textQuerySearchAction
         dropAction: eloBasedSearchAction
      }
   def createBlankEloButton: MultiImageButton = MultiImageButton {
         imageName: "new1_a"
         action: createNewBlankEloAction
      }
   def archiver = Archiver {
         tbi: scyDesktop.config.getToolBrokerAPI()
         missionMapModel: scyDesktop.missionModelFX
         scyWindowControl: scyWindowControl
      }
   def eloBasedSearchers = new ArrayList();
   var eloBasedSearchDesign: GridEloBasedSearch;
   var backgroundEloBasedSearch: BackgroundEloBasedSearch;
   var searchElos: SearchElos;
   var gridEloSearch: GridEloSearch;
   var backgroundQuerySearch: BackgroundQuerySearch;

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
      findTemplateEloInformation();
      scyDesktop.dragAndDropManager.addDropTaget(archiver);
      scyDesktop.dragAndDropManager.addDropTaget(searcher);
//      eloBasedSearchers.add(new SameEloSearcher());
//      eloBasedSearchers.add(new FindNothingSearcher());
      eloBasedSearchers.add(new SameTechnicalFormatSearcher(tbi));
      eloBasedSearchers.add(new SameAuthorSearcher(tbi));
      eloBasedSearchers.add(new SameMissionSearcher(tbi,true));
      eloBasedSearchers.add(new SameMissionSearcher(tbi,false));
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
   var eloTemplateUriDisplays: ScySearchResult[];

   function findTemplateEloInformation() {
      if (templateEloUris != null) {
         for (uri in templateEloUris) {
            insert uri as URI into eloTemplateUris;
         }
      }
      eloTemplateUriDisplays = for (uri in eloTemplateUris) {
            createScySearchResult(uri)
         //            var uriDisplay = createUriDisplay(uri, false);
         //            if (uriDisplay == null) {
         //               logger.error("Cannot find template elo with uri: {uri}");
         //            }
         //            uriDisplay;
         }
      //      eloTemplateUriDisplays = Sequences.sort(eloTemplateUriDisplays, UriDisplayComparator {}) as UriDisplay[];
      eloTemplateUriDisplays = Sequences.sort(eloTemplateUriDisplays, new ScySearchResultTitleComparator()) as ScySearchResult[];
      newFromEloTemplateButton.disable = sizeof eloTemplateUris == 0;
   }

   function createScySearchResult(uri: URI): ScySearchResult {
      def scyElo = ScyElo.loadMetadata(uri, tbi);
      def eloIcon: EloIcon = windowStyler.getScyEloIcon(uri);
      def scySearchResult = new ScySearchResult(scyElo, 1.0);
      scySearchResult.setEloIcon(eloIcon);
      scySearchResult
   }

   function createUriDisplay(uri: URI, showAuthor: Boolean): UriDisplay {
      if (uri == null) {
         return UriDisplay {
               uri: uri
               display: ##"Nothing found"
            };
      }
      def scyElo = ScyElo.loadMetadata(uri, tbi);
      if (scyElo == null) {
         return null;
      }
      def title = scyElo.getTitle();
      def technicalFormat = scyElo.getTechnicalFormat();
      //      var author = metadata.getMetadataValueContainer(authorKey).getValue() as Contribute;
      def typeName = scyDesktop.newEloCreationRegistry.getEloTypeName(technicalFormat);
      var authorDisplay = "";
      if (showAuthor) {
      //         var authorName = author.getVCard();
      //         if (authorName == null) {
      //            authorName = ##"unknown"
      //         }
      //         authorDisplay = "{authorName}: "
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
      createNewElo.listView.cellFactory = createNewElo.simpleScyEloCellFactory;
      createNewElo.listView.items = eloTemplateUriDisplays;

      createNewElo.label.text = ##"Select template";
      var eloIcon = windowStyler.getScyEloIcon(ImageWindowStyler.generalNew);
      var windowColorScheme = windowStyler.getWindowColorScheme(ImageWindowStyler.generalNew);

      createModalDialog(windowColorScheme, eloIcon, ##"Create new", createNewElo);
   }

   function createNewEloFromTemplate(createNewElo: CreateNewElo): Void {
      var scySearchResult = createNewElo.listView.selectedItem as ScySearchResult;
      //      var uriDisplay = createNewElo.listView.selectedItem as UriDisplay;
      if (scySearchResult != null) {
         var eloTemplateUri = scySearchResult.getScyElo().getUri();
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
      eloIcon.selected = true;
      Composer.localizeDesign(modalDialogNode.getContentNodes());
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
      if (backgroundQuerySearch != null) {
         backgroundQuerySearch.abort();
         backgroundQuerySearch = null;
      }
      if (backgroundEloBasedSearch != null) {
         backgroundEloBasedSearch.abort();
         backgroundEloBasedSearch = null;
      }
   }

   function searchEloAction(): Void {
      searcher.turnedOn = true;
      searchElos = SearchElos {
            newEloCreationRegistry: scyDesktop.newEloCreationRegistry
            windowStyler: windowStyler
            cancelAction: cancelModalDialog
            searchAction: searchForElos
            simpleSearchAction: simpleSearchForElos
            openAction: openElo
         }
      var typeNames = scyDesktop.newEloCreationRegistry.getEloTypeNames();
      searchElos.typesListView.items = Sequences.sort(typeNames);

      def eloIcon = windowStyler.getScyEloIcon(ImageWindowStyler.generalSearch);
      def windowColorScheme = windowStyler.getWindowColorScheme(ImageWindowStyler.generalSearch);

      createModalDialog(windowColorScheme, eloIcon, ##"Search", searchElos);
   }

   function searchForElos(searchElos: SearchElos): Void {
      if (backgroundQuerySearch != null) {
         backgroundQuerySearch.abort();
      }

      searchElos.openButton.disable = true;
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
      backgroundQuerySearch = new BackgroundQuerySearch(tbi, scyDesktop.newEloCreationRegistry, searchQuery, this);

      backgroundQuerySearch.start();
      searchElos.showSearching();
   }
   
   function simpleSearchForElos(searchElos: SearchElos): Void {
      if (backgroundQuerySearch != null) {
         backgroundQuerySearch.abort();
      }

      searchElos.openButton.disable = true;
      var searchQuery: BasicMetadataQuery = new BasicMetadataQuery(searchElos.simpleSearchField.text);
      backgroundQuerySearch = new BackgroundQuerySearch(tbi, scyDesktop.newEloCreationRegistry, searchQuery, this);

      backgroundQuerySearch.start();
      searchElos.showSearching();
   }

   override public function querySearchFinished(scySearchResultList: List): Void {
      setScySearchResults(scySearchResultList, gridEloSearch);
   }

   function setScySearchResults(scySearchResultList: List, showSearching: ShowSearching) {
      def results =
         for (scySearchResultObject in scySearchResultList) {
            def scySearchResult = scySearchResultObject as ScySearchResult;
            def eloIcon: EloIcon = windowStyler.getScyEloIcon(scySearchResult.getScyElo().getUri());
            scySearchResult.setEloIcon(eloIcon);
            scySearchResult
         }
      showSearching.showSearchResult(results);
   }

   function createTypeQuery(searchElos: SearchElos): IQuery {
      var eloTypeName = searchElos.typesListView.selectedItem as String;
      if (not searchElos.allTypesCheckBox.selected and eloTypeName != null) {
         var eloType = scyDesktop.newEloCreationRegistry.getEloType(eloTypeName);
         return new BasicMetadataQuery(technicalFormatKey, BasicSearchOperations.EQUALS, eloType);
      }
      return null;
   }

   function createTitleQuery(searchElos: SearchElos): IQuery {
      var searchTitle = searchElos.nameTextbox.rawText.trim();
      if (searchTitle.length() > 0) {
         return new BasicMetadataQuery(titleKey, BasicSearchOperations.REGULAR_EXPRESSIONS, ".*{searchTitle}.*");
      }
      return null;
   }

   function createAuthorQuery(searchElos: SearchElos): IQuery {
      // if both mine and others and checked or unchecked, nothing to do
      if (searchElos.mineCheckBox.selected or searchElos.othersCheckBox.selected) {
         if (not (searchElos.mineCheckBox.selected and searchElos.othersCheckBox.selected)) {
            if (searchElos.mineCheckBox.selected) {
               var authorValue = new Contribute(userId, System.currentTimeMillis());
               return new BasicMetadataQuery(authorKey, BasicSearchOperations.EQUALS, authorValue);
            }
            if (searchElos.othersCheckBox.selected) {
               var authorValue = new Contribute(userId, System.currentTimeMillis());
               return new BasicMetadataQuery(authorKey, BasicSearchOperations.NOT_EQUALS, authorValue);
            }
         }
      }
      return null;
   }

   function openElo(searchElos: SearchElos): Void {
      var scySearchResult = searchElos.resultsListView.selectedItem as ScySearchResult;
      if (scySearchResult != null) {
         scyWindowControl.addOtherScyWindow(scySearchResult.getScyElo().getUri());
      }

      searchElos.modalDialogBox.close();
      searcher.turnedOn = false;
   }

   function textQuerySearchAction(): Void {
      FX.deferAction(function(): Void {
         searcher.turnedOn = true;
      });
      gridEloSearch = GridEloSearch {
            newEloCreationRegistry: scyDesktop.newEloCreationRegistry
            windowStyler: windowStyler
            cancelAction: cancelModalDialog
            doSearch: doTextQuerySearch
            openAction: doEloBasedOpen
         }
      def eloIcon = windowStyler.getScyEloIcon(ImageWindowStyler.generalSearch);
      def windowColorScheme = windowStyler.getWindowColorScheme(ImageWindowStyler.generalSearch);

      createModalDialog(windowColorScheme, eloIcon, ##"Search", gridEloSearch);
   }

   function doTextQuerySearch(gridEloSearch: GridEloSearch): Void {
      if (backgroundQuerySearch != null) {
         backgroundQuerySearch.abort();
      }

      searchElos.openButton.disable = true;
      var searchQuery: BasicMetadataQuery = null;
      if (scyDesktop.initializer.offlineMode){
         def queryText = gridEloSearch.queryBox.rawText.trim();
         if (queryText!=""){
            searchQuery = new BasicMetadataQuery(titleKey, BasicSearchOperations.EQUALS, "{queryText}");
         }
      }
      else{
         searchQuery = new BasicMetadataQuery(gridEloSearch.queryBox.rawText);
      }
      backgroundQuerySearch = new BackgroundQuerySearch(tbi, scyDesktop.newEloCreationRegistry, searchQuery, this);

      backgroundQuerySearch.start();
      gridEloSearch.showSearching();
   }

   function eloBasedSearchAction(scyElo: ScyElo): Void {
      FX.deferAction(function(): Void {
         searcher.turnedOn = true;
      });
      eloBasedSearchDesign = GridEloBasedSearch {
            newEloCreationRegistry: scyDesktop.newEloCreationRegistry
            windowStyler: windowStyler
            cancelAction: cancelModalDialog
            doSearch: doEloBasedSearch
            baseOnAction: doEloBasedBaseOn
            openAction: doEloBasedOpen
         }
      eloBasedSearchDesign.baseElo = scyElo;
      eloBasedSearchDesign.baseEloIcon = windowStyler.getScyEloIcon(scyElo.getUri());
      if (eloBasedSearchers != null) {
         eloBasedSearchDesign.searchersList.items = for (eloBasedSearcher in eloBasedSearchers) {
               eloBasedSearcher as EloBasedSearcher
            }
      }
      def eloIcon = windowStyler.getScyEloIcon(ImageWindowStyler.generalSearch);
      def windowColorScheme = windowStyler.getWindowColorScheme(ImageWindowStyler.generalSearch);

      createModalDialog(windowColorScheme, eloIcon, ##"Search", eloBasedSearchDesign);
   }

   function doEloBasedSearch(eloBasedSearchDesign: GridEloBasedSearch): Void {
//      if (eloBasedSearchDesign.selectedEloBasedSearcher == null) {
//         delete  eloBasedSearchDesign.searchersList.items;
//         return;
//      }
      if (backgroundEloBasedSearch != null) {
         backgroundEloBasedSearch.abort();
      }
      eloBasedSearchDesign.showSearching();
      backgroundEloBasedSearch = new BackgroundEloBasedSearch(tbi, scyDesktop.newEloCreationRegistry, eloBasedSearchDesign.selectedEloBasedSearchers, eloBasedSearchDesign.baseElo, this);

      backgroundEloBasedSearch.start();
   }

   override public function eloBasedSearchFinished(scySearchResultList: List): Void {
      setScySearchResults(scySearchResultList, eloBasedSearchDesign);
   }

   function doEloBasedBaseOn(eloBasedSearchDesign: GridEloBasedSearch): Void {
      eloBasedSearchDesign.baseElo = eloBasedSearchDesign.selectedSearchResult.getScyElo();
      eloBasedSearchDesign.baseEloIcon = (eloBasedSearchDesign.selectedSearchResult.getEloIcon() as EloIcon).clone();
      doEloBasedSearch(eloBasedSearchDesign);
   }

   function doEloBasedOpen(gridSearchNode: GridSearchNode): Void {
      scyWindowControl.addOtherScyWindow(gridSearchNode.selectedSearchResult.getScyElo().getUri());
      cancelModalDialog(gridSearchNode);
   }

}