/*
 * InitLog4J.fx
 *
 * Created on 29-jun-2009, 14:27:02
 */
package eu.scy.client.desktop.desktoputils.log4j;

import org.apache.log4j.xml.DOMConfigurator;
import eu.scy.client.desktop.desktoputils.ResourceAccessor;
import java.net.URL;

/**
 * @author sikkenj
 */
def configFileName = "/config/scy-lab-log4j.xml";

public function initLog4J(): Void {
   initLog4J("", false)
}

public function initLog4J(fileName: String, debugging: Boolean) {
   def fileNameToUse = if (fileName.length() > 0) {
              fileName
           } else {
              configFileName
           }
   try {
      var configUrl = findConfigUrl(fileNameToUse, debugging);
      if (configUrl != null) {
         println("reading log4j config from {configUrl}");
         DOMConfigurator.configure(configUrl);
      } else {
         println("cannot find log4j configuration file: {configUrl}");
      }
   } catch (e) {
      println("Problems with loading log4j config, from {fileNameToUse}");
      e.printStackTrace();
   }

}

function findConfigUrl(fileName: String, debugging: Boolean): URL {
   var configUrl: URL;
   if (debugging) {
      configUrl = ResourceAccessor.getResourceUrl(ResourceAccessor.addDebugToFileName(fileName));
   }
   if (configUrl == null) {
      configUrl = ResourceAccessor.getResourceUrl(fileName);
   }
   configUrl
}



