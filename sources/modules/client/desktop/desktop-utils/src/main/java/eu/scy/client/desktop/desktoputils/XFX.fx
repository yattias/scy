/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.desktoputils;

import eu.scy.client.desktop.desktoputils.JavaFXUIThreadRunner;
import eu.scy.client.desktop.desktoputils.JavaFXBackgroundRunner;
import javafx.animation.Timeline;
import javafx.animation.KeyFrame;
import java.lang.Exception;

/**
 * @author giemza
 */
public function deferActionAndWait(action: function(): Void): Void {
   JavaFXUIThreadRunner {
      runner: action
   }.start();
}

public function runActionInBackgroundAndCallBack(backgroundAction: function(): Object, callbackAction: function(result: Object): Void): Void {
   runActionInBackgroundAndCallBack(backgroundAction, callbackAction, getDefaultThreadName())
}

public function runActionInBackgroundAndCallBack(backgroundAction: function(): Object, callbackAction: function(result: Object): Void, threadName: String): Void {
   JavaFXBackgroundRunner {
      runner: backgroundAction
      finished: callbackAction
      threadName: threadName
   }.start();
}

public function runActionInBackground(backgroundAction: function(): Void): Void {
   runActionInBackground(backgroundAction, getDefaultThreadName())
}

public function runActionInBackground(backgroundAction: function(): Void, threadName: String): Void {
   JavaFXBackgroundRunner {
      runner: backgroundAction
      threadName: threadName
   }.start();
}

public function runActionAfter(action: function(): Void, waitCount: Integer): Void {
   if (waitCount < 0) {
      action()
   } else {
      FX.deferAction(function(): Void {
         runActionAfter(action, waitCount - 1)
      });
   }
}

public function runActionInBackgroundAfter(action: function(): Void, waitCount: Integer): Void {
   runActionInBackgroundAfter(action, waitCount, getDefaultThreadName())
}

public function runActionInBackgroundAfter(action: function(): Void, waitCount: Integer, threadName: String): Void {
   if (waitCount < 0) {
      JavaFXBackgroundRunner {
         runner: action
         threadName: threadName
      }.start();
   } else {
      FX.deferAction(function(): Void {
         runActionInBackgroundAfter(action, waitCount - 1)
      })
   }
}

public function runActionAfter(action: function(): Void, duration: Duration): Void {
   Timeline {
      repeatCount: 1
      keyFrames: [
         KeyFrame {
            time: duration
            action: action
         }
      ];
   }.play()
}

public function runActionInBackgroundAfter(action: function(): Void, duration: Duration): Void {
   runActionInBackgroundAfter(action, duration, getDefaultThreadName())
}

public function runActionInBackgroundAfter(action: function(): Void, duration: Duration, threadName: String): Void {
   Timeline {
      repeatCount: 1
      keyFrames: [
         KeyFrame {
            time: duration
            action: function(): Void {
               JavaFXBackgroundRunner {
                  runner: action
                  threadName: threadName
               }.start();
            }
         }
      ];
   }.play()
}

def callerIndex = 2;

function getDefaultThreadName(): String {
   try {
      throw new Exception()
   } catch (e: Exception) {
      def stackTraceElements = e.getStackTrace();
      if (sizeof stackTraceElements >= callerIndex) {
         def callerStackTraceElement = stackTraceElements[callerIndex];
         return "{callerStackTraceElement.getClassName()}.{callerStackTraceElement.getMethodName()}:{callerStackTraceElement.getLineNumber()}"
      }
   }
   null

}
