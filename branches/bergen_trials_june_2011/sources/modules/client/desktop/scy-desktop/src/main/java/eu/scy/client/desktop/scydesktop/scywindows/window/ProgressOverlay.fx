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
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import javafx.animation.Interpolator;

/**
 * @author sikkenj
 */
var theStage: Stage on replace {
        if (theStage != null and showWorkingActiveWhenPossible) {
            startShowWorking();
        }
    };
var showWorkingActive = false;
var showWorkingActiveWhenPossible = false;
def progressIndicator = ProgressIndicator {
      cursor: Cursor.WAIT
      scaleX: 10
      scaleY: 10
      opacity: 0
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
      content: [mouseBlockRectangle,
            progressIndicator]
}

function placeNodes() {
   mouseBlockRectangle.width = theStage.scene.width;
   mouseBlockRectangle.height = theStage.scene.height;
   progressIndicator.layoutX = theStage.scene.width / 2 - progressIndicator.layoutBounds.width / 2;
   progressIndicator.layoutY = theStage.scene.height / 2 - progressIndicator.layoutBounds.height / 2;
}

def fadeOutProgress = Timeline {
   keyFrames: [
        KeyFrame {
            canSkip: false
            time: 200ms
            values: [progressIndicator.opacity => 0 tween Interpolator.EASEIN]
            action: function() {
                showWorkingActive = false;
            }
        }
   ]
}

def fadeInProgress = Timeline {
   keyFrames: [
        KeyFrame {
            canSkip: false
            time: 200ms
            values: [progressIndicator.opacity => 0.3 tween Interpolator.EASEOUT]
        }
   ]
}

public function initOverlay(stage: Stage): Void {
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
   if (theStage == null) {
       showWorkingActiveWhenPossible = true;
   } else {
       if (showWorkingActive) {
          return;
       }
       placeNodes();
       showWorkingActive = true;
       fadeInProgress.playFromStart();
   }
}

public function stopShowWorking(): Void {
    if (theStage == null) {
        showWorkingActiveWhenPossible = false;
    } else {
        if (not showWorkingActive) {
            println("calling stopShowWorking, while show wait is not active");
            return;
        }
        fadeOutProgress.playFromStart();
    }
}