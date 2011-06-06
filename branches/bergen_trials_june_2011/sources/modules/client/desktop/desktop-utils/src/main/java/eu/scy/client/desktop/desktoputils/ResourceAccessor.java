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

}
