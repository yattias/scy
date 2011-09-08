package i18n2xsl.console

object MainConsole {
  def main(args: Array[String]): Unit = {
    val consoleControl = new ConsoleControl2
    consoleControl.createCommandHandlers()
    consoleControl.start

  }
}
