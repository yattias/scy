/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.desktoputils.log4j;

import java.net.URL;
import org.apache.log4j.xml.DOMConfigurator;

/**
 *
 * @author sikkenj
 */
public class InitLog4j
{

   public static void init(URL configUrl)
   {
      try
      {
         // System.out.println("reading log4j config from " + configUrl);
         DOMConfigurator.configure(configUrl);
      }
      catch (Exception e)
      {
         // System.out.println("Problems with loading log4j config, from " + configUrl);
         e.printStackTrace();
      }

   }

   public static void initFromClassPath(String configFileName)
   {
      URL configUrl = new Object().getClass().getResource(configFileName);
      init(configUrl);
   }

   public static void init()
   {
      initFromClassPath("/config/scy-lab-log4j.xml");
   }
}
