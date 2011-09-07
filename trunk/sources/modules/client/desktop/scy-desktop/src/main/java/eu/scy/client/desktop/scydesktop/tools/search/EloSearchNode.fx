/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.search;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.layout.Resizable;
import javafx.util.Math;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import java.net.URI;
import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;
import javafx.geometry.HPos;
import javafx.scene.control.Label;
import com.javafx.preview.layout.Grid;
import com.javafx.preview.layout.GridRow;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.QuerySearchFinished;
import java.util.List;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.BackgroundQuerySearch;
import roolo.search.MetadataQueryComponent;
import roolo.search.Query;
import roolo.search.QueryContext;
import org.apache.log4j.Logger;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.client.desktop.desktoputils.art.EloIcon;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.ScySearchResult;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButton;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButtonManager;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.client.desktop.desktoputils.jdom.JDomStringConversion;
import org.jdom.Element;
import eu.scy.common.mission.impl.jdom.JDomConversionUtils;
import javafx.scene.layout.Flow;
import eu.scy.client.desktop.desktoputils.art.eloicons.EloIconFactory;
import java.util.ArrayList;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.client.desktop.scydesktop.corners.elomanagement.ExtendedScyEloDisplayNode;
import eu.scy.client.desktop.desktoputils.StringUtils;
import java.lang.System;
import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.BigMissionMapControl;
import roolo.search.IQueryComponent;
import roolo.search.IQuery;
import javafx.scene.layout.VBox;
import java.lang.StringBuilder;

/**
 * @author SikkenJ
 */
public class EloSearchNode extends GridSearchResultsNode, Resizable, ScyToolFX, EloSaverCallBack, QuerySearchFinished {

   def logger = Logger.getLogger(this.getClass());
//   public override var width on replace { sizeChanged() };
//   public override var height on replace { sizeChanged() };
   public var window: ScyWindow;
   public var repository: IRepository;
   public var eloFactory: IELOFactory;
   public var toolBrokerAPI: ToolBrokerAPI on replace {
              repository = toolBrokerAPI.getRepository();
              eloFactory = toolBrokerAPI.getELOFactory();
           };
   public var scyDesktop: ScyDesktop;
   public var querySelecterFactory: QuerySelecterFactory;
   def minimumWidth = 150;
   def minimumHeight = 100;
   def queryLabel = Label {
           }
   def searchButton: Button = Button {
              text: ##"Search"
              disable: bind queryBox.rawText.trim() == ""
              action: function() {
                 doSearch();
              }
           };
   def queryBox: TextBox = TextBox {
              text: ""
              columns: 40
              selectOnFocus: true
              action: function() {
                 if (queryBox.rawText.trim() != "") {
                    doSearch();
                 }
              }
           };
   def suggestionsAndHistoryNode = SuggestionsAndHistoryNode {
              entrySelected: historySelected
           }
   def baseEloInfo = ExtendedScyEloDisplayNode {
              layoutInfo: eloInfoLayoutInfo
              newEloCreationRegistry: newEloCreationRegistry
              scyElo: bind baseElo
              eloIcon: bind baseEloIcon
           }
   var baseElo: ScyElo;
   var baseEloIcon: EloIcon;
   var backgroundQuerySearch: BackgroundQuerySearch;
   var scyElo: ScyElo;
   var technicalFormatKey: IMetadataKey;
   def scySearchType = "scy/search";
   var searchResults: ScySearchResult[];
   var scySearchResultXmlUtils: ScySearchResultXmlUtils;
   def jdomStringConversion = new JDomStringConversion();
   def contentTagName = "search";
   def queryTagName = "query";
   def querySelecterUsageTagName = "querySelecterUsage";
   def simpleTextQueryTagName = "simpleText";
   def eloUriTagName = "eloUri";
   def contextTagName = "context";
   def userNameTagName = "username";
   def missionSpecificationUriTagName = "missionSpecificationUri";
   def queryLastExcecutedTagName = "queryLastExcecuted";
   def saveTitleBarButton = TitleBarButton {
              actionId: TitleBarButton.saveActionId
              action: doSaveElo
           }
   def saveAsTitleBarButton = TitleBarButton {
              actionId: TitleBarButton.saveAsActionId
              action: doSaveAsElo
           }
   var queryGridRow: GridRow;
   var querySelecterUsage = QuerySelecterUsage.TEXT;
   var querySelecterDisplays: QuerySelecterDisplay[];
   def usedQuerySelecters: List = new ArrayList();
   def eloIconFactory = EloIconFactory {};
   var queryContentUserName: String;
   var queryContextMissionSpecificationUri: URI;
   var queryLastExcecuted: Long;
   var currentQuery: HistoryEntry;

