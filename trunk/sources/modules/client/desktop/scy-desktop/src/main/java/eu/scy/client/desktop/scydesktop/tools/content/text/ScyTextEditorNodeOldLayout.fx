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
import eu.scy.client.desktop.desktoputils.log4j.Logger;

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

/**
 * @author sikken
 */

public class ScyTextEditorNodeOldLayout extends CustomNode, Resizable, ScyToolFX, EloSaverCallBack {

   def logger = Logger.getLogger(this.getClass());

   def scyTextType = "scy/text";
   def textTagName = "text";
   def jdomStringConversion = new JDomStringConversion();

   public var eloFactory:IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository:IRepository;

   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};

   def textBox:TextBox = TextBox{
      multiline:true
      editable:true
      lines:100
      layoutInfo: LayoutInfo {
         height: bind textBox.height
         width: bind textBox.width
         hfill:true
         vfill:true
      }
   }

   var elo:IELO;

   var technicalFormatKey: IMetadataKey;

   public override function initialize(windowContent: Boolean):Void{
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
   }

   public override function loadElo(uri:URI){
      doLoadElo(uri);
   }

   def spacing = 5.0;

   public override function create(): Node {
      resizeContent();
      FX.deferAction(resizeContent);
      return Group {
         blocksMouse:true;
         content: [
            VBox{
               translateY:spacing;
               spacing:spacing;
               content:[
                  HBox{
                     translateX:spacing;
                     spacing:spacing;
                     content:[
                        Button {
                           text: "Save"
                           action: function() {
                              doSaveElo();
                           }
                        }
                        Button {
                           text: "Save as"
                           action: function() {
										doSaveAsElo();
                           }
                        }
                     ]
                  }
                  textBox
               ]
            }
         ]
      };
   }

   function doLoadElo(eloUri:URI)
   {
      logger.info("Trying to load elo {eloUri}");
      var newElo = repository.retrieveELO(eloUri);
      if (newElo != null)
      {
         var metadata = newElo.getMetadata();
         var text = eloContentXmlToText(newElo.getContent().getXmlString());
         textBox.text = text;
         logger.info("elo text loaded");
         elo = newElo;
      }
   }

   function doSaveElo(){
      elo.getContent().setXmlString(textToEloContentXml(textBox.rawText));
      eloSaver.eloUpdate(getElo(),this);
   }

   function doSaveAsElo(){
      eloSaver.eloSaveAs(getElo(),this);
   }

   function getElo():IELO{
      if (elo==null){
         elo = eloFactory.createELO();
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyTextType);
      }
      elo.getContent().setXmlString(textToEloContentXml(textBox.rawText));
      return elo;
   }


   function textToEloContentXml(text:String):String{
      var textElement= new Element(textTagName);
      textElement.setText(text);
      return jdomStringConversion.xmlToString(textElement);
   }

   function eloContentXmlToText(text:String):String{
      var textElement=jdomStringConversion.stringToXml(text);
      if (textTagName != textElement.getName()){
         logger.error("wrong tag name, expected {textTagName}, but got {textElement.getName()}");
      }
      return textElement.getTextTrim();
   }

    override public function eloSaveCancelled (elo : IELO) : Void {
    }

    override public function eloSaved (elo : IELO) : Void {
        this.elo = elo;
    }

   function resizeContent():Void{
      Container.resizeNode(textBox,width,height-textBox.boundsInParent.minY-spacing);
//      println("textBox resized to {width}*{height-textBox.boundsInParent.minY-spacing} (total: {width}*{height}, bounds: {textBox.boundsInParent})");
//      println("textBox columns: {textBox.columns}, lines: {textBox.lines}");
 }

   public override function getPrefHeight(suggestedHeight: Number) : Number{
      var suggestedTextBoxHeight = suggestedHeight-textBox.boundsInParent.minY-spacing;
      var prefHeight = Container.getNodePrefHeight(textBox, suggestedTextBoxHeight)+textBox.boundsInParent.minY+spacing;
      prefHeight = Math.max(prefHeight,height);
//      println("pref size: {Container.getNodePrefWidth(textBox, width)}*{prefHeight}");
//      println("max size: {this.getMaxWidth()}*{this.getMaxHeight()}");
      return prefHeight
   }

   public override function getPrefWidth(width: Number) : Number{
     return Container.getNodePrefWidth(textBox, width);
   }

   public override function getMinHeight() : Number{
      return 100;
   }

   public override function getMinWidth() : Number{
      return 140;
   }
}
