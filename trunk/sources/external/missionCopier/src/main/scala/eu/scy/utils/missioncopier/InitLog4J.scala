package eu.scy.utils.missioncopier

import org.apache.log4j.xml.DOMConfigurator

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 29-11-11
 * Time: 16:30
 * To change this template use File | Settings | File Templates.
 */

object InitLog4J {
  def initLog4J(fileName: String) {
    try {
      val configUrl = this.getClass().getResource(fileName)
      if (configUrl != null) {
        println("reading log4j config from " + configUrl);
        DOMConfigurator.configure(configUrl);
      } else {
        println("cannot find log4j configuration file: " + fileName);
      }

    } catch {
      case e: Exception => println("Problems with loading log4j config, from " + fileName)
      e.printStackTrace()
    }
  }

}