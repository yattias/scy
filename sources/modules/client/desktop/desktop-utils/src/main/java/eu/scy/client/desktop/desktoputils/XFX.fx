/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.desktoputils;

import eu.scy.client.desktop.desktoputils.JavaFXUIThreadRunner;
import eu.scy.client.desktop.desktoputils.JavaFXBackgroundRunner;
import javafx.animation.Timeline;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;

/**
 * @author giemza
 */
public function deferActionAndWait(action: function(): Void): Void {
   JavaFXUIThreadRunner {
      runner: action;
   }.start();
}

public function runActionInBackgroundAndCallBack(backgroundAction: function(): Object, callbackAction: function(result: Object): Void): Void {
   JavaFXBackgroundRunner {
      runner: backgroundAction;
      finished: callbackAction;
   }.start();
}

public function runActionAfter(action: function(): Void, waitCount: Integer): Void {
   if (waitCount < 0) {
      action()
   } else {
      runActionAfter(action, waitCount - 1)
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