   public override function initialize(windowContent: Boolean): Void {
      technicalFormatKey = toolBrokerAPI.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      eloFactory = toolBrokerAPI.getELOFactory();
      repository = toolBrokerAPI.getRepository();
      scySearchResultXmlUtils = new ScySearchResultXmlUtils(toolBrokerAPI);
      queryContentUserName = toolBrokerAPI.getLoginUserName();
      queryContextMissionSpecificationUri = toolBrokerAPI.getMissionSpecificationURI();
   }

   public override function setTitleBarButtonManager(titleBarButtonManager: TitleBarButtonManager, windowContent: Boolean): Void {
      if (windowContent) {
         titleBarButtonManager.titleBarButtons = [
                    saveTitleBarButton,
                    saveAsTitleBarButton
                 ]
      }
   }

   public override function newElo(): Void {
      createNodeContent();
   }

   public override function loadElo(uri: URI) {
      doLoadElo(uri);
   }

   public override function onQuit(): Void {
      if (scyElo != null) {
         def oldContentXml = scyElo.getContent().getXmlString();
         def newContentXml = getElo().getContent().getXmlString();
         if (oldContentXml == newContentXml) {
            // nothing changed
            return;
         }
      }
      doSaveElo();
   }

   public override function create(): Node {
      openAction = openElo;
      useAsBaseAction = searchBasedOnElo;
      windowStyler = scyDesktop.windowStyler;
      grid = Grid {
                 managed: false
                 hgap: spacing
                 vgap: spacing
                 padding: gridPadding
                 growRows: [0, 0, 0, 0, 0, 1, 0, 0]
                 growColumns: [0, 1]
              }
   }

   function getGridRows(): GridRow[] {
      [
         GridRow {
            cells: [
               getLeftColumnFiller(),
               queryLabel
            ]
         }
         if (QuerySelecterUsage.TEXT == querySelecterUsage) {
            GridRow {
               cells: [
                  getLeftColumnFiller(),
                  VBox {
                     spacing: spacing
                     content: [
                        HBox {
                           spacing: spacing
                           content: [
                              queryBox,
                              searchButton
                           ]
                        }
                        suggestionsAndHistoryNode
                     ]
                  }
               ]
            }
         } else if (QuerySelecterUsage.ELO_BASED == querySelecterUsage) {
            GridRow {
               cells: [
                  baseEloInfo
               ]
            }
         } else {
            null
         }
         GridRow {
            cells: [
               getLeftColumnFiller(),
               Flow {
                  hgap: 2 * spacing
                  vgap: spacing
                  nodeHPos: HPos.LEFT
                  content: querySelecterDisplays
               }
            ]
         }
         getDevideLine(),
         getResultDisplayPart(),
         GridRow {
            cells: [
               getLeftColumnFiller(),
               HBox {
                  spacing: spacing
                  layoutInfo: LayoutInfo {
                  //                           hpos: HPos.RIGHT
                  }
                  hpos: HPos.RIGHT
                  content: [
                     useAsBaseButton,
                     openButton
                  ]
               }
            ]
         }
      ]
   }

   function createNodeContent() {
      createQuerySelecterDisplays(querySelecterFactory.createQuerySelecters(querySelecterUsage));
      grid.rows = getGridRows();
      if (QuerySelecterUsage.TEXT == querySelecterUsage) {
         queryLabel.text = ##"Query";
      } else if (QuerySelecterUsage.ELO_BASED == querySelecterUsage) {
         queryLabel.text = ##"Search based on product";
      }
   }

