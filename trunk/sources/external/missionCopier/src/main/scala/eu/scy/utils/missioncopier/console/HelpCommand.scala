package eu.scy.utils.missioncopier.console


class HelpCommand(val theCommandExecutor: CommandExecutor) extends CommandHandler {
  val commands = Seq("help","?")
  val description = "shows help"
  def execute(params: Seq[String]): Unit = {
    println("Available commands:")
    for (commandHandler <- theCommandExecutor.registeredCommandHandlers) {
      println("- " + commandHandler.description + ":")
      for (command <- commandHandler.commands) {
        print("   " + command)
        if (!commandHandler.paramDescription.isEmpty())
        	print(" <" + commandHandler.paramDescription + ">")
        println()  		
      }
    }
  }
}