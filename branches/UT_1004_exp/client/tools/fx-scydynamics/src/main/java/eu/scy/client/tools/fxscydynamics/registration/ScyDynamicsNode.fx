package eu.scy.client.tools.fxscydynamics.registration;

import java.net.URI;
import javafx.ext.swing.SwingComponent;
import javafx.scene.Group;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.CustomNode;
import javafx.scene.layout.Resizable;
import colab.um.xml.model.JxmModel;
import java.awt.Dimension;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import roolo.api.IRepository;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.client.tools.scydynamics.editor.ModelEditor;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.toolbrokerapi.ToolBrokerAPI;


public class ScyDynamicsNode extends CustomNode, Resizable, ScyToolFX, EloSaverCallBack {

   def logger = Logger.getLogger(this.getClass());
   def scyDynamicsType = "scy/model";
   def jdomStringConversion = new JDomStringConversion();

   public-init var modelEditor: ModelEditor;
   public-init var scyWindow: ScyWindow;
   public var eloFactory: IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository: IRepository;
   public var toolBrokerAPI: ToolBrokerAPI;
   public var actionLogger: IActionLogger;

   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};

   var wrappedModelEditor:SwingComponent;
   var technicalFormatKey: IMetadataKey;
   var elo:IELO;
   def spacing = 5.0;

   public override function initialize(windowContent: Boolean):Void{
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      modelEditor.setActionLogger(toolBrokerAPI.getActionLogger(), "dummy_user");
   }

   public override function loadElo(uri:URI){
      doLoadElo(uri);
   }

   public override function create(): Node {
      // note: the injected services are not yet available here
      // e.g., use the initialize(..) method
      wrappedModelEditor = SwingComponent.wrap(modelEditor);
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
                  wrappedModelEditor
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
         modelEditor.setNewModel();
	 modelEditor.setXmModel(JxmModel.readStringXML(newElo.getContent().getXmlString()));
         logger.info("elo loaded");
         elo = newElo;
      }
   }

   function doSaveElo(){
      eloSaver.eloUpdate(getElo(),this);
   }

   function doSaveAsElo(){
      eloSaver.eloSaveAs(getElo(),this);
   }

   function getElo():IELO{
      if (elo==null){
         elo = eloFactory.createELO();
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyDynamicsType);
      }
      var xmlString = modelEditor.getModelXML();
      if (xmlString.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\"?>")) {
        xmlString = xmlString.substring(39);
      }
      elo.getContent().setXmlString(xmlString);
      return elo;
   }

    override public function eloSaveCancelled (elo : IELO) : Void {
    }

    override public function eloSaved (elo : IELO) : Void {
        this.elo = elo;
    }

   function resizeContent(){
      var size = new Dimension(width,height-wrappedModelEditor.boundsInParent.minY-spacing);
      // setPreferredSize is needed
      modelEditor.setPreferredSize(size);
      // setSize is not visual needed
      // but set it, so the component react to it
      modelEditor.setSize(size);
//      println("resized whiteboardPanel to ({width},{height})");
   }

   public override function getPrefHeight(width: Number) : Number{
      return modelEditor.getPreferredSize().getHeight();
   }

   public override function getPrefWidth(width: Number) : Number{
      return modelEditor.getPreferredSize().getWidth();
   }


   public override function getMinHeight() : Number{
      return 140;
   }

   public override function getMinWidth() : Number{
      return 140;
   }
}
