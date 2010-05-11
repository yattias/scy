package eu.scy.client.tools.fxfitex.registration;

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
import org.jdom.Element;
import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.ScyToolActionLogger;
import eu.scy.client.desktop.scydesktop.tools.EloSaverCallBack;
import roolo.api.IRepository;
import eu.scy.client.desktop.scydesktop.utils.log4j.Logger;
import eu.scy.client.desktop.scydesktop.utils.jdom.JDomStringConversion;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import eu.scy.client.desktop.scydesktop.scywindows.DatasyncAttribute;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;
import javax.swing.JOptionPane;
import eu.scy.client.common.datasync.ISynchronizable;
import eu.scy.client.common.datasync.DummySyncListener;
import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
import java.lang.System;
import eu.scy.client.desktop.scydesktop.edges.DatasyncEdge;

/**
 * @author Marjolaine
 */

public class FitexNode extends ISynchronizable, CustomNode, Resizable, ScyToolFX, EloSaverCallBack {
   def logger = Logger.getLogger(this.getClass());
   def scyFitexType = "scy/pds";
   def jdomStringConversion = new JDomStringConversion();

   public-init var fitexPanel:FitexPanel;
   public-init var scyWindow: ScyWindow;
   public var eloFactory:IELOFactory;
   public var metadataTypeManager: IMetadataTypeManager;
   public var repository:IRepository;
   public var toolBrokerAPI: ToolBrokerAPI;
   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};
   var wrappedFitexPanel:SwingComponent;
   var technicalFormatKey: IMetadataKey;
   var syncAttrib: DatasyncAttribute;
   var datasyncEdge: DatasyncEdge;
   var elo:IELO;
   def spacing = 5.0;

   var bundle:ResourceBundleWrapper;

  public override function canAcceptDrop(object: Object): Boolean {
        if (object instanceof ISynchronizable) {
            if ((object as ISynchronizable).getToolName().equals("simulator") or (object as ISynchronizable).getToolName().equals("fitex")) {
                return true;
            }
        }
        return false;
    }

    public function isDnDSimulator(object: ISynchronizable): Boolean{
        return (object as ISynchronizable).getToolName().equals("simulator");
    }

    public function isDnDFitex(object: ISynchronizable): Boolean{
        return (object as ISynchronizable).getToolName().equals("fitex");
    }

    public override function acceptDrop(object: Object) {
        logger.debug("drop accepted.");
        var isSync = isSynchronizingWith(object as ISynchronizable);
        if(isSync){
            removeDatasync(object as ISynchronizable);
        }else{
            var yesNoOptions = [getBundleString("YES"), getBundleString("NO")];
            var n = -1;
            if(isDnDSimulator(object as ISynchronizable)){
                n = JOptionPane.showOptionDialog( null,
                    getBundleString("MSG_SYNCHRONIZE_SIMULATOR"),               // question
                    getBundleString("TITLE_DIALOG_SYNCHRONIZE"),           // title
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,  // icon
                    null, yesNoOptions,yesNoOptions[0] );
                if (n == 0) {
                    initializeDatasync(object as ISynchronizable);
                }
            }else if(isDnDFitex(object as ISynchronizable)){
                    var question = getBundleString("MSG_MERGE_DATASET");
                    if(isSynchronizing()){
                        question = getBundleString("MSG_STOP_SYNC_BEFORE_MERGE");
                    }

                 n = JOptionPane.showOptionDialog( null,
                    question,               // question
                    getBundleString("TITLE_DIALOG_MERGE"),           // title
                    JOptionPane.YES_NO_CANCEL_OPTION,
                    JOptionPane.QUESTION_MESSAGE,  // icon
                    null, yesNoOptions,yesNoOptions[0] );
                if (n == 0) {
                    leave(null);
                    mergeDataset(object as ISynchronizable);
                }
            }
        }
   }

   /* return true is fitex is synchronizing with scysimulator with this sessionID*/
   function isSynchronizingWith(simulator : ISynchronizable) : Boolean {
       if(isDnDSimulator(simulator) and simulator.getSessionID() != null and getSessionID() != null and getSessionID().equals(simulator.getSessionID())){
            return true;
       }else{
            return false;
       }
   }

   /* return true is fitex is synchronizing: sessionID not null*/
   function isSynchronizing() : Boolean {
       return getSessionID() != null;
   }

   public function initializeDatasync(simulator: ISynchronizable) {
        var datasyncsession = toolBrokerAPI.getDataSyncService().createSession(new DummySyncListener());
        datasyncEdge = scyWindow.windowManager.scyDesktop.edgesManager.addDatasyncLink(syncAttrib, simulator.getDatasyncAttribute() as DatasyncAttribute);
        this.join(datasyncsession.getId());
        simulator.join(datasyncsession.getId(), datasyncEdge as Object);
        datasyncEdge.join(datasyncsession.getId(), toolBrokerAPI);
    }

    public function removeDatasync(simulator: ISynchronizable) {
        scyWindow.windowManager.scyDesktop.edgesManager.removeDatasyncLink(datasyncEdge);
        datasyncEdge = null;
        this.leave(simulator.getSessionID());
        simulator.leave(simulator.getSessionID());
    }

    public override function getDatasyncAttribute(): DatasyncAttribute {
        return syncAttrib;
    }

   public override function join(mucID: String) {
        fitexPanel.joinSession(mucID);
    }

    public override function join(mucID: String, edge: Object) {
        this.datasyncEdge = edge as DatasyncEdge;
        fitexPanel.joinSession(mucID);
    }

    public override function leave(mucID: String) {
        fitexPanel.leaveSession(mucID);
    }

    public override function getSessionID() {
        return fitexPanel.getSessionID();
    }

    public override function getToolName() {
        return "fitex";
    }

    public function mergeDataset(fitex: ISynchronizable){
        if(fitex instanceof FitexNode)
        fitexPanel.mergeELO((fitex as FitexNode).getDataset());
    }

    public function getDataset():Element{
        return fitexPanel.getPDS();
    }

    public override function initialize(windowContent: Boolean):Void{
      technicalFormatKey = metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT);
      fitexPanel.setTBI(toolBrokerAPI);
      fitexPanel.setEloUri((scyWindow.scyToolsList.actionLoggerTool as ScyToolActionLogger).getURI());
      syncAttrib = DatasyncAttribute{
                    scyWindow:scyWindow
                    dragAndDropManager:scyWindow.dragAndDropManager;
                    dragObject:this};
      insert syncAttrib into scyWindow.scyWindowAttributes;
      fitexPanel.initActionLogger();
      fitexPanel.initFitex();
   }

   public override function loadElo(uri:URI){
      doLoadElo(uri);
   }

   public override function create(): Node {
      bundle = new ResourceBundleWrapper(this);
      wrappedFitexPanel = SwingComponent.wrap(fitexPanel);
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
                           text: getBundleString("MENU_IMPORT_DATA");
                           action: function() {
                              doImportCSVFile();
                           }
                        }
