package eu.scy.utils.missioncopier

object MissionCopier {
  
  def foo(x : Array[String]) = x.foldLeft("")((a,b) => a + b)
  
  def main(args : Array[String]) {
    InitJavaUtilLogging.initJavaUtilLogging("missionCopier-java-util-logging.properties")
    InitLog4J.initLog4J("missionCopier-log4j.xml")
    val consoleControl = new ConsoleControl
    consoleControl.intialize
    consoleControl.start
  }

}
