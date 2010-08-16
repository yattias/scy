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
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javafx.scene.control.Button;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import java.awt.Component;
import javafx.scene.image.ImageView;
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
import javax.swing.JFileChooser;
import roolo.elo.api.IELO;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.client.desktop.scydesktop.tools.content.eloImporter.ExampleFileFilter;
import eu.scy.client.desktop.scydesktop.tools.mission.springimport.SpringConfigFileImporter;

/**
 * @author sikken
 */
var lastUsedDirectory: File;

public class MissionSpecificationEditor extends CustomNode, Resizable, ScyToolFX, EloSaverCallBack {

   def logger = Logger.getLogger(this.getClass());
   public var eloFactory: IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository: IRepository;
   public var technicalType: String;
   public var scyWindow: ScyWindow;
   public var preserveRatio = true;
   public-init var extensions: String[];
   public override var width on replace {sizeChanged()};
   public override var height on replace {sizeChanged()};
   var elo: IELO;
   var technicalFormatKey: IMetadataKey;
   var titleKey: IMetadataKey;
   var image: ImageView;
   def jdomStringConversion = new JDomStringConversion();
   def textBox:TextBox = TextBox{
      multiline:true
      editable:true
      layoutInfo: LayoutInfo {
         hfill:true
         vfill:true
         hgrow: Priority.ALWAYS
         vgrow: Priority.ALWAYS
      }
   }
   var nodeBox: VBox;
   var buttonBox: HBox;
   def spacing = 5.0;
   def scyMissionSpecificationType = "scy/missionspecification";

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

   public override function loadElo(uri:URI){
      doLoadElo(uri);
   }

   function getParentComponent(): Component {
      return null;
   }

   function doImport(){
      var fileChooser = new JFileChooser();
      fileChooser.setCurrentDirectory(lastUsedDirectory);
      fileChooser.setFileFilter(new ExampleFileFilter("xml","Spring mission specification"));
      if (JFileChooser.APPROVE_OPTION == fileChooser.showOpenDialog(getParentComponent())) {
         //getting the file from the fileChooser
         //lastUsedDirectory = fileChooser.getCurrentDirectory();
         lastUsedDirectory = fileChooser.getCurrentDirectory();
         var springConfigFileImporter = SpringConfigFileImporter{
            file:fileChooser.getSelectedFile().getAbsolutePath()
            repository:repository
         }
         textBox.text = springConfigFileImporter.missionMapXml;
      }
   }


   function doLoadElo(eloUri:URI)
   {
      logger.info("Trying to load elo {eloUri}");
      var newElo = repository.retrieveELO(eloUri);
      if (newElo != null)
      {
         var metadata = newElo.getMetadata();
         textBox.text = newElo.getContent().getXmlString();
         elo = newElo;
      }
   }

   function doSaveElo(){
      elo.getContent().setXmlString(textBox.rawText);
      eloSaver.eloUpdate(getElo(),this);
   }

   function doSaveAsElo(){
      eloSaver.eloSaveAs(getElo(),this);
   }

   function getElo():IELO{
      if (elo==null){
         elo = eloFactory.createELO();
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyMissionSpecificationType);
      }
      elo.getContent().setXmlString(textBox.rawText);
      return elo;
   }


//   function textToEloContentXml(text:String):String{
//      var textElement= new Element(textTagName);
//      textElement.setText(text);
//      return jdomStringConversion.xmlToString(textElement);
//   }
//
//   function eloContentXmlToText(text:String):String{
//      var textElement=jdomStringConversion.stringToXml(text);
//      if (textTagName != textElement.getName()){
//         logger.error("wrong tag name, expected {textTagName}, but got {textElement.getName()}");
//      }
//      return textElement.getTextTrim();
//   }

    override public function eloSaveCancelled (elo : IELO) : Void {
    }

    override public function eloSaved (elo : IELO) : Void {
        this.elo = elo;
    }

    function sizeChanged(): Void {
      Container.resizeNode(nodeBox, width, height);
   }

   public override function getPrefHeight(h: Number) : Number{
      textBox.boundsInParent.minY + textBox.getPrefHeight(h);
   }

   public override function getPrefWidth(w: Number) : Number{
      textBox.boundsInParent.minX + textBox.getPrefWidth(w);
   }

   public override function getMinHeight() : Number{
     textBox.boundsInParent.minY + textBox.getMinHeight();
   }

   public override function getMinWidth() : Number{
      Math.max(buttonBox.getMinWidth(), textBox.getMinWidth())
   }

}
