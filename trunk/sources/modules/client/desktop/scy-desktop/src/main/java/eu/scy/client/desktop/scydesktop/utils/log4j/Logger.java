/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.utils.log4j;

/**
 * Only created because my netbeans 6.8 does not want to recognise the import of org.apache.log4j.Logger.
 *
 * This class is source compatible with the orginal org.apache.log4j.Logger. It only contains the methods that I use from javafx.
 *
 * @author sikken
 */
public class Logger
{

   private org.apache.log4j.Logger logger;

   private Logger(org.apache.log4j.Logger logger)
   {
      this.logger = logger;
   }

   public static Logger getLogger(Class clazz)
   {
      return new Logger(org.apache.log4j.Logger.getLogger(clazz));
   }

   public static Logger getLogger(String name)
   {
      return new Logger(org.apache.log4j.Logger.getLogger(name));
   }

   public void debug(java.lang.Object message)
   {
      logger.debug(message);
   }

   public void debug(java.lang.Object message, java.lang.Throwable t)
   {
      logger.debug(message, t);
   }

   public void info(java.lang.Object message)
   {
      logger.info(message);
   }

   public void info(java.lang.Object message, java.lang.Throwable t)
   {
      logger.info(message, t);
   }

   public void warn(java.lang.Object message)
   {
      logger.warn(message);
   }

   public void warn(java.lang.Object message, java.lang.Throwable t)
   {
      logger.warn(message, t);
   }

   public void error(java.lang.Object message)
   {
      logger.error(message);
   }

   public void error(java.lang.Object message, java.lang.Throwable t)
   {
      logger.error(message, t);
   }
}
