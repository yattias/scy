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

   override public function run(): Void {
      def result = runner();
      FX.deferAction(function(): Void {
         finished(result)
      });
   }

   public function start() {
      new Thread(this).start();
   }

}
