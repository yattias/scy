/*
 * ImageViewer.fx
 *
 * Created on 2-feb-2010, 20:07:55
 */
package eu.scy.client.desktop.scydesktop.tools.imageviewer;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import eu.scy.client.desktop.desktoputils.log4j.Logger;
import roolo.api.IRepository;
import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import java.net.URI;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.control.Button;
import roolo.elo.api.IContent;
import eu.scy.common.mission.impl.jdom.JDomStringConversion;
import org.jdom.Element;
import javax.swing.JFileChooser;
import java.awt.Component;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.scene.Group;
import javafx.scene.layout.Resizable;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import java.io.File;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 * @author sikken
 */
var lastUsedDirectory: File;

public class ImageViewer extends CustomNode, Resizable, ScyToolFX, EloSaverCallBack {

   def logger = Logger.getLogger(this.getClass());
   public var toolBrokerAPI: ToolBrokerAPI on replace {
         eloFactory = toolBrokerAPI.getELOFactory();
         metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
         repository = toolBrokerAPI.getRepository();
      };
   public var eloFactory: IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository: IRepository;
   public var technicalType: String;
   public var scyWindow: ScyWindow;
   public var preserveRatio = true;
   public-init var extensions: String[];
   public override var width on replace { resizeContent() };
   public override var height on replace { resizeContent() };
   var elo: IELO;
   var technicalFormatKey: IMetadataKey;
   var titleKey: IMetadataKey;
   var image: ImageView;
   def jdomStringConversion = new JDomStringConversion();
   def imageUrlTagName = "imageUrl";
   def scyImageype = "scy/image";
   def contentGroup = Group {
      }

   override protected function create(): Node {
      contentGroup.content =
         Button {
            text: "Load"
            action: askUserForFile
         };
      contentGroup
   }

   public override function initialize(windowContent: Boolean): Void {
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      titleKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE);
   }

   function getParentComponent(): Component {
      return null;
   }

   function askUserForFile(): Void {
      var fileChooser = new JFileChooser();
      fileChooser.setCurrentDirectory(lastUsedDirectory);
      if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(getParentComponent())) {
         //getting the file from the fileChooser
         //lastUsedDirectory = fileChooser.getCurrentDirectory();
         lastUsedDirectory = fileChooser.getCurrentDirectory();
         var imageUrl = "{fileChooser.getSelectedFile().toURL()}";
         showImage(imageUrl);
         var eloTitle = fileChooser.getSelectedFile().getName();
         var lastPointPos = eloTitle.lastIndexOf(".");
         if (lastPointPos >= 0) {
            eloTitle = eloTitle.substring(0, lastPointPos);
         }
         getElo().getMetadata().getMetadataValueContainer(titleKey).setValue(eloTitle);
         eloSaver.eloUpdate(elo, this);
      }
   }

   function showImage(imageUrl: String) {
      //      println("showImage({imageUrl})");
      image = ImageView {
            image: Image {
               url: imageUrl
            }
            preserveRatio: preserveRatio
         }
      contentGroup.content = image;
      scyWindow.open();
   }

   function getElo(): IELO {
      if (elo == null) {
         elo = eloFactory.createELO();
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyImageype);
      }
      setUrlInContent(elo.getContent(), image.image.url);
      return elo;
   }

   public override function loadElo(uri: URI) {
      elo = repository.retrieveELO(uri);
      if (elo == null) {
         logger.error("the elo does not exists: {uri}");
         return;
      }
      showImage(getUrlFromContent(elo.getContent()));
   }

   function getUrlFromContent(content: IContent): String {
      var contentXml = jdomStringConversion.stringToXml(content.getXmlString());
      return contentXml.getTextTrim();
   }

   function setUrlInContent(content: IContent, url: String) {
      var contentXml = new Element(imageUrlTagName);
      contentXml.setText(url);
      content.setXmlString(jdomStringConversion.xmlToString(contentXml));
   }

   public override function onOpened(): Void {
      if (elo != null) {
         image.image = Image {
               url: getUrlFromContent(elo.getContent())
            }
      //         showImage(getUrlFromContent(elo.getContent()));
      }
   }

   override public function getPrefWidth(arg0: Number): Number {
      if (image == null) {
         return arg0;
      }
      return image.image.width
   }

   override public function getPrefHeight(arg0: Number): Number {
      if (image == null) {
         return arg0;
      }
      return image.image.height
   }

   public override function getMinHeight(): Number {
      return 30;
   }

   public override function getMinWidth(): Number {
      return 50;
   }

   function resizeContent() {
      if (image != null) {
         image.fitWidth = width;
         image.fitHeight = height;
      }
   }

   override public function eloSaveCancelled(arg0: IELO): Void {

   }

   override public function eloSaved(arg0: IELO): Void {
      elo = arg0;
   }

}
