package i18n2xsl.console.commands
import i18n2xsl.console.PropertiesModelCommandHandler
import i18n2xsl.console.PropertiesModel
import i18n2xsl.console.CommandExecuter

class HelpCommand(override val propertiesModel: PropertiesModel, val commandExecuter: CommandExecuter) extends PropertiesModelCommandHandler(propertiesModel) {
  val commands = Seq("help","?")
  val description = "shows help"
  def execute(params: Seq[String]): Unit = {
    println("Available commands:")
    for (commandHandler <- commandExecuter.registeredCommandHandlers) {
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