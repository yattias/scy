/*
 * ScyTextEditorNode.fx
 *
 * Created on 30-nov-2009, 15:37:28
 */
package eu.scy.client.desktop.scydesktop.tools.content.text;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Resizable;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Container;
import java.net.URI;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IELO;
import org.jdom.Element;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.common.mission.impl.jdom.JDomStringConversion;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import javafx.scene.control.TextBox;
import javafx.scene.layout.LayoutInfo;
import javafx.util.Math;
import javafx.scene.layout.Priority;
import javafx.geometry.Insets;
import java.awt.image.BufferedImage;
import javafx.geometry.BoundingBox;
import eu.scy.client.desktop.scydesktop.utils.ImageUtils;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButton;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButtonManager;

/**
 * @author sikken
 */
public class ScyTextEditorNode extends CustomNode, Resizable, ScyToolFX, EloSaverCallBack {

   def logger = Logger.getLogger(this.getClass());
   def scyTextType = "scy/text";
   def textTagName = "text";
   def jdomStringConversion = new JDomStringConversion();
   public var toolBrokerAPI: ToolBrokerAPI on replace {
         eloFactory = toolBrokerAPI.getELOFactory();
         metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
         repository = toolBrokerAPI.getRepository();
      };
   public var eloFactory: IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository: IRepository;
   public override var width on replace { sizeChanged() };
   public override var height on replace { sizeChanged() };
   def textBox: TextBox = TextBox {
         multiline: true
         editable: true
         managed: false
         layoutInfo: LayoutInfo {
            hfill: true
            vfill: true
            hgrow: Priority.ALWAYS
            vgrow: Priority.ALWAYS
         }
      }
   var nodeBox: VBox;
   var buttonBox: HBox;
   var elo: IELO;
   var technicalFormatKey: IMetadataKey;
   def saveTitleBarButton = TitleBarButton {
              actionId: "save"
              iconType: "save"
              action: doSaveElo
              tooltip: "save ELO"
           }
   def saveAsTitleBarButton = TitleBarButton {
              actionId: "saveAs"
              iconType: "save_as"
              action: doSaveAsElo
              tooltip: "save copy of ELO"
           }

   public override function initialize(windowContent: Boolean): Void {
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      eloFactory = toolBrokerAPI.getELOFactory();
      metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
      repository = toolBrokerAPI.getRepository();
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

   def spacing = 5.0;

   public override function create(): Node {
      //      resizeContent();
      //      FX.deferAction(resizeContent);
      textBox
//      nodeBox = VBox {
//            blocksMouse: true
//            managed: false
//            spacing: spacing;
//            content: [
//               buttonBox = HBox {
//                     spacing: spacing;
//                     padding: Insets {
//                        left: spacing
//                        top: spacing
//                        right: spacing
//                     }
//                     content: [
//                        Button {
//                           text: "Save"
//                           action: function() {
//                              doSaveElo();
//                           }
//                        }
//                        Button {
//                           text: "Save as"
//                           action: function() {
//                              doSaveAsElo();
//                           }
//                        }
//                     ]
//                  }
//               textBox
//            ]
//         }
   }

   function doLoadElo(eloUri: URI) {
      logger.info("Trying to load elo {eloUri}");
      var newElo = repository.retrieveELO(eloUri);
      if (newElo != null) {
         var text = eloContentXmlToText(newElo.getContent().getXmlString());
         logger.info("elo text loaded");
         elo = newElo;
         FX.deferAction(function(): Void {
            textBox.text = text;
         });
      }
   }

   function doSaveElo() {
      elo.getContent().setXmlString(textToEloContentXml(textBox.rawText));
      eloSaver.eloUpdate(getElo(), this);
   }

   function doSaveAsElo() {
      eloSaver.eloSaveAs(getElo(), this);
   }

   function getElo(): IELO {
      if (elo == null) {
         elo = eloFactory.createELO();
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyTextType);
      }
      elo.getContent().setXmlString(textToEloContentXml(textBox.rawText));
      return elo;
   }

   function textToEloContentXml(text: String): String {
      var textElement = new Element(textTagName);
      textElement.setText(text);
      return jdomStringConversion.xmlToString(textElement);
   }

   function eloContentXmlToText(text: String): String {
      var textElement = jdomStringConversion.stringToXml(text);
      if (textTagName != textElement.getName()) {
         logger.error("wrong tag name, expected {textTagName}, but got {textElement.getName()}");
      }
      return textElement.getTextTrim();
   }

   override public function eloSaveCancelled(elo: IELO): Void {
   }

   override public function eloSaved(elo: IELO): Void {
      this.elo = elo;
   }

   function sizeChanged(): Void {
//      Container.resizeNode(nodeBox, width, height);
      Container.resizeNode(textBox, width, height);
   }

   public override function getPrefHeight(h: Number): Number {
//      textBox.boundsInParent.minY + textBox.getPrefHeight(h);
      textBox.getPrefHeight(h)
   }

   public override function getPrefWidth(w: Number): Number {
//      textBox.boundsInParent.minX + textBox.getPrefWidth(w);
      textBox.getPrefWidth(w)
   }

   public override function getMinHeight(): Number {
//      textBox.boundsInParent.minY + textBox.getMinHeight();
      textBox.getMinHeight()
   }

   public override function getMinWidth(): Number {
//      Math.max(buttonBox.getMinWidth(), textBox.getMinWidth())
      textBox.getMinWidth()
   }

   public override function getThumbnail(width: Integer, height: Integer): BufferedImage {
      def bounds = BoundingBox {
            width: width
            height: height
         }
      def thumbnailImage = ImageUtils.nodeToSquareImage(textBox, bounds);
//      sizeChanged();
      thumbnailImage
   }

}