//                        Button {
//                           text: "Merge dataset"
//                           action: function() {
//                              doMergeDataset();
//                           }
//                        }
                        Button {
                           text: getBundleString("MENU_SAVE");
                           action: function() {
                              doSaveElo();
                           }
                        }
                        Button {
                           text: getBundleString("MENU_SAVE_AS");
                           action: function() {
                                doSaveAsElo();
                           }
                        }
                        Button {
                           text: "flash";
                           action: function() {
                                datasyncEdge.flash();
                           }
                        }
                        /*Button {
                           text: "Synchronize"
                           action: function() {
                              doSynchronizeTool();
                           }
                        }*/
                     ]
                  }
                  wrappedFitexPanel
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
         fitexPanel.setEloUri(eloUri.toString());
         fitexPanel.loadELO(newElo.getContent().getXmlString());
         logger.info("elo loaded");
         elo = newElo;
      }
   }

   function doSaveElo(){
      eloSaver.eloUpdate(getElo(),this);
      fitexPanel.setEloUri(elo.getUri().toString());
   }

   function doSaveAsElo(){
      eloSaver.eloSaveAs(getElo(),this);
   }

   function getElo():IELO{
      if (elo==null){
         elo = eloFactory.createELO();
         elo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(scyFitexType);
      }
      elo.getContent().setXmlString(jdomStringConversion.xmlToString(fitexPanel.getPDS()));
      return elo;
   }

    override public function eloSaveCancelled (elo : IELO) : Void {
    }

    override public function eloSaved (elo : IELO) : Void {
        this.elo = elo;
        fitexPanel.setEloUri(elo.getUri().toString());
    }

   /*function doSynchronizeTool(){
       // get the mucID to join simulator session
       logger.info("Sync. Data Fitex");
       fitexPanel.synchronizeTool();
   }*/

   function doMergeDataset(){
       logger.info("Merge Fitex dataset");
       //fitexPanel.mergeELO(newElo.getContent().getXmlString());
   }

   function doImportCSVFile(){
       fitexPanel.importCsvFile();
   }

   function resizeContent(){
      var size = new Dimension(width,height-wrappedFitexPanel.boundsInParent.minY-spacing);
      // setPreferredSize is needed
      fitexPanel.setPreferredSize(size);
      // setSize is not visual needed
      // but set it, so the component react to it
      fitexPanel.setSize(size);
      //println("resized whiteboardPanel to ({width},{height})");
   }

   public override function getPrefHeight(width: Number) : Number{
      return fitexPanel.getPreferredSize().getHeight();
   }

   public override function getPrefWidth(width: Number) : Number{
      return fitexPanel.getPreferredSize().getWidth();
   }
   
   public override function getMinHeight() : Number{
      return 270;
   }

   public override function getMinWidth() : Number{
      return 460;
   }

   public function getBundleString(key:String) : String{
       return bundle.getString(key);
   }
   
}
