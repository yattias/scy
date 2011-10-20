/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.desktoputils;

import java.lang.Runnable;
import java.lang.Thread;

/**
 * @author SikkenJ
 */
public class JavaFXBackgroundRunner extends Runnable {

   public var runner: function(): Object;
   public var finished: function(result: Object): Void;
   public var threadName: String;

   override public function run(): Void {
      var result: Object = null;
      try {
         result = runner();
      } finally {
         if (finished != null) {
            FX.deferAction(function(): Void {
               finished(result)
            });
         }
      }
   }

   public function start() {
      def thread = new Thread(this);
      if (threadName != null) {
         thread.setName(threadName);
      }
      thread.start();
   }

}
