/*
 * InitJavaUtilLogging.fx
 *
 * Created on 4-jan-2010, 11:05:28
 */
package eu.scy.client.desktop.desktoputils;

import java.util.logging.LogManager;
import java.net.URL;

/**
 * @author sikken
 */
def configFileName = "/config/scy-lab-java-util-logging.properties";

public function initJavaUtilLogging():Void{
   initJavaUtilLogging("",false)
}

public function initJavaUtilLogging(fileName: String, debugging: Boolean) {
   def fileNameToUse = if (fileName.length() > 0) {
              fileName
           } else {
              configFileName
           }
   try {
      var configUrl = findConfigUrl(fileNameToUse, debugging);
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
