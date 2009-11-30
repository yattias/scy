/*
 * DummyScyToolWindowContent.fx
 *
 * Created on 30-nov-2009, 12:16:13
 */

package eu.scy.client.desktop.scydesktop.dummy;

import javafx.scene.CustomNode;
import javafx.scene.Node;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import eu.scy.client.desktop.scydesktop.tools.ScyTool;

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

/**
 * @author sikken
 */

// place your code here
public class DummyScyToolWindowContent  extends CustomNode,Resizable, ScyTool {

   public override var width on replace {resizeContent()};
   public override var height on replace {resizeContent()};

   public var repository:IRepository on replace {addMessage("repository set")};

   var uri = "?????";
   def textEditor = new TextEditor();
   def wrappedTextEditor = SwingComponent.wrap(textEditor);
   def spacing = 5.0;
   def dateTimeFormat = new SimpleDateFormat("HH:mm:ss");

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
            wrappedTextEditor
         ]
      };
   }

   function addMessage(message:String){
      var newLine = "";
      var messages = textEditor.getText();
      if (messages.length()>0){
         newLine = "\n";
      }
      textEditor.setText("{textEditor.getText()}{newLine}{dateTimeFormat.format(new Date())} - {message}");
   }

   public override function initialize():Void{
      addMessage("initialize");
   }

   public override function newElo():Void{
      addMessage("newElo");
      uri = "no yet set";
   }

   public override function loadElo(eloUri:URI):Void{
      addMessage("loadElo {eloUri}");
      uri = "{eloUri}";
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

   public override function setEloSaver(eloSaver:EloSaver):Void{
   }

   public override function setMyEloChanged(myEloChanged:MyEloChanged):Void{
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