package i18n2xsl.console

case class FoundCommandHandler(commandHandler: CommandHandler, params: Seq[String])

trait CommandHandlerFinder {
  
  def findCommandHandler(registeredCommandHandlers: Seq[CommandHandler], commandLine: String): Option[FoundCommandHandler]
}