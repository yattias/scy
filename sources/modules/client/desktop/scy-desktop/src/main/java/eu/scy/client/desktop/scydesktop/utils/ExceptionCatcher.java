/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.utils;

import java.util.Date;

import javax.swing.JOptionPane;

import org.apache.log4j.Logger;

public class ExceptionCatcher implements Thread.UncaughtExceptionHandler
{

   private final static Logger logger = Logger.getLogger(ExceptionCatcher.class);
   private String appName = "unknown";
   private boolean quit = false;

   public ExceptionCatcher(String appName)
   {
      super();
      this.appName = appName;
   }

   public ExceptionCatcher(String appName, boolean quit)
   {
      super();
      this.appName = appName;
      this.quit = quit;
   }

   public boolean isQuit()
   {
      return quit;
   }

   public void setQuit(boolean quit)
   {
      this.quit = quit;
   }

   @Override
   public void uncaughtException(Thread t, Throwable e)
   {
      logger.error("An uncatched exception occurred in thread " + t.getName(), e);
      System.err.println("An uncatched exception occurred at " + new Date() + " in thread " + t.getName());
      e.printStackTrace(System.err);
      JOptionPane.showMessageDialog(null, "An uncatched exception occurred:\nApplication: " + appName + "\nThread: " + t.getName() + "\nMessage: " + e.getMessage() + "\nThe exception has been logged.", "An uncatched exception occurred",
         JOptionPane.ERROR_MESSAGE);
      if (quit)
      {
         System.exit(1);
      }
   }
}
