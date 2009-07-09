/*
 * InitLog4J.fx
 *
 * Created on 29-jun-2009, 14:27:02
 */

package eu.scy.client.desktop.scydesktop.utils.log4j;

import org.apache.log4j.xml.DOMConfigurator;

/**
 * @author sikkenj
 */

def configFileName = "/config/scy-desktop-log4j.xml";

public function initLog4J() {
   initLog4J(configFileName);
}

public function initLog4J(fileName) {
      var object = new Object();
		try {
         var configUrl = object.getClass().getResource(configFileName);
         println("reading log4j config from {configUrl}");
         DOMConfigurator.configure (configUrl);
		} catch (e) {
         println("Problems with loading log4j config, from {configFileName}");
			e.printStackTrace();
		}

}


