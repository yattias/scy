/*
 * ScyTextEditorNode.fx
 *
 * Created on 30-nov-2009, 15:37:28
 */
package eu.scy.client.desktop.scydesktop.tools.content.text;

import javafx.scene.CustomNode;
import javafx.scene.Group;
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
import eu.scy.client.desktop.scydesktop.swingwrapper.ScySwingWrapper;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButton;
import eu.scy.client.desktop.scydesktop.tools.TitleBarButtonManager;

/**
 * @author sikken
 */
public class SwingTextEditorNode extends CustomNode, Resizable, ScyToolFX, EloSaverCallBack {

   def logger = Logger.getLogger(this.getClass());
   def scyTextType = "scy/text";
   def textTagName = "text";
   def jdomStringConversion = new JDomStringConversion();
   public-init var textEditor: TextEditor;
   public var eloFactory: IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository: IRepository;
   public var toolBrokerAPI: ToolBrokerAPI on replace {
              eloFactory = toolBrokerAPI.getELOFactory();
              metadataTypeManager = toolBrokerAPI.getMetaDataTypeManager();
              repository = toolBrokerAPI.getRepository();
           };
   public override var width on replace { resizeContent() };
   public override var height on replace { resizeContent() };
   var wrappedTextEditor: Node;
   var elo: IELO;
   var technicalFormatKey: IMetadataKey;
   def saveTitleBarButton = TitleBarButton {
              actionId: "save"
              action: doSaveElo
           }
   def saveAsTitleBarButton = TitleBarButton {
              actionId: "saveAs"
              action: doSaveAsElo
           }

   public override function initialize(windowContent: Boolean): Void {
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

   def spacing = 0.0;

   public override function create(): Node {
      wrappedTextEditor = ScySwingWrapper.wrap(textEditor);
      resizeContent();
      FX.deferAction(resizeContent);
      wrappedTextEditor
//      return Group {
//                 blocksMouse: true;
//                 content: [
//                    VBox {
//                       translateY: spacing;
//                       spacing: spacing;
//                       content: [
//                          HBox {
//                             translateX: spacing;
//                             spacing: spacing;
//                             content: [
//                                Button {
//                                   text: "Save"
//                                   action: function() {
//                                      doSaveElo();
//                                   }
//                                }
//                                Button {
//                                   text: "Save as"
//                                   action: function() {
//                                      doSaveAsElo();
//                                   }
//                                }
//                             ]
//                          }
//                          wrappedTextEditor
//                       ]
//                    }
//                 ]
//              };
   }

   function doLoadElo(eloUri: URI) {
      logger.info("Trying to load elo {eloUri}");
      var newElo = repository.retrieveELO(eloUri);
      if (newElo != null) {
         var metadata = newElo.getMetadata();
         var text = eloContentXmlToText(newElo.getContent().getXmlString());
         textEditor.setText(text);
         logger.info("elo text loaded");
         elo = newElo;
      }
   }

   function doSaveElo() {
      elo.getContent().setXmlString(textToEloContentXml(textEditor.getText()));
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
      elo.getContent().setXmlString(textToEloContentXml(textEditor.getText()));
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

   function resizeContent(): Void {
      Container.resizeNode(wrappedTextEditor, width, height);
   }

   public override function getPrefHeight(width: Number): Number {
      return Container.getNodePrefHeight(wrappedTextEditor, width);
   }

   public override function getPrefWidth(width: Number): Number {
      return Container.getNodePrefWidth(wrappedTextEditor, width);
   }

   public override function getMinHeight(): Number {
      return 100;
   }

   public override function getMinWidth(): Number {
      return 140;
   }

}
