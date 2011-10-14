/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop;

import java.awt.EventQueue;

/**
 *
 * @author SikkenJ
 */
public class EdtChecker
{

   public static void checkIfRunningOnEdt()
   {
      if (!EventQueue.isDispatchThread())
      {
         try
         {
            throw new RuntimeException("not running on edt");
         }
         catch (RuntimeException e)
         {
            System.err.println("Not running on the event dispatch thread, but on: " + Thread.currentThread().getName());
            e.printStackTrace();
         }
      }
   }
}
