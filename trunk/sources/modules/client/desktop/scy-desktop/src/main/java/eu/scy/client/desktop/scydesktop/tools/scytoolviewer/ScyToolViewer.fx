/*
 * ScyToolViewer.fx
 *
 * Created on 12-jan-2010, 15:34:49
 */

package eu.scy.client.desktop.scydesktop.tools.scytoolviewer;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import eu.scy.client.desktop.scydesktop.tools.ScyToolFX;
import eu.scy.client.desktop.scydesktop.scywindows.ScyWindow;

import eu.scy.client.desktop.scydesktop.tools.EloSaver;
import eu.scy.client.desktop.scydesktop.tools.MyEloChanged;
import java.net.URI;

import javafx.ext.swing.SwingComponent;

import eu.scy.client.desktop.scydesktop.tools.content.text.TextEditor;

import javafx.scene.layout.Resizable;
import javafx.scene.layout.VBox;
import java.awt.Dimension;

import java.text.SimpleDateFormat;

import java.util.Date;

import roolo.api.IRepository;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.awareness.IAwarenessService;
import eu.scy.server.pedagogicalplan.PedagogicalPlanService;
import eu.scy.toolbrokerapi.ToolBrokerAPI;
import roolo.api.IExtensionManager;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IELOFactory;
import eu.scy.client.common.datasync.IDataSyncService;

/**
 * @author sikken
 */

// place your code here
public class ScyToolViewer  extends CustomNode,Resizable, ScyToolFX {

   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};

   public var toolBrokerAPI:ToolBrokerAPI on replace {serviceSet("toolBrokerAPI",toolBrokerAPI)};
   public var repository:IRepository on replace {serviceSet("repository",repository)};
   public var extensionManager:IExtensionManager on replace {serviceSet("extensionManager",extensionManager)};
   public var metadataTypeManager:IMetadataTypeManager on replace {serviceSet("metadataTypeManager",metadataTypeManager)};
   public var eloFactory:IELOFactory on replace {serviceSet("eloFactory",eloFactory)};
   public var actionLogger:IActionLogger on replace {serviceSet("actionLogger",actionLogger)};
   public var awarenessService:IAwarenessService on replace {serviceSet("awarenessService",awarenessService)};
   public var dataSyncService:IDataSyncService on replace {serviceSet("dataSyncService",dataSyncService)};
   public var pedagogicalPlanService:PedagogicalPlanService on replace {serviceSet("pedagogicalPlanService",pedagogicalPlanService)};
   public var scyWindow:ScyWindow on replace {addMessage("scyWindow set ({scyWindow.title})")};

   var uri = "?????";
   var location = "?";
   def textEditor = new TextEditor();
   def wrappedTextEditor = SwingComponent.wrap(textEditor);
   def spacing = 5.0;
   def dateTimeFormat = new SimpleDateFormat("HH:mm:ss");

   init{
      addMessage("class init");
   }

   postinit {
      addMessage("class postinit");
   }

   public override function create(): Node {
      addMessage("CustomNode.create");
      textEditor.setEditable(false);
      return VBox {
         spacing:spacing;
         layoutY:spacing
         content: [
            Text {
               font: Font {
                  size: 12
               }
               x: 0,
               y: 0
               content: bind "uri - {uri}"
            }
            Text {
               font: Font {
                  size: 12
               }
               x: 0,
               y: 0
               content: bind "Location - {location}"
            }
            wrappedTextEditor
         ]
      };
   }

   function serviceSet(name:String,service:Object){
      var message = "service {name} set";
      if (service==null){
         message = "{message}, but it is null!!!";
      }
      addMessage(message);
   }


   function addMessage(message:String){
      var newLine = "";
      var messages = textEditor.getText();
      if (messages.length()>0){
         newLine = "\n";
      }
      textEditor.setText("{textEditor.getText()}{newLine}{dateTimeFormat.format(new Date())} - {message}");
   }

   public override function initialize(windowContent: Boolean):Void{
      addMessage("initialize({windowContent})");
      location = if (windowContent) "window" else "drawer";
   }

   public override function postInitialize():Void{
      addMessage("postInitialize");
   }

   public override function newElo():Void{
      addMessage("newElo");
      uri = "no yet set";
   }

   public override function loadElo(eloUri:URI):Void{
      addMessage("loadElo {eloUri}");
      uri = "{eloUri}";
   }

   public override function loadedEloChanged(eloUri:URI):Void{
      addMessage("loadedEloChanged({eloUri})");
   }

   public override function onGotFocus():Void{
      addMessage("onGotFocus");
   }

   public override function onLostFocus():Void{
      addMessage("onLostFocus");
   }

   public override function onMinimized():Void{
      addMessage("onMinimized");
   }

   public override function onUnMinimized():Void{
      addMessage("onUnMinimized");
   }

   public override function onClosed():Void{
      addMessage("onClosed");
   }

   public override function aboutToClose():Boolean{
      addMessage("aboutToClose");
      return true;
   }

   public override function setEloSaver(eloSaver:EloSaver):Void{
   }

   public override function setMyEloChanged(myEloChanged:MyEloChanged):Void{
   }

   public override function canAcceptDrop(object:Object):Boolean{
      addMessage("canAcceptDrop of {object.getClass()}");
      return true;
   }

   public override function acceptDrop(object:Object):Void{
      addMessage("acceptDrop of {object.getClass()}");
   }

   function resizeContent(){
//      println("wrappedTextEditor.boundsInParent: {wrappedTextEditor.boundsInParent}");
//      println("wrappedTextEditor.layoutY: {wrappedTextEditor.layoutY}");
//      println("wrappedTextEditor.translateY: {wrappedTextEditor.translateY}");
      var size = new Dimension(width,height-wrappedTextEditor.boundsInParent.minY-spacing);
      // setPreferredSize is needed
      textEditor.setPreferredSize(size);
      // setSize is not visual needed
      // but set it, so the component react to it
      textEditor.setSize(size);
      //println("resized whiteboardPanel to ({width},{height})");
   }

   public override function getPrefHeight(width: Number) : Number{
      return textEditor.getPreferredSize().getHeight();
   }

   public override function getPrefWidth(width: Number) : Number{
      return textEditor.getPreferredSize().getWidth();
   }
}