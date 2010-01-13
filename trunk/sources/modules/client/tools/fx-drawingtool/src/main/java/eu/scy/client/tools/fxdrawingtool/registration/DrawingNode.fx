/*
 * DrawingNode.fx
 *
 * Created on 18-dec-2008, 15:19:52
 */

package eu.scy.client.tools.fxdrawingtool.registration;

import colab.vt.whiteboard.component.WhiteboardPanel;
import java.net.URI;
import javafx.ext.swing.SwingComponent;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;

import javafx.scene.control.Button;

import javafx.scene.CustomNode;
import javafx.scene.layout.Resizable;

import java.awt.Dimension;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import roolo.api.IRepository;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;


/**
 * @author sikkenj
 */

 // place your code here
public class DrawingNode extends CustomNode, Resizable, ScyToolFX {
   def logger = Logger.getLogger(this.getClass());
   def scyDrawingType = "scy/drawing";
   def jdomStringConversion = new JDomStringConversion();

   public-init var whiteboardPanel:WhiteboardPanel;
   public var eloFactory:IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository:IRepository;

   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};

   var wrappedWhiteboardPanel:SwingComponent;
   var technicalFormatKey: IMetadataKey;

   var elo:IELO;

   def spacing = 5.0;

   public override function initialize(windowContent: Boolean):Void{
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
   }

   public override function loadElo(uri:URI){
      doLoadElo(uri);
   }

   public override function create(): Node {
      wrappedWhiteboardPanel = SwingComponent.wrap(whiteboardPanel);
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
                  wrappedWhiteboardPanel
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
         whiteboardPanel.deleteAllWhiteboardContainers();
         whiteboardPanel.setContentStatus(jdomStringConversion.stringToXml(newElo.getContent().getXmlString()));
         logger.info("elo loaded");
         elo = newElo;
      }
   }

   function doSaveElo(){
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
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyDrawingType);
      }
      elo.getContent().setXmlString(jdomStringConversion.xmlToString(whiteboardPanel.getContentStatus()));
      return elo;
   }

   function resizeContent(){
      var size = new Dimension(width,height-wrappedWhiteboardPanel.boundsInParent.minY-spacing);
      // setPreferredSize is needed
      whiteboardPanel.setPreferredSize(size);
      // setSize is not visual needed
      // but set it, so the component react to it
      whiteboardPanel.setSize(size);
      //println("resized whiteboardPanel to ({width},{height})");
   }

   public override function getPrefHeight(width: Number) : Number{
      return whiteboardPanel.getPreferredSize().getHeight();
   }

   public override function getPrefWidth(width: Number) : Number{
      return whiteboardPanel.getPreferredSize().getWidth();
   }


   public override function getMinHeight() : Number{
      return 60;
   }

   public override function getMinWidth() : Number{
      return 140;
   }
}
