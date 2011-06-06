package eu.scy.client.desktop.localtoolbroker;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DirectoryUtils
{
   private static final String dateFormat = "yyMMdd";

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

   public static void copyDirectory(File source, File destination) throws IOException
   {
      if (!source.isDirectory())
      {
         throw new IOException("source is not an existing directory: " + source.getAbsolutePath());
      }
      copyFiles(source.listFiles(), destination);
   }

   public static void copyFiles(File[] files, File destination) throws IOException
   {
      if (!destination.isDirectory())
      {
         throw new IOException("destination is not an existing directory: "
                  + destination.getAbsolutePath());
      }
      for (int i = 0; i < files.length; i++)
      {
         if (files[i] != null && !files[i].isHidden())
         {
            File destinationFile = new File(destination, files[i].getName());
            if (files[i].isFile())
            {
               copyFile(files[i], destinationFile);
            }
            else if (files[i].isDirectory())
            {
               // don't copy a cvs directory
               if (!files[i].getName().equalsIgnoreCase("cvs"))
               {
                  if (!destinationFile.mkdir())
                  {
                     throw new IOException("Failed to create directory "
                              + destinationFile.getAbsolutePath());
                  }
                  copyDirectory(files[i], destinationFile);
               }
            }
         }
      }
   }

   public static void copyFile(File source, File destination) throws IOException
   {
      if (destination.exists())
      {
         throw new IOException("Duplicate file " + destination.getAbsolutePath());
      }
      FileInputStream fileReader = null;
      FileOutputStream fileWriter = null;
      try
      {
         fileReader = new FileInputStream(source);
         fileWriter = new FileOutputStream(destination);
         byte[] buffer = new byte[64 * 1024];
         int bytesRead = 0;
         while ((bytesRead = fileReader.read(buffer)) >= 0)
         {
            fileWriter.write(buffer, 0, bytesRead);
         }
         fileWriter.close();
         fileWriter = null;
         destination.setLastModified(source.lastModified());
      }
      catch (IOException e)
      {
         if (fileWriter != null)
         {
            try
            {
               fileWriter.close();
               fileWriter = null;
            }
            catch (IOException eClose)
            {
            }
         }
         destination.delete();
         throw e;
      }
      finally
      {
         if (fileReader != null)
         {
            fileReader.close();
         }
      }
   }

}
