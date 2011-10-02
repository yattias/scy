/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tooltips.impl;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author SikkenJ
 */
public class BubbleManagerTimer implements Runnable
{

   private final long bubbleWaitMillis = 10000;
   private long nextBubbleMillis = Long.MAX_VALUE;
   private boolean abort = true;
   private final ShowNextBubble showNextBubble;

   public BubbleManagerTimer(ShowNextBubble showNextBubble)
   {
      this.showNextBubble = showNextBubble;
   }

   public void start(){
      abort = false;
      new Thread(this).start();
   }

   public void abort()
   {
      abort = true;
   }

   @Override
   public void run()
   {
      resetNextBubbleMillis();
      while (!abort)
      {
         if (System.currentTimeMillis() >= nextBubbleMillis)
         {
            showNextBubble.showNextBubble();
            resetNextBubbleMillis();
         }
         try
         {
            Thread.sleep(nextBubbleMillis - System.currentTimeMillis());
         }
         catch (InterruptedException ex)
         {
            Logger.getLogger(BubbleManagerTimer.class.getName()).log(Level.SEVERE, null, ex);
         }
      }
   }

   public void userDidSomething()
   {
      resetNextBubbleMillis();
   }

   private void resetNextBubbleMillis()
   {
      nextBubbleMillis = System.currentTimeMillis() + bubbleWaitMillis;
   }
}
