package eu.scy.client.desktop.localtoolbroker;

import java.io.File;
import java.io.IOException;

import junit.framework.Assert;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

public class TestUtils
{
   public static void testToolBrokerAPI(ToolBrokerAPI tbi)
   {
      Assert.assertNotNull(tbi);
      Assert.assertNotNull(tbi.getRepository());
      Assert.assertNotNull(tbi.getExtensionManager());
      Assert.assertNotNull(tbi.getMetaDataTypeManager());
      Assert.assertNotNull(tbi.getELOFactory());
      Assert.assertNotNull(tbi.getActionLogger());
      Assert.assertNotNull(tbi.getAwarenessService());
      Assert.assertNotNull(tbi.getDataSyncService());
   }

   public static void deleteDirectory(File dir, boolean onlyContent) throws IOException
   {
      for (File file : dir.listFiles())
      {
         if (file.isDirectory())
         {
            deleteDirectory(file, false);
         }
         else
         {
            if (!file.delete())
            {
               throw new IOException("failed to delete file: " + file.getAbsolutePath());
            }
         }
      }
      if (!onlyContent)
      {
         if (!dir.delete())
         {
            throw new IOException("failed to delete directory: " + dir.getAbsolutePath());
         }
      }
   }

   public static long getDirectoryBytes(File dir)
   {
      long bytes = 0;
      for (File file : dir.listFiles())
      {
         if (file.isHidden())
         {
            continue;
         }
         if (file.isDirectory())
         {
            bytes += getDirectoryBytes(file);
         }
         else
            bytes += file.length();
      }
      return bytes;
   }

   public static int getDirectoryFileCount(File dir)
   {
      int files = 0;
      for (File file : dir.listFiles())
      {
         if (file.isHidden())
         {
            continue;
         }
         if (file.isDirectory())
         {
            files += getDirectoryFileCount(file);
         }
         ++files;
      }
      return files;
   }

}
