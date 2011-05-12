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
 * @author sikkenj
 */
public class RedirectSystemStreams
{

   private static final String dateFormat = "yyMMdd";
   private static final String outFileName = "out";
   private static final String errFileName = "err";
   private static final String fileExtension = ".txt";

   public static void redirect(File directory)
   {
      if (!directory.exists())
      {
         System.err.println("The logging directory (" + directory.getAbsolutePath() + ") does not exists.\nSystem stream will not be redirected!");
         return;
      }
      if (!directory.isDirectory())
      {
         System.err.println("The logging directory (" + directory.getAbsolutePath() + ") is not a directory.\nSystem stream will not be redirected!");
      }
      try
      {
         PrintStream printStream = createPrintStream(directory, outFileName);
         System.setOut(printStream);
         // System.out.println("\nRedirected the System.out to this file, on " + new Date().toString() + "\n");
      }
      catch (IOException e)
      {
         // System.out.println("Failed to direct System.out, " + e.getMessage());
      }
      try
      {
         PrintStream printStream = createPrintStream(directory, errFileName);
         System.setErr(printStream);
         System.err.println("\nRedirected the System.err to this file, on " + new Date().toString() + "\n");
      }
      catch (IOException e)
      {
         // System.out.println("Failed to direct System.err, " + e.getMessage());
      }
   }

   private static PrintStream createPrintStream(File directory, String fileName) throws IOException
   {
//      int fileCount = 0;
//      File streamFile = getFile(directory, fileName, fileCount);
//      while (streamFile.exists())
//      {
//         ++fileCount;
//         streamFile = getFile(directory, fileName, fileCount);
//      }
      FileOutputStream outputStream = new FileOutputStream(getLogFile(directory, fileName, fileExtension), false);
      return new PrintStream(outputStream, true);
   }

   private static File getFile(File directory, String fileName, int count)
   {
      return new File(directory, fileName + "_" + count + fileExtension);
   }

   public static File getLogFile(String logDirectory, String fileName, String extension)
   {
      return getLogFile(new File(logDirectory), fileName, extension);
   }

   public static File getLogFile(File logDirectory, String fileName, String extension)
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
