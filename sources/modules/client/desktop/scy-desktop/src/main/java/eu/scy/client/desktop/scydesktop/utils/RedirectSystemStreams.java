/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Date;

/**
 *
 * @author sikkenj
 */
public class RedirectSystemStreams
{

   private static final String outFileName = "out";
   private static final String errFileName = "err";
   private static final String fileExtension = ".txt";

   public static void redirect(File directory)
   {
      if (!directory.exists()){
         System.err.println("The logging directory (" + directory.getAbsolutePath() +") does not exists.\nSystem stream will not be redirected!");
         return;
      }
      if (!directory.isDirectory()){
         System.err.println("The logging directory (" + directory.getAbsolutePath() +") is not a directory.\nSystem stream will not be redirected!");
      }
      try
      {
         PrintStream printStream = createPrintStream(directory, outFileName);
         System.setOut(printStream);
         System.out.println("\nRedirected the System.out to this file, on " + new Date().toString() + "\n");
      }
      catch (IOException e)
      {
         System.out.println("Failed to direct System.out, " + e.getMessage());
      }
      try
      {
         PrintStream printStream = createPrintStream(directory, errFileName);
         System.setErr(printStream);
         System.err.println("\nRedirected the System.err to this file, on " + new Date().toString() + "\n");
      }
      catch (IOException e)
      {
         System.out.println("Failed to direct System.err, " + e.getMessage());
      }
   }

   private static PrintStream createPrintStream(File directory, String fileName) throws IOException
   {
      int fileCount = 0;
      File streamFile = getFile(directory, fileName, fileCount);
      while (streamFile.exists())
      {
         ++fileCount;
         streamFile = getFile(directory, fileName, fileCount);
      }
      FileOutputStream outputStream = new FileOutputStream(streamFile, false);
      return new PrintStream(outputStream, true);
   }

   private static File getFile(File directory, String fileName, int count)
   {
      return new File(directory, fileName + "_" + count + fileExtension);
   }
}
