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

def configFileName = "/config/scy-lab-log4j.xml";

public function initLog4J() {
   initLog4J(configFileName);
}

public function initLog4J(fileName:String) {
      var object = new Object();
		try {
         var configUrl = object.getClass().getResource(fileName);
         if (configUrl!=null){
            println("reading log4j config from {configUrl}");
            DOMConfigurator.configure (configUrl);
         }
         else{
            println("cannot find log4j configuration file: {fileName}");
         }

		} catch (e) {
         println("Problems with loading log4j config, from {fileName}");
			e.printStackTrace();
		}

}


