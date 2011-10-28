package eu.scy.client.desktop.desktoputils;

import java.io.InputStream;
import java.net.URL;

public class ResourceAccessor
{

   private ResourceAccessor()
   {
   }

   public static URL getResourceUrl(String fileName)
   {
      return ResourceAccessor.class.getResource(fileName);
   }

   public static InputStream getResourceStream(String fileName)
   {
      return ResourceAccessor.class.getResourceAsStream(fileName);
   }

   public static String addDebugToFileName(String fileName)
   {
      final String debugName = "_debug";
      final int lastPointPos = fileName.lastIndexOf('.');
      if (lastPointPos >= 0)
      {
         return fileName.substring(0, lastPointPos) + debugName + fileName.substring(lastPointPos, fileName.length());
      }
      else
      {
         return fileName + debugName;
      }
   }
}
