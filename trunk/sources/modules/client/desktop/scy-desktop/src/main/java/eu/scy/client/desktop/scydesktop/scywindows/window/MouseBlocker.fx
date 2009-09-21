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

/**
 * @author sikkenj
 */

var theStage:Stage;

def testBorder = 0;

var mouseBlockNode = Rectangle {
   blocksMouse:true;
   x: testBorder, y: testBorder
   width: 100, height: 100
//   fill: Color.color(.5,.5,.5,.5)
   fill: Color.TRANSPARENT
   stroke:null
}

var mouseBlockingActive = false;

public function initMouseBlocker(stage:Stage){
   theStage = stage;
   println("");
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
   insert mouseBlockNode into theStage.scene.content;
}

public function stopMouseBlocking(){
   checkInitialisation();
   if (not mouseBlockingActive){
      println("calling stopMouseBlocking, while mouse blocking is not active");
      return;
   }
   mouseBlockingActive = false;
   delete mouseBlockNode from theStage.scene.content;
}

function checkInitialisation(){
   if (theStage==null){
      throw new IllegalStateException("Please call MouseBlocker.initMouseBlocker before use");
   }
}
