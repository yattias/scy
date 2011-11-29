package eu.scy.utils.missioncopier.console

class CommandExecutor2(val commandHandlerFinder: CommandHandlerFinder) extends CommandExecutor {

   private var commandHandlers = Seq[CommandHandler]()

   override def registerCommandHandler(commandHandler: CommandHandler): Unit = {
      commandHandlers :+= commandHandler
   }

   override def registeredCommandHandlers: Seq[CommandHandler] = commandHandlers

   def executeCommand(commandLine: String): Boolean = {
      findCommandHandler(commandLine) match {
         case Some(FoundCommandHandler(commandHandler: CommandHandler, params: Seq[String])) =>
            println("Executing: " + commandHandler.commands(0))
            println("With: " + params.mkString(" "))
            try {
               commandHandler.execute(params)
            } catch {
               case e: Exception => e.printStackTrace()
            }
            return true
         case _ =>
            return false
      }
   }

   def findCommandHandler(commandLine: String): Option[FoundCommandHandler] = {
      commandHandlerFinder.findCommandHandler(registeredCommandHandlers, commandLine)
   }

}