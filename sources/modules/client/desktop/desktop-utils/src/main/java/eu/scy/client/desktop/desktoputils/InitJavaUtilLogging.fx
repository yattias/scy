/*
 * InitJavaUtilLogging.fx
 *
 * Created on 4-jan-2010, 11:05:28
 */
package eu.scy.client.desktop.desktoputils;

import java.util.logging.LogManager;

/**
 * @author sikken
 */
def configFileName = "/config/scy-lab-java-util-logging.properties";

public function initJavaUtilLogging() {
   initJavaUtilLogging(configFileName);
}

public function initJavaUtilLogging(fileName: String) {
   try {
      var configUrl = ResourceAccessor.getResourceUrl(fileName);
      if (configUrl != null) {
         println("reading java util logging config from {configUrl}");
         var logManager = LogManager.getLogManager();
         var configStream = configUrl.openStream();
         logManager.readConfiguration(configStream);
      } else {
      println("cannot find java util logging configuration file: {fileName}");
      }

   } catch (e) {
      println("Problems with loading java util logging config, from {fileName}");
      e.printStackTrace();
   }

}


