package eu.scy.utils.missioncopier.console

trait CommandExecutor {
	def registerCommandHandler(commandHandler: CommandHandler): Unit
	def registeredCommandHandlers: Seq[CommandHandler]
	def executeCommand(commandLine: String): Boolean
}