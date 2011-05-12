/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.desktoputils;

import java.net.URI;

/**
 *
 * @author sikken
 */
public class UriUtils {

   public static String getName(URI uri){
      String path = uri.getPath();
      int lastSlashPos = path.lastIndexOf('/');
      if (lastSlashPos>=0){
         return path.substring(lastSlashPos+1);
      }
      return path;
   }

   public static String getExtension(URI uri){
      String name = getName(uri);
     int lastPointPos = name.lastIndexOf('.');
      if (lastPointPos>=0){
         return name.substring(lastPointPos+1);
      }
      return "";
   }

   public static String getTitle(URI uri){
      String name = getName(uri);
     int lastPointPos = name.lastIndexOf('.');
      if (lastPointPos>=0){
         return name.substring(0,lastPointPos);
      }
      return name;
   }

}
