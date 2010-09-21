/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.mission;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import roolo.elo.api.IMetadataKey;
import javafx.scene.control.Button;
import java.awt.Component;
import javafx.scene.layout.Resizable;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import java.io.File;
import javafx.geometry.Insets;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextBox;
import javafx.scene.layout.Container;
import javafx.scene.layout.LayoutInfo;
import javafx.scene.layout.Priority;
import javafx.util.Math;
import java.net.URI;
import roolo.elo.api.IELO;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.common.mission.impl.jdom.JDomStringConversion;
import java.lang.Exception;
import eu.scy.client.desktop.scydesktop.scywindows.scydesktop.ModalDialogBox;
import javafx.scene.text.Text;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.text.TextOrigin;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 * @author sikken
 */
protected var lastUsedDirectory: File = new File(".");

public abstract class EloXmlEditor extends CustomNode, Resizable, ScyToolFX, EloSaverCallBack {

   def logger = Logger.getLogger(this.getClass());
   public var eloFactory: IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository: IRepository;
   public var toolBrokerAPI : ToolBrokerAPI;
   public var window: ScyWindow;
   public override var width on replace { sizeChanged() };
   public override var height on replace { sizeChanged() };
   protected var elo: IELO;
   protected var technicalFormatKey: IMetadataKey;
   protected var titleKey: IMetadataKey;
   protected def textBox: TextBox = TextBox {
         multiline: true
         editable: true
         layoutInfo: LayoutInfo {
            hfill: true
            vfill: true
            hgrow: Priority.ALWAYS
            vgrow: Priority.ALWAYS
         }
      }
   var nodeBox: VBox;
   var buttonBox: HBox;
   def spacing = 5.0;
   protected def jdomStringConversion = new JDomStringConversion();

   override protected function create(): Node {
      nodeBox = VBox {
            managed: false
            blocksMouse: true;
            spacing: spacing;
            content: [
               buttonBox = HBox {
                     padding: Insets {
                        left: spacing
                        top: spacing
                        right: spacing
                     }
                     spacing: spacing;
                     content: [
                        Button {
                           text: ##"Save"
                           action: function() {
                              doSaveElo();
                           }
                        }
                        Button {
                           text: ##"Save as"
                           action: function() {
                              doSaveAsElo();
                           }
                        }
                        Button {
                           text: ##"Import"
                           action: function() {
                              doImport();
                           }
                        }
                     ]
                  }
               textBox
            ]
         }
   }

   public override function initialize(windowContent: Boolean): Void {
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      titleKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE);
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

   protected function getParentComponent(): Component {
      return null;
   }

   abstract function doImport(): Void;

   function doLoadElo(eloUri: URI) {
      logger.info("Trying to load elo {eloUri}");
      var newElo = repository.retrieveELO(eloUri);
      if (newElo != null) {
         var metadata = newElo.getMetadata();
         textBox.text = newElo.getContent().getXmlString();
         elo = newElo;
         // just check and display the errors
         // no special actions needed when the xml is wrong
         isValidXml(newElo.getContent().getXmlString());
      }
   }

   function doSaveElo() {
      var theElo = getElo();
      if (theElo != null) {
         eloSaver.eloUpdate(theElo, this);
      }
   }

   function doSaveAsElo() {
      var theElo = getElo();
      if (theElo != null) {
         eloSaver.eloSaveAs(theElo, this);
      }
   }

   function getElo(): IELO {
      if (elo == null) {
         elo = eloFactory.createELO();
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(getEloType());
      }
      var eloXml = getEloXml();
      if (not isValidXml(eloXml)) {
         return null;
      }

      elo.getContent().setXmlString(eloXml);
      return elo;
   }

   function isValidXml(xml: String): Boolean {
      var errors = validateXml(xml);
      if (errors != null) {
         showErrorMessage("Errors in the xml", errors);
         return false;
      }
      return true;
   }

   protected function showErrorMessage(title: String, message: String): Void {
      ModalDialogBox {
         content: Text {
            x: 0, y: 0
            textOrigin: TextOrigin.TOP
            content: message
         }
         title: title
         windowColorScheme: window.windowColorScheme
//         targetScene: this.scene
      }
   }

   abstract function getEloType(): String;

   function getEloXml(): String {
      textBox.rawText
   }

   protected function validateXml(xml: String): String {
      try {
         var root = jdomStringConversion.stringToXml(xml);
      }
      catch (e: Exception) {
         return e.getMessage()
      }
      return null;
   }

   override public function eloSaveCancelled(elo: IELO): Void {
   }

   override public function eloSaved(elo: IELO): Void {
      this.elo = elo;
   }

   function sizeChanged(): Void {
      Container.resizeNode(nodeBox, width, height);
   }

   public override function getPrefHeight(h: Number): Number {
      textBox.boundsInParent.minY + textBox.getPrefHeight(h);
   }

   public override function getPrefWidth(w: Number): Number {
      textBox.boundsInParent.minX + textBox.getPrefWidth(w);
   }

   public override function getMinHeight(): Number {
      textBox.boundsInParent.minY + textBox.getMinHeight();
   }

   public override function getMinWidth(): Number {
      Math.max(buttonBox.getMinWidth(), textBox.getMinWidth())
   }

}
