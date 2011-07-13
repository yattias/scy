/*
 * DrawingNode.fx
 *
 * Created on 18-dec-2008, 15:19:52
 */
package eu.scy.client.tools.fxdrawingtool.registration;

import colab.vt.whiteboard.component.WhiteboardPanel;
import java.net.URI;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.CustomNode;
import javafx.scene.layout.Resizable;
import javafx.scene.layout.Container;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import roolo.api.IRepository;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;
import javafx.util.Math;
import java.awt.Dimension;
import java.awt.image.BufferedImage;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButton;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButtonManager;
import eu.scy.client.desktop.desktoputils.UiUtils;
import eu.scy.client.desktop.desktoputils.jdom.JDomStringConversion;
import org.apache.log4j.Logger;
import eu.scy.client.desktop.desktoputils.XFX;

/**
 * @author sikkenj
 */
// place your code here
public class DrawingNode extends CustomNode, Resizable, ScyToolFX, EloSaverCallBack {

   def logger = Logger.getLogger(this.getClass());
   def scyDrawingType = "scy/drawing";
   def jdomStringConversion = new JDomStringConversion();
   public-init var whiteboardPanel: WhiteboardPanel;
   public-init var scyWindow: ScyWindow;
   public var toolBrokerAPI: ToolBrokerAPI;
   public var eloFactory: IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository: IRepository;
   public override var width on replace {sizeChanged()};
   public override var height on replace {sizeChanged()};
   var wrappedWhiteboardPanel: Node;
   var technicalFormatKey: IMetadataKey;
   var elo: IELO;
   var nodeBox: VBox;
   var buttonBox: HBox;
   def spacing = 5.0;
   def saveTitleBarButton = TitleBarButton {
              actionId: TitleBarButton.saveActionId
              action: doSaveElo
           }
   def saveAsTitleBarButton = TitleBarButton {
              actionId: TitleBarButton.saveAsActionId
              action: doSaveAsElo
           }

//   def cached = bind scyWindow.cache on replace {
//         wrappedWhiteboardPanel.cache = cached;
//         println("changed wrappedWhiteboardPanel.cache to {wrappedWhiteboardPanel.cache}");
//      }
   public override function initialize(windowContent: Boolean): Void {
       metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
       repository = toolBrokerAPI.getRepository();
       eloFactory = toolBrokerAPI.getELOFactory();

      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
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

   public override function onQuit():Void{
      if (elo!=null){
         def oldContentXml = elo.getContent().getXmlString();
         def newContentXml = getElo().getContent().getXmlString();
         if (oldContentXml==newContentXml){
            // nothing changed
            return;
         }
      }
      doSaveElo();
   }

   public override function getThumbnail(width: Integer, height: Integer): BufferedImage {
        return UiUtils.createThumbnail(whiteboardPanel, whiteboardPanel.getSize(), new Dimension(width, height));
    }

   public override function create(): Node {
      wrappedWhiteboardPanel = ScySwingWrapper.wrap(whiteboardPanel);
      //wrappedWhiteboardPanel.cache = true;
      wrappedWhiteboardPanel
   }

   function doLoadElo(eloUri: URI) {
      logger.info("Trying to load elo {eloUri}");
      var newElo = repository.retrieveELO(eloUri);
      if (newElo != null) {
         whiteboardPanel.deleteAllWhiteboardContainers();
         whiteboardPanel.setContentStatus(jdomStringConversion.stringToXml(newElo.getContent().getXmlString()));
         logger.info("elo loaded");
         elo = newElo;
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
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyDrawingType);
      }
      elo.getContent().setXmlString(jdomStringConversion.xmlToString(whiteboardPanel.getContentStatus()));
      return elo;
   }

   override public function eloSaveCancelled(elo: IELO): Void {
   }

   override public function eloSaved(elo: IELO): Void {
      this.elo = elo;
      println("DrawerNode.eloSaved({elo.getUri()})");
   }

   function sizeChanged(): Void {
      Container.resizeNode(wrappedWhiteboardPanel, width, height);
   }

   public override function getPrefHeight(h: Number): Number {
      Container.getNodePrefHeight(wrappedWhiteboardPanel, h);
   }

   public override function getPrefWidth(w: Number): Number {
      Container.getNodePrefWidth(wrappedWhiteboardPanel, w);
   }

   public override function getMinHeight(): Number {
      Math.max(30, Container.getNodeMinHeight(wrappedWhiteboardPanel));
   }

   public override function getMinWidth(): Number {
      Container.getNodeMinWidth(wrappedWhiteboardPanel)
   }

}
