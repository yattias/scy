/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.dummy;

import eu.scy.actionlogging.FileLogger;
import eu.scy.actionlogging.api.IAction;
import eu.scy.actionlogging.api.IActionLogger;
import eu.scy.client.desktop.scydesktop.utils.RedirectSystemStreams;

/**
 *
 * @author sikken
 */
public class LocalFileActionLogger implements IActionLogger
{
   private static final String fileName = "actions";
   private String logDirectory;
   private FileLogger fileLogger = null;

   private boolean enableLogging = false;

   public LocalFileActionLogger(String logDirectory)
   {
      this.logDirectory = logDirectory;
   }

   public void setEnableLogging(boolean enableLogging)
   {
      this.enableLogging = enableLogging;
      if (enableLogging && fileLogger==null){
         fileLogger = new FileLogger(RedirectSystemStreams.getLogFile(logDirectory,fileName,".txt").getAbsolutePath());
      }
   }

   @Override
   public void log(IAction action)
   {
      if (enableLogging)
      {
         fileLogger.log(action);
         fileLogger.flush();
      }
   }

   @Override
   public void log(String username, String source, IAction action)
   {
      log(action);
   }


}