   function createQuerySelecterDisplays(querySelecters: List) {
      querySelecterDisplays =
              for (querySelecterObject in querySelecters) {
                 def querySelecter = querySelecterObject as QuerySelecter;
                 querySelecter.setAuthorMode(scyDesktop.initializer.authorMode);
                 querySelecter.setDebugMode(scyDesktop.initializer.debugMode);
                 QuerySelecterDisplay {
                    querySelecter: querySelecter
                    querySelecterUsage: querySelecterUsage
                    eloIconFactory: eloIconFactory
                    windowColorScheme: window.windowColorScheme
                    tooltipManager: scyDesktop.tooltipManager
                    selectionChanged: querySelecterChanged
                    basedOnElo: baseElo
                    iconNameFilter: iconNameFilter
                 }
              }
      usedQuerySelecters.clear();
      for (querySelecterDisplay in querySelecterDisplays) {
         if (querySelecterDisplay.noOptions) {
            querySelecterDisplay.disable = true
         } else {
            usedQuerySelecters.add(querySelecterDisplay.querySelecter)
         }
      }
   }

   function iconNameFilter(iconName: String): String {
      if (scyDesktop.missionModelFX.missionMapButtonIconType != "") {
         if (iconName == BigMissionMapControl.defaultMissionMapIconName) {
            return scyDesktop.missionModelFX.missionMapButtonIconType
         }
      }
      return iconName
   }

   function querySelecterChanged(): Void {
      if (QuerySelecterUsage.TEXT == querySelecterUsage) {
         if (searchButton.disabled) {
            return
         }
      } else if (QuerySelecterUsage.ELO_BASED == querySelecterUsage) {
      }
      doSearch();
   }

   function historySelected(history: HistoryEntry): Void {
      queryBox.text = history.query;
      for (i in [0..sizeof querySelecterDisplays]) {
         querySelecterDisplays[i].setSelectedOption(history.selecterOptions[i])
      }
      doSearch();
   }

   function setSelecterFilters(query: IQuery) {
      for (querySelecterDisplay in querySelecterDisplays) {
         querySelecterDisplay.setFilterOptions(query)
      }
   }

   function doSearch(): Void {
      var query: IQueryComponent = null;
      if (QuerySelecterUsage.TEXT == querySelecterUsage) {
         def queryText = queryBox.rawText.trim();
         if (not StringUtils.isEmpty(queryText)) {
            query = new MetadataQueryComponent(queryText);
            currentQuery = HistoryEntry {
                       query: queryText
                       selecterOptions: for (querySelecterDisplay in querySelecterDisplays) {
                          querySelecterDisplay.getSelectedOption()
                       }
                    }
         }
      } else if (QuerySelecterUsage.ELO_BASED == querySelecterUsage) {
         def sb = new StringBuilder();
         for (keyword in baseElo.getKeywords()) {
            sb.append(keyword);
            sb.append(" ");
         }
         for (tag in baseElo.getTagNames()) {
            sb.append(tag);
            sb.append(" ");
         }
         sb.append(baseElo.getTitle());
         query = new MetadataQueryComponent("contents", sb);
      }
      if (query != null) {
         if (backgroundQuerySearch != null) {
            backgroundQuerySearch.abort();
         }
         def searchQuery = new Query(query);
         searchQuery.setIncludedEloTypes(scyDesktop.newEloCreationRegistry.getEloTypes());
         setSelecterFilters(searchQuery);
         def queryContext: QueryContext = createQueryContext(null);
         searchQuery.setQueryContext(queryContext);
         backgroundQuerySearch = new BackgroundQuerySearch(toolBrokerAPI, scyDesktop.newEloCreationRegistry, searchQuery, this);

         backgroundQuerySearch.start();
         showSearching();
      } else {
         setScySearchResults(new ArrayList());
      }
   }

