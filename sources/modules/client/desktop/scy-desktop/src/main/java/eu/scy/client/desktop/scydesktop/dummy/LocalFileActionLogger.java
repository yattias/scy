/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.dummy;

import eu.scy.actionlogging.FileLogger;
import eu.scy.actionlogging.api.IAction;
import eu.scy.client.desktop.scydesktop.utils.RedirectSystemStreams;

/**
 *
 * @author sikken
 */
public class LocalFileActionLogger extends FileLogger
{
   private static final String fileName = "actions";

   private boolean enableLogging = true;

   public LocalFileActionLogger(String logDirectory)
   {
      super(RedirectSystemStreams.getLogFile(logDirectory,fileName,".txt").getAbsolutePath());
   }

   public void setEnableLogging(boolean enableLogging)
   {
      this.enableLogging = enableLogging;
   }

   @Override
   public void log(IAction action)
   {
      if (enableLogging)
      {
         super.log(action);
      }
   }
}
