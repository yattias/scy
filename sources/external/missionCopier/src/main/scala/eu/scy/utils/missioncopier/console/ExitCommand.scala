package eu.scy.utils.missioncopier.console


class ExitCommand(val consoleModel: ConsoleModel) extends CommandHandler {
   val commands = Seq("exit", "quit")
   val description = "exits application"

   def execute(params: Seq[String]): Unit = {
      consoleModel.exit = true
   }
}