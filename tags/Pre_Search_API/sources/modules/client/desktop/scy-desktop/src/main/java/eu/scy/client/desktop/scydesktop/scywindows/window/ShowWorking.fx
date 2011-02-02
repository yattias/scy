package eu.scy.client.desktop.scydesktop.scywindows.window;

import javafx.stage.Stage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import java.lang.IllegalStateException;
import java.lang.System;
import java.lang.IllegalArgumentException;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.Cursor;

/**
 * @author sikkenj
 */
var theStage: Stage;
var showWorkingActive = false;
def progressScale = 5.0;
def progressIndicator = ProgressIndicator {
      cursor: Cursor.WAIT
      progress: -1
      scaleX: progressScale
      scaleY: progressScale
   }
def mouseBlockRectangle: Rectangle = Rectangle {
      blocksMouse: true
      cursor: Cursor.WAIT
      x: 0, y: 0
      width: 100, height: 100
//         fill: Color.color(.5,.5,.5,.5)
      fill: Color.TRANSPARENT
   }
public def showProgressNode: Node = Group {
      visible: bind showWorkingActive
      content: [
         mouseBlockRectangle,
         progressIndicator
      ]
   }

function placeNodes() {
   mouseBlockRectangle.width = theStage.scene.width;
   mouseBlockRectangle.height = theStage.scene.height;
   progressIndicator.layoutX = theStage.scene.width / 2 - progressIndicator.layoutBounds.width / 2;
   progressIndicator.layoutY = theStage.scene.height / 2 - progressIndicator.layoutBounds.height / 2;
}

public function initShowWorking(stage: Stage): Void {
   if (stage == null) {
      System.err.println("initShowWorking called with a null stage! (theStage: {theStage})");
      try {
         throw new IllegalArgumentException("stage may not be null");
      }
      catch (e: IllegalArgumentException) {
         e.printStackTrace(System.err);
      }
   //throw new IllegalArgumentException("stage may not be null");
   } else {
      theStage = stage;
   }
}

public function startShowWorking(): Void {
   checkInitialisation();
   if (showWorkingActive) {
      println("calling startShowWorking, while show wait is already active");
      return;
   }
   placeNodes();
   showWorkingActive = true;
}

public function stopShowWorking(): Void {
   checkInitialisation();
   if (not showWorkingActive) {
      println("calling stopShowWorking, while show wait is not active");
      return;
   }
   showWorkingActive = false;
}

function checkInitialisation() {
   if (theStage == null) {
      throw new IllegalStateException("Please call ShowWorking.initShowWorking before use");
   }
}
