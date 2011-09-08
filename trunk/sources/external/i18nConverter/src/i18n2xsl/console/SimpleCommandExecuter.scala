package i18n2xsl.console

case class HandlerOption(val commands: Seq[String], val commandHandler: CommandHandler)

class SimpleCommandExecuter extends CommandExecuter {
  private var commandHandlers = Seq[CommandHandler]()

  override def registerCommandHandler(commandHandler: CommandHandler): Unit = {
    commandHandlers :+= commandHandler
  }

  override def registeredCommandHandlers: Seq[CommandHandler] = commandHandlers

  override def executeCommand(commandLine: String): Boolean = {
    val initialHandlerOptions =
      for { commandHandler <- commandHandlers } yield for { commands <- commandHandler.commands } yield new HandlerOption(commands.split(" "), commandHandler)
    executeCommand(commandLine, commandLine.split(' '), initialHandlerOptions.flatten)
  }

  private def executeCommand(commandLine: String, args: Seq[String], handlerOptions: Seq[HandlerOption]): Boolean = {
    val newHandlerOptions = findHandlerOptions(args.head.toLowerCase(), handlerOptions)
    if (newHandlerOptions.size > 1) {
      return executeCommand2(commandLine, args, newHandlerOptions)
    } else if (newHandlerOptions.size == 1) {
      newHandlerOptions(0).commands match {
        case Nil =>
          println("Executing: " + commandLine)
          newHandlerOptions.head.commandHandler.execute(args.tail)
        case _ => return executeCommand2(commandLine, args, newHandlerOptions)
      }
    } else {
      println("Can't understand: " + commandLine)
      return false
    }
    true
  }
  
  private def executeCommand2(commandLine: String, args: Seq[String], handlerOptions: Seq[HandlerOption]): Boolean = {
      args.tail match {
        case Nil =>
          println("Expected more commands: " + commandLine)
          return false
        case _ =>
          executeCommand(commandLine, args.tail, handlerOptions)
      }
  }  

  private def findHandlerOptions(arg: String, allHandlerOptions: Seq[HandlerOption]): Seq[HandlerOption] = {
    val handlerOptions = findExactHandlerOptions(arg, allHandlerOptions)
    if (handlerOptions.size > 0) {
      return handlerOptions
    }
    return findPossibleHandlerOptions(arg, allHandlerOptions)
  }

  private def findExactHandlerOptions(arg: String, handlerOptions: Seq[HandlerOption]): Seq[HandlerOption] = {
    for {
      handlerOption <- handlerOptions
      if arg == handlerOption.commands.head
    } yield new HandlerOption(handlerOption.commands.tail, handlerOption.commandHandler)
  }

  private def findPossibleHandlerOptions(arg: String, handlerOptions: Seq[HandlerOption]): Seq[HandlerOption] = {
    for {
      handlerOption <- handlerOptions
      if handlerOption.commands.head.startsWith(arg)
    } yield new HandlerOption(handlerOption.commands.tail, handlerOption.commandHandler)
  }
}