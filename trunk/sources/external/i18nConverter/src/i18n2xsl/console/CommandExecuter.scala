package i18n2xsl.console

trait CommandExecuter {
	def registerCommandHandler(commandHandler: CommandHandler): Unit
	def registeredCommandHandlers: Seq[CommandHandler]
	def executeCommand(commnadLine: String): Boolean
}