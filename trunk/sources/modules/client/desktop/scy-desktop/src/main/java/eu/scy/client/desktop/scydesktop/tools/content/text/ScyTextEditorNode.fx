/*
 * ScyTextEditorNode.fx
 *
 * Created on 30-nov-2009, 15:37:28
 */

package eu.scy.client.desktop.scydesktop.tools.content.text;

import javafx.ext.swing.SwingComponent;
import javafx.scene.CustomNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Resizable;
import javafx.scene.layout.VBox;
import java.awt.Dimension;
import java.net.URI;
import org.apache.log4j.Logger;

import roolo.api.IRepository;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IELO;

import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;

import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import org.jdom.Element;

import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;

/**
 * @author sikken
 */

public class ScyTextEditorNode extends CustomNode, Resizable, ScyToolFX {

   def logger = Logger.getLogger("eu.scy.client.desktop.scydesktop.tools.content.text.ScyTextEditorNode");
   def scyTextType = "scy/text";
//   def untitledDocName = "untitled";
   def textTagName = "text";
   def jdomStringConversion = new JDomStringConversion();

   public-init var textEditor:TextEditor;
   public-init var eloTextEditorActionWrapper:EloTextEditorActionWrapper;

   public var eloFactory:IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository:IRepository;

   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};

   var wrappedTextEditor:SwingComponent;

   var elo:IELO;

   var technicalFormatKey: IMetadataKey;

   public override function initialize():Void{
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
   }

   public override function loadElo(uri:URI){
      doLoadElo(uri);
   }

   def spacing = 5.0;

   public override function create(): Node {
      wrappedTextEditor = SwingComponent.wrap(textEditor);
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
                  wrappedTextEditor
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
//         IMetadataValueContainer metadataValueContainer = metadata.getMetadataValueContainer(titleKey);
//         // TODO fixe the locale problem!!!
//         Object titleObject = metadataValueContainer.getValue();
//         Object titleObject2 = metadataValueContainer.getValue(Locale.getDefault());
//         Object titleObject3 = metadataValueContainer.getValue(Locale.ENGLISH);
//
//         setDocName(titleObject3.toString());
         var text = eloContentXmlToText(newElo.getContent().getXmlString());
         textEditor.setText(text);
         logger.info("elo text loaded");
         elo = newElo;
      }
   }

   function doSaveElo(){
      elo.getContent().setXmlString(textToEloContentXml(textEditor.getText()));
      var savedElo = eloSaver.eloUpdate(getElo());
      if (savedElo!=null){
         elo = savedElo;
      }
   }

   function doSaveAsElo(){
      var savedElo = eloSaver.eloSaveAs(getElo());
      if (savedElo!=null){
         elo = savedElo;
      }
   }

   function getElo():IELO{
      if (elo==null){
         elo = eloFactory.createELO();
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyTextType);
      }
      elo.getContent().setXmlString(textToEloContentXml(textEditor.getText()));
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

   function resizeContent(){
      var size = new Dimension(width,height-wrappedTextEditor.boundsInParent.minY-spacing);
      // setPreferredSize is needed
      textEditor.setPreferredSize(size);
      // setSize is not visual needed
      // but set it, so the component can react to it
      textEditor.setSize(size);
      //println("resized whiteboardPanel to ({width},{height})");
   }

   public override function getPrefHeight(width: Number) : Number{
      return textEditor.getPreferredSize().getHeight();
   }

   public override function getPrefWidth(width: Number) : Number{
      return textEditor.getPreferredSize().getWidth();
   }

   public override function getMinHeight() : Number{
      return 100;
   }

   public override function getMinWidth() : Number{
      return 140;
   }
}
