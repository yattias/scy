/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.tools.fxyoutuber;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.layout.Resizable;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import java.lang.String;
import java.util.ArrayList;
import javafx.scene.control.ScrollBarPolicy;
import javafx.scene.control.ScrollView;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import java.net.URI;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import eu.scy.client.desktop.scydesktop.ScyDesktop;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButton;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButtonManager;
import eu.scy.client.desktop.desktoputils.XFX;

/**
 * @author pg
 */
public class YouTuberNode extends CustomNode, Resizable, ScyToolFX, ILoadXML, EloSaverCallBack {

   public-init var youTuberRepositoryWrapper: YouTuberRepositoryWrapper;
   public-init var scyDesktop: ScyDesktop;
   public var toolBrokerAPI: ToolBrokerAPI;
   public var repository: IRepository;
   public var eloFactory: IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var technicalFormatKey: IMetadataKey;
   public var spacing: Number on replace { requestLayout() }
   public var windowTitle: String;
   var scyYouTubeRType = "scy/youtuber";
   var elo: IELO;
   var eloUri: String = "n/a";
   var dataSets: ArrayList;
   var contentList: VBox = VBox {
              spacing: 5.0;
           };
   public-read var titleFont: Font = Font {
              size: 15.0;
           }
   public-read var textFont: Font = Font {
              size: 12.0;
           }
   var sv: ScrollView = ScrollView {
              node: bind contentList;
              style: "-fx-background-color: transparent;"
              vbarPolicy: ScrollBarPolicy.AS_NEEDED;
              hbarPolicy: ScrollBarPolicy.NEVER;
              layoutInfo: LayoutInfo {
                 height: bind height - 60;
                 width: bind width - 15;
              }
           }
   var nodes: Node[];
   var foreground: Node[];
   override var children = bind [nodes, foreground];
   var content: VBox = VBox {
              content: [
                 sv]
              spacing: 5.0;
           }
   var blocker: Rectangle = Rectangle {
              blocksMouse: true;
              height: bind height;
              width: bind width;
              translateX: 0;
              translateY: 0;
              fill: Color.BLACK;
              opacity: 0.7;
           }
   def saveTitleBarButton = TitleBarButton {
              actionId: "save"
              action: doSaveELO
           }
   def saveAsTitleBarButton = TitleBarButton {
              actionId: "saveAs"
              action: doSaveAsELO
           }
   def addVideoTitleBarButton = TitleBarButton {
              actionId: "import"
              iconType: "import"
              action: addUrl
              tooltip: ##"add YouTube Video"
           }

   postinit {
      insert content into nodes;
      dataSets = new ArrayList();
   }

   public function updateDataSet(id: Integer, dataSet: YouTuberDataSet): Void {
      if (id == -1) {
         dataSets.add(dataSet);
      } else {
         dataSets.set(id, dataSet);
      }
      //call refresh on GUI
      refreshGUIList();
   }

   public function getDataSet(id: Integer): YouTuberDataSet {
      return (dataSets.get(id) as YouTuberDataSet);
   }

   function addUrl(): Void {
      showPopup(YouTubeDataEditor {
         ytNode: this
         translateX: bind (width / 2) - (170);
         translateY: bind (height / 2) - (105);
      });
   //showPopup(Text { content: "foobar "});
   //println("added buttons");
   }

   function refreshGUIList(): Void {
      //delete contentList.content;
      contentList.content = [];

      for (i in [0..dataSets.size() - 1]) {
         var item = YouTuberItem {
                    dataSetID: i;
                    dataSet: (dataSets.get(i) as YouTuberDataSet);
                    ytNode: this;
                 }
         insert item into contentList.content;

      }
      insert Rectangle {
         height: 10;
         width: 10;
         fill: Color.TRANSPARENT;
         stroke: Color.TRANSPARENT
      } into contentList.content;
   }

   public function closePopup(item: Node): Void {
      delete item from foreground;
      delete blocker from foreground;
   }

   public function showPopup(item: Node): Void {
      insert blocker into foreground;
      insert item into foreground;
   //println("inserting {item}");
   }

   public function deleteItem(id: Integer): Void {
      dataSets.remove(id);
      refreshGUIList();
   }

   override function getPrefWidth(height: Number): Number {
      return 400;
   }

   override function getPrefHeight(width: Number): Number {
      return 400;
   }

   public function setFormAuthorRepositoryWrapper(wrapper: YouTuberRepositoryWrapper): Void {
      youTuberRepositoryWrapper = wrapper;
   }

   public override function postInitialize(): Void {
      youTuberRepositoryWrapper = new YouTuberRepositoryWrapper(this);
      youTuberRepositoryWrapper.setRepository(repository);
      youTuberRepositoryWrapper.setMetadataTypeManager(metadataTypeManager);
      youTuberRepositoryWrapper.setEloFactory(eloFactory);
   }

   public override function initialize(windowContent: Boolean): Void {
      repository = toolBrokerAPI.getRepository();
      metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
      eloFactory = toolBrokerAPI.getELOFactory();
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
   }

   public override function setTitleBarButtonManager(titleBarButtonManager: TitleBarButtonManager, windowContent: Boolean): Void {
      if (windowContent) {
         titleBarButtonManager.titleBarButtons = [
                    saveTitleBarButton,
                    saveAsTitleBarButton,
                    addVideoTitleBarButton
                 ]
      }
   }

   override function loadXML(xml: String): Void {
      this.dataSets = YTDataHandler.createSetFromString(xml);
      refreshGUIList();

   }

   override function getXML(): String {
      return YTDataHandler.createXMLDocument(dataSets);
   }

   override function setTitle(title: String): Void {
      youTuberRepositoryWrapper.setDocName(title);
   }

   public function browseElos(): Void {
      youTuberRepositoryWrapper.loadYTAction();
   }

   public function saveElo(): Void {
      youTuberRepositoryWrapper.saveYTAction();
   }

   public function createXML(): Void {
   }

   public override function loadElo(uri: URI) {
      XFX.runActionInBackground(function(): Void {
         doLoadELO(uri);
      })
   }

   function doLoadELO(eloUri: URI) {
      var newElo = repository.retrieveELO(eloUri);
      if (newElo != null) {
         FX.deferAction(function(): Void {
            loadXML(newElo.getContent().getXmlString());
            this.eloUri = eloUri.toString();
            elo = newElo;
         })
      }
   }

   function doSaveELO(): Void {
      eloSaver.eloUpdate(getELO(), this);
      this.eloUri = elo.getUri().toString(); // stolen from filtex, dont know why (:
   }

   function doSaveAsELO() {
      eloSaver.eloSaveAs(getELO(), this);
   }

   function getELO(): IELO {
      if (elo == null) {
         elo = eloFactory.createELO();
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyYouTubeRType);
      }
      elo.getContent().setXmlString(getXML());
      return elo;
   }

   public override function onQuit(): Void {
      if (elo != null) {
         def oldContentXml = elo.getContent().getXmlString();
         def newContentXml = getELO().getContent().getXmlString();
         if (oldContentXml == newContentXml) {
            // nothing changed
            return;
         }
      }
      doSaveELO();
   }

   override public function eloSaveCancelled(elo: IELO): Void {
   }

   override public function eloSaved(elo: IELO): Void {
      this.elo = elo;
      eloUri = elo.getUri().toString();
   }

}
