package eu.scy.utils.missioncopier

import java.util.logging.LogManager

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 29-11-11
 * Time: 16:22
 * To change this template use File | Settings | File Templates.
 */

object InitJavaUtilLogging {
  def initJavaUtilLogging(fileName: String) {
    try {
      val configUrl = this.getClass().getResource(fileName)
      if (configUrl != null) {
        println("reading java util logging config from " + configUrl);
        val logManager = LogManager.getLogManager();
        val configStream = configUrl.openStream();
        logManager.readConfiguration(configStream);
      } else {
        println("cannot find java util logging configuration file: " + fileName);
      }

    } catch {
      case e: Exception => println("Problems with loading java util logging config, from " + fileName)
      e.printStackTrace()
    }
  }
}

