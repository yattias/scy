package eu.scy.client.desktop.localtoolbroker;

import java.io.File;

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

   public static void deleteDirectory(File dir, boolean onlyContent)
   {
      for (File file : dir.listFiles())
      {
         if (file.isDirectory())
         {
            deleteDirectory(file, false);
         }
         else
            file.delete();
      }
      if (!onlyContent)
      {
         dir.delete();
      }
   }

}
