package eu.scy.utils.missioncopier.console

case class FoundCommandHandler(commandHandler: CommandHandler, params: Seq[String])

trait CommandHandlerFinder {
  
  def findCommandHandler(registeredCommandHandlers: Seq[CommandHandler], commandLine: String): Option[FoundCommandHandler]
}