/*
 * InitLog4J.fx
 *
 * Created on 29-jun-2009, 14:27:02
 */
package eu.scy.client.desktop.desktoputils.log4j;

import org.apache.log4j.xml.DOMConfigurator;
import eu.scy.client.desktop.desktoputils.ResourceAccessor;

/**
 * @author sikkenj
 */
def configFileName = "/config/scy-lab-log4j.xml";

public function initLog4J() {
   initLog4J(configFileName);
}

public function initLog4J(fileName: String) {
   try {
      var configUrl = ResourceAccessor.getResourceUrl(fileName);
      if (configUrl != null) {
         println("reading log4j config from {configUrl}");
         DOMConfigurator.configure(configUrl);
      } else {
      println("cannot find log4j configuration file: {fileName}");
      }
   } catch (e) {
      println("Problems with loading log4j config, from {fileName}");
      e.printStackTrace();
   }

}