   protected override function getByAuthors(): String[] {
      var byAuthors: String[] = [];
      if (scyElo != null) {
         def authorsList = scyElo.getAuthors();
         if (authorsList != null) {
            if (authorsList.size() != 1 or authorsList.get(0) != toolBrokerAPI.getLoginUserName()) {
               byAuthors = for (author in authorsList) {
                          author
                       }
            }
         }
      }
      return byAuthors
   }

   function createQueryContext(
           eloUri: String): QueryContext {
      def queryContext: QueryContext = new QueryContext();
      queryContext.setEloUri(eloUri);
      queryContext.setUsername(queryContentUserName);
      if (queryContextMissionSpecificationUri != null) {
         queryContext.setMission(queryContextMissionSpecificationUri.toASCIIString());
      }
      queryContext.setTool("search");
      return queryContext;
   }

   override public function querySearchFinished(scySearchResultList: List): Void {
      setScySearchResults(scySearchResultList);
   }

   function setScySearchResults(scySearchResultList: List) {
      def scySearchResults =
              for (scySearchResultObject in scySearchResultList) {
                 def scySearchResult = scySearchResultObject as ScySearchResult;
                 scySearchResult
              }
      addEloIconsToSearchResults(scySearchResults);
      queryLastExcecuted = System.currentTimeMillis();
      searchResults = scySearchResults;
      showSearchResult(searchResults);
   }

   override function showSearchResult(results: Object[]): Void {
      super.showSearchResult(results);
      setDateFound(queryLastExcecuted);
      if (QuerySelecterUsage.TEXT == querySelecterUsage) {
         if (currentQuery.query != "") {
            def historyEntry = HistoryEntry {
                       query: currentQuery.query
                       nrOfResults: sizeof results
                       selecterOptions: currentQuery.selecterOptions
                    }
            suggestionsAndHistoryNode.addHistoryEntry(historyEntry);
         }
      }
   }

   function addEloIconsToSearchResults(scySearchResults: ScySearchResult[]) {
      for (scySearchResult in scySearchResults) {
         def eloIcon: EloIcon = windowStyler.getScyEloIcon(scySearchResult.getScyElo());
         scySearchResult.setEloIcon(eloIcon);
      }
   }

   function openElo(): Void {
      scyDesktop.scyWindowControl.addOtherScyWindow(selectedSearchResult.getScyElo().getUri());
   }

   function searchBasedOnElo(scyElo: ScyElo): Void {
      querySelecterUsage = QuerySelecterUsage.ELO_BASED;
      setBasedOnElo(scyElo);
      createNodeContent();
      doSearch();
   }

   function setBasedOnElo(scyElo: ScyElo) {
      baseElo = scyElo;
      if (scyElo == null) {
         baseEloIcon = null;
      } else {
         baseEloIcon = windowStyler.getScyEloIcon(scyElo);
      }
   //      for (querySelecterDisplay in querySelecterDisplays) {
   //         querySelecterDisplay.querySelecter.setBasedOnElo(baseElo)
   //      }
   }

   function doLoadElo(eloUri: URI) {
      logger.info("Trying to load elo {eloUri}");
      var newScyElo = ScyElo.loadElo(eloUri, toolBrokerAPI);
      if (newScyElo != null) {
         loadEloContent(newScyElo.getContent().getXmlString());
         scyElo = newScyElo;
      }
   }

   function doSaveElo() {
      eloSaver.eloUpdate(getElo().getElo(), this);
   }

   function doSaveAsElo() {
      eloSaver.eloSaveAs(getElo().getElo(), this);
   }

   function getElo(): ScyElo {
      if (scyElo == null) {
         scyElo = ScyElo.createElo(scySearchType, toolBrokerAPI);
      }
      scyElo.getContent().setXmlString(getEloContent());
      return scyElo;
   }

   function getEloContent(): String {
      def root = new Element(contentTagName);
      root.addContent(JDomConversionUtils.createElement(querySelecterUsageTagName, querySelecterUsage));
      root.addContent(getContextXml());
      root.addContent(getQueryXml());
      root.addContent(scySearchResultXmlUtils.scySearchResultsToXml(searchResults));
      jdomStringConversion.xmlToString(root);
   }

