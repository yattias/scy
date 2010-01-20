/*
 * MouseBlocker.fx
 *
 * Created on 21-sep-2009, 16:32:32
 */

package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.stage.Stage;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.lang.IllegalStateException;
import javafx.scene.input.MouseEvent;
import java.lang.IllegalArgumentException;

/**
 * @author sikkenj
 */

var theStage:Stage;

def testBorder = 0;

var mouseBlockNode:Rectangle = Rectangle {
   blocksMouse:true;
   x: testBorder, y: testBorder
   width: 100, height: 100
//   fill: Color.color(.5,.5,.5,.5)
   fill: Color.TRANSPARENT
//   stroke:null
   stroke:Color.color(.5,.5,.5,.5)
   strokeWidth:3.0
   onMouseClicked: function (e: MouseEvent): Void {
      if (mouseBlockingActive){
         println("mouse blocking disabled, by clicking on the mouseBlockNode");
         stopMouseBlocking();
      }
   }
   onMouseReleased: function (e: MouseEvent): Void {
      // there are situations that the normal mouse drag sequence is not ended by onMouseReleased
      // this happens when dragging action to open an window is stopped, before the window tools are created
      // the onMouseReleased action is then recieved by this node
      if (mouseBlockingActive){
         stopMouseBlocking();
      }
   }

}

var mouseBlockingActive = false;

public function initMouseBlocker(stage:Stage):Void{
   if (stage==null){
      throw new IllegalArgumentException("stage may not be null");
   }

   theStage = stage;
}

public function startMouseBlocking(){
   checkInitialisation();
   if (mouseBlockingActive){
      println("calling startMouseBlocking, while mouse blocking is already active");
      return;
   }
   mouseBlockingActive = true;
   mouseBlockNode.width = theStage.scene.width-2*testBorder;
   mouseBlockNode.height = theStage.scene.height-2*testBorder;
   //insert mouseBlockNode into theStage.scene.content;
   var sceneContent = theStage.scene.content;
   insert mouseBlockNode into sceneContent;
   theStage.scene.content = sceneContent;
}

public function stopMouseBlocking(){
   checkInitialisation();
   if (not mouseBlockingActive){
      println("calling stopMouseBlocking, while mouse blocking is not active");
      return;
   }
   mouseBlockingActive = false;
   //delete mouseBlockNode from theStage.scene.content;
   var sceneContent = theStage.scene.content;
   delete mouseBlockNode from sceneContent;
   theStage.scene.content = sceneContent;
}

function checkInitialisation(){
   if (theStage==null){
      throw new IllegalStateException("Please call MouseBlocker.initMouseBlocker before use");
   }
}
