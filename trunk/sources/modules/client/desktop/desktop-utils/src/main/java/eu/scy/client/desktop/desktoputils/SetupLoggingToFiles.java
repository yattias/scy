/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.desktoputils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author sikken
 */
public class SetupLoggingToFiles
{

   private static final String dateFormat = "yyMMdd";
   private static final String fileExtension = ".txt";
   private static final String outFileName = "out";
   private static final String errFileName = "err";
   private static final String javaUtilLogFileName = "java";
   private static final String log4JLogFileName = "log4j";
   private File directory;
   private boolean directoryOk = false;

   public SetupLoggingToFiles(File directory)
   {
      this.directory = directory;
      checkDirectory();
   }

   private void checkDirectory()
   {
      if (!directory.exists())
      {
         System.err.println("The logging directory (" + directory.getAbsolutePath() + ") does not exists.\nNo log files will be created!");
         return;
      }
      if (!directory.isDirectory())
      {
         System.err.println("The logging directory (" + directory.getAbsolutePath() + ") is not a directory.\nNo log files will be created!");
      }
      directoryOk = true;
   }

   @Override
   public String toString()
   {
      return "{directory:" + directory + ", directoryOk:" + directoryOk + "}";
   }

   public void redirectSystemStreams()
   {
      if (!directoryOk){
         System.err.println("Cannot redirect system streams because of invalid log directory");
         return;
      }
      try
      {
         PrintStream printStream = createPrintStream(directory, outFileName);
         System.setOut(printStream);
         // System.out.println("\nRedirected the System.out to this file, on " + new Date().toString() + "\n");
      }
      catch (IOException e)
      {
         System.err.println("Failed to direct System.out, " + e.getMessage());
      }
      try
      {
         PrintStream printStream = createPrintStream(directory, errFileName);
         System.setErr(printStream);
         System.err.println("\nRedirected the System.err to this file, on " + new Date().toString() + "\n");
      }
      catch (IOException e)
      {
         System.err.println("Failed to direct System.err, " + e.getMessage());
      }
   }

   private PrintStream createPrintStream(File directory, String fileName) throws IOException
   {
      FileOutputStream outputStream = new FileOutputStream(getLogFile(directory, fileName, fileExtension), false);
      return new PrintStream(outputStream, true);
   }

   public void setupJavaUtilLogFile()
   {
      if (!directoryOk){
         System.err.println("Cannot log java util logging to file because of invalid log directory");
         return;
      }
      File logFile = getLogFile(javaUtilLogFileName);
      try
      {
         java.util.logging.Logger setupLogger = java.util.logging.Logger.getLogger("");
         java.util.logging.Handler fileHandler = new java.util.logging.FileHandler(logFile.getAbsolutePath());
         fileHandler.setFormatter(new java.util.logging.SimpleFormatter());
         fileHandler.setLevel(java.util.logging.Level.ALL);
         setupLogger.addHandler(fileHandler);
         // System.out.println("Java utillLogging setup to log to " + logFile.getAbsolutePath());
      }
      catch (Exception ex)
      {
         System.err.println("Failed to write java util logging to file (" + logFile.getAbsolutePath() + "), " + ex.getMessage());
      }
   }

   public void setuplog4JLogFile()
   {
      if (!directoryOk){
         System.err.println("Cannot log log4j to file because of invalid log directory");
         return;
      }
      File logFile = getLogFile(log4JLogFileName);
      try
      {
        org.apache.log4j.Logger logger = org.apache.log4j.Logger.getRootLogger();
        FileOutputStream filename= new FileOutputStream(logFile);
        org.apache.log4j.WriterAppender writeappender = new org.apache.log4j.WriterAppender(new org.apache.log4j.PatternLayout("%d [%t] %c.%M:%L%n %-5p: %m%n"),filename);
        logger.addAppender(writeappender);
         // System.out.println("Log4J setup to log to " + logFile.getAbsolutePath());
      }
      catch (Exception ex)
      {
         System.err.println("Failed to write log4J to file (" + logFile.getAbsolutePath() + "), " + ex.getMessage());
      }
   }

   private File getLogFile(String fileName)
   {
      return getLogFile(directory, fileName, fileExtension);
   }

   private File getLogFile(File logDirectory, String fileName, String extension)
   {
      if (!logDirectory.exists())
      {
         throw new IllegalArgumentException("log directory does not exists: " + logDirectory);
      }
      if (!logDirectory.isDirectory())
      {
         throw new IllegalArgumentException("log directory is not a directory: " + logDirectory);
      }
      String dateString = new SimpleDateFormat(dateFormat).format(new Date());
      int fileCount = 0;
      File logFile = new File(logDirectory, getFileName(dateString, fileName, fileCount, extension));
      while (logFile.exists())
      {
         ++fileCount;
         logFile = new File(logDirectory, getFileName(dateString, fileName, fileCount, extension));
      }
      return logFile;
   }

   private static String getFileName(String dateString, String fileName, int count, String extension)
   {
      return String.format("%1$s_%2$s_%3$03d%4$s", dateString, fileName, count, extension);
   }
}