   function getContextXml(): Element {
      def root = new Element(contextTagName);
      root.addContent(JDomConversionUtils.createElement(userNameTagName, queryContentUserName));
      root.addContent(JDomConversionUtils.createElement(missionSpecificationUriTagName, queryContextMissionSpecificationUri));
      root.addContent(JDomConversionUtils.createElement(queryLastExcecutedTagName, queryLastExcecuted));
      return root
   }

   function getQueryXml(): Element {
      def root = new Element(queryTagName);
      if (QuerySelecterUsage.TEXT == querySelecterUsage) {
         root.addContent(JDomConversionUtils.createElement(simpleTextQueryTagName, queryBox.rawText.trim()));
      } else if (QuerySelecterUsage.ELO_BASED == querySelecterUsage) {
         root.addContent(JDomConversionUtils.createElement(eloUriTagName, baseElo.getUri()));
      }
      root.addContent(querySelecterFactory.createQuerySelectersXml(usedQuerySelecters));
      return root
   }

   function loadEloContent(xmlString: String) {
      def root = jdomStringConversion.stringToXml(xmlString);
      querySelecterUsage = JDomConversionUtils.getEnumValue(QuerySelecterUsage.class, root, querySelecterUsageTagName);
      loadContext(root);
      def queryXml = root.getChild(queryTagName);
      if (QuerySelecterUsage.TEXT == querySelecterUsage) {
         loadSimpleQuery(queryXml);
      } else if (QuerySelecterUsage.ELO_BASED == querySelecterUsage) {
         loadEloBasedQuery(queryXml);
      }
      createNodeContent();
      def scySearchResultsXml = root.getChild(scySearchResultXmlUtils.SCYSEARCHRESULTS);
      def scySearchResults = scySearchResultXmlUtils.scySearchResultsFromXml(scySearchResultsXml);
      addEloIconsToSearchResults(scySearchResults);
      searchResults = scySearchResults;
      showSearchResult(searchResults);
   }

   function loadContext(root: Element) {
      def contextXml = root.getChild(contextTagName);
      queryContentUserName = contextXml.getChildTextTrim(userNameTagName);
      queryContextMissionSpecificationUri = JDomConversionUtils.getUriValue(contextXml, missionSpecificationUriTagName);
      queryLastExcecuted = JDomConversionUtils.getLongValue(contextXml, queryLastExcecutedTagName);
   }

   function loadSimpleQuery(root: Element) {
      def simpleTextQueryXml = root.getChild(simpleTextQueryTagName);
      if (simpleTextQueryXml != null) {
         queryBox.text = simpleTextQueryXml.getTextTrim();
      } else {
         queryBox.text = "";
      }
   }

   function loadEloBasedQuery(root: Element) {
      def baseEloUri = JDomConversionUtils.getUriValue(root, eloUriTagName);
      if (baseEloUri != null) {
         setBasedOnElo(ScyElo.loadMetadata(baseEloUri, toolBrokerAPI))
      } else {
         setBasedOnElo(null)
      }
   }

   public override function getPrefHeight(h: Number): Number {
      Math.max(minimumHeight, Math.max(grid.getPrefHeight(h), grid.boundsInParent.minY + grid.getPrefHeight(h)));
   }

   public override function getPrefWidth(w: Number): Number {
      Math.max(minimumWidth, grid.boundsInParent.minX + grid.getPrefWidth(w));
   }

   public override function getMinHeight(): Number {
      Math.max(minimumHeight, Math.max(grid.getMinHeight(), grid.boundsInParent.minY + grid.getMinHeight()));
   }

   public override function getMinWidth(): Number {
      Math.max(minimumWidth, grid.getMinWidth())
   }

   public override function loadedEloChanged(uri: URI): Void {
   //      eloUri = uri;
   }

   override public function eloSaveCancelled(elo: IELO): Void {
   }

   override public function eloSaved(elo: IELO): Void {
   }

}
