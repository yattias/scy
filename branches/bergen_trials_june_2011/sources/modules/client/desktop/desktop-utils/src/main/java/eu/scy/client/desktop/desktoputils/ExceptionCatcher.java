/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.desktoputils;

import eu.scy.client.common.scyi18n.ResourceBundleWrapper;
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
      final ResourceBundleWrapper resourceBundleWrapper = new ResourceBundleWrapper(this);
      if (resourceBundleWrapper==null){
         logger.error("can't report uncatched exception, because I can't find ResourceBundleWrapper");
         return;
      }
      final String npeMessage = resourceBundleWrapper.getString("ExceptionCatcher.nullPointerException");
      String errorMessage = getThrowableMessage(e,npeMessage);
      StringBuilder exceptionMessage = new StringBuilder(errorMessage);
      Throwable cause = e.getCause();
      while (cause!=null){
         exceptionMessage.append("\n");
         exceptionMessage.append(resourceBundleWrapper.getString("ExceptionCatcher.causedBy"));
         exceptionMessage.append(" ");
         exceptionMessage.append(getThrowableMessage(cause,npeMessage));
         cause = e.getCause();
      }
      JOptionPane.showMessageDialog(null,resourceBundleWrapper.getString("ExceptionCatcher.message")+ "\n" +
         resourceBundleWrapper.getString("ExceptionCatcher.application") + " " + appName + "\n" +
         resourceBundleWrapper.getString("ExceptionCatcher.thread") + " " + t.getName() + "\n" +
         resourceBundleWrapper.getString("ExceptionCatcher.exceptionMessage") + " " + exceptionMessage.toString() + "\n" +
         resourceBundleWrapper.getString("ExceptionCatcher.loggedAction"),
         resourceBundleWrapper.getString("ExceptionCatcher.title"),
         JOptionPane.ERROR_MESSAGE);
//      JOptionPane.showMessageDialog(null, "An uncatched exception occurred:\nApplication: " + appName + "\nThread: " + t.getName() + "\nMessage: " + e.getMessage() + "\nThe exception has been logged.", "An uncatched exception occurred",
//         JOptionPane.ERROR_MESSAGE);
      if (quit)
      {
         System.exit(1);
      }
   }

   private String getThrowableMessage(Throwable e, String npeMessage){
      if (e!=null){
         final String message = e.getMessage();
         if (message!=null){
            return message;
         }
         else{
            return npeMessage;
         }
      }
      return "";
   }
}
