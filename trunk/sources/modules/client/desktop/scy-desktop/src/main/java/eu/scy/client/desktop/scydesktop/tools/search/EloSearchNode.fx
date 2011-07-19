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
import eu.scy.client.desktop.scydesktop.corners.elomanagement.GridSearchNode;
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

/**
 * @author SikkenJ
 */
public class EloSearchNode extends GridSearchNode, Resizable, ScyToolFX, EloSaverCallBack, QuerySearchFinished {

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
   def minimumWidth = 150;
   def minimumHeight = 100;
   var searchButton: Button;
   public var queryBox: TextBox;
   var backgroundQuerySearch: BackgroundQuerySearch;
   var elo: IELO;
   var technicalFormatKey: IMetadataKey;
   def scySearchType = "scy/search";
   var searchResults: ScySearchResult[] on replace { showSearchResult(searchResults) }
   var scySearchResultXmlUtils: ScySearchResultXmlUtils;
   def jdomStringConversion = new JDomStringConversion();
   def contentTagName = "search";
   def queryTagName = "query";
   def simpleTextQueryTagName = "simpleText";
   def saveTitleBarButton = TitleBarButton {
              actionId: TitleBarButton.saveActionId
              action: doSaveElo
           }
   def saveAsTitleBarButton = TitleBarButton {
              actionId: TitleBarButton.saveAsActionId
              action: doSaveAsElo
           }

   public override function initialize(windowContent: Boolean): Void {
      technicalFormatKey = toolBrokerAPI.getMetaDataTypeManager().getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      eloFactory = toolBrokerAPI.getELOFactory();
      repository = toolBrokerAPI.getRepository();
      scySearchResultXmlUtils = new ScySearchResultXmlUtils(toolBrokerAPI);
   }

   public override function setTitleBarButtonManager(titleBarButtonManager: TitleBarButtonManager, windowContent: Boolean): Void {
      if (windowContent) {
         titleBarButtonManager.titleBarButtons = [
                    saveTitleBarButton,
                    saveAsTitleBarButton
                 ]
      }
   }

   public override function loadElo(uri: URI) {
      doLoadElo(uri);
   }

   public override function onQuit(): Void {
      if (elo != null) {
         def oldContentXml = elo.getContent().getXmlString();
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
      windowStyler = scyDesktop.windowStyler;
      grid = Grid {
                 managed: false
                 hgap: spacing
                 vgap: spacing
                 padding: gridPadding
                 growRows: [0, 0, 0, 0, 1, 0]
                 growColumns: [0, 1]
                 rows: [
                    GridRow {
                       cells: [
                          getLeftColumnFiller(),
                          Label {
                             text: ##"Query"
                          }
                       ]
                    }
                    GridRow {
                       cells: [
                          getLeftColumnFiller(),
                          HBox {
                             spacing: spacing
                             layoutInfo: LayoutInfo {
                             //                           hpos: HPos.RIGHT
                             }
                             content: [
                                queryBox = TextBox {
                                           text: ""
                                           columns: 40
                                           selectOnFocus: true
                                           action: function() {
                                              if (queryBox.rawText.trim() != "") {
                                                 doSearch();
                                              }
                                           }
                                        }
                                searchButton = Button {
                                           text: ##"Search"
                                           disable: bind queryBox.rawText.trim() == ""
                                           action: function() {
                                              doSearch();
                                           }
                                        }
                             ]
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
                                openButton,
                                cancelButton
                             ]
                          }
                       ]
                    }
                 ]
              }
   }

   function doSearch() {
      doTextQuerySearch()
   }

   function doTextQuerySearch(): Void {
      if (backgroundQuerySearch != null) {
         backgroundQuerySearch.abort();
      }

      def searchQuery = new Query(new MetadataQueryComponent(queryBox.rawText.trim()));
      def queryContext: QueryContext = createQueryContext(null);
      searchQuery.setQueryContext(queryContext);
      //        searchQuery.setAllowedEloTypes(getAllowedEloTypes());
      logger.info("Adding Query Context: {queryContext}");
      searchQuery.setAllowedEloTypes(scyDesktop.newEloCreationRegistry.getEloTypes());
      backgroundQuerySearch = new BackgroundQuerySearch(toolBrokerAPI, scyDesktop.newEloCreationRegistry, searchQuery, this);

      backgroundQuerySearch.start();
      showSearching();
   }

   function createQueryContext(eloUri: String): QueryContext {
      def queryContext: QueryContext = new QueryContext();
      queryContext.setEloUri(eloUri);
      queryContext.setUsername(toolBrokerAPI.getLoginUserName());
      queryContext.setMission(toolBrokerAPI.getMissionSpecificationURI().toASCIIString());
      queryContext.setTool("scy-simple-search");
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
      searchResults = scySearchResults;
   }

   function addEloIconsToSearchResults(scySearchResults: ScySearchResult[]) {
      for (scySearchResult in scySearchResults) {
         def eloIcon: EloIcon = windowStyler.getScyEloIcon(scySearchResult.getScyElo());
         scySearchResult.setEloIcon(eloIcon);
      }
   }

   function openElo(gridSearchNode: GridSearchNode): Void {
      scyDesktop.scyWindowControl.addOtherScyWindow(gridSearchNode.selectedSearchResult.getScyElo().getUri());
   }

   function doLoadElo(eloUri: URI) {
      logger.info("Trying to load elo {eloUri}");
      var newElo = repository.retrieveELO(eloUri);
      if (newElo != null) {
         loadEloContent(newElo.getContent().getXmlString());
         elo = newElo;
      //         FX.deferAction(function(): Void {
      //            textBox.text = text;
      //         });
      }
   }

   function doSaveElo() {
      eloSaver.eloUpdate(getElo(), this);
   }

   function doSaveAsElo() {
      eloSaver.eloSaveAs(getElo(), this);
   }

   function getElo(): IELO {
      if (elo == null) {
         elo = eloFactory.createELO();
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scySearchType);
      }
      elo.getContent().setXmlString(getEloContent());
      return elo;
   }

   function getEloContent(): String {
      def root = new Element(contentTagName);
      root.addContent(getQueryXml());
      root.addContent(scySearchResultXmlUtils.scySearchResultsToXml(searchResults));
      jdomStringConversion.xmlToString(root);
   }

   function getQueryXml(): Element{
      def root = new Element(queryTagName);
      root.addContent(JDomConversionUtils.createElement(simpleTextQueryTagName,queryBox.rawText.trim()));
      return root
   }

   function loadEloContent(xmlString: String) {
      def root = jdomStringConversion.stringToXml(xmlString);
      def queryXml = root.getChild(queryTagName);
      loadQuery(queryXml);
      def scySearchResultsXml = root.getChild(scySearchResultXmlUtils.SCYSEARCHRESULTS);
      def scySearchResults = scySearchResultXmlUtils.scySearchResultsFromXml(scySearchResultsXml);
      addEloIconsToSearchResults(scySearchResults);
      searchResults = scySearchResults;
   }

   function loadQuery(root: Element){
      def simpleTextQueryXml = root.getChild(simpleTextQueryTagName);
      if (simpleTextQueryXml!=null){
         queryBox.text = simpleTextQueryXml.getTextTrim();
      }
   }

//   function sizeChanged(): Void {
//      Container.resizeNode(grid, width, height);
//   }
//
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
