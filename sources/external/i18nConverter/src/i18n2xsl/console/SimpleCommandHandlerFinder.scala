package i18n2xsl.console
import scala.collection.mutable.ArrayBuffer

class SimpleCommandHandlerFinder extends CommandHandlerFinder {

  override def findCommandHandler(commandHandlers: Seq[CommandHandler], commandLine: String): Option[FoundCommandHandler] = {
    val found = ArrayBuffer[FoundCommandHandler]()
    for (commandHandler <- commandHandlers) {
      for (command <- commandHandler.commands) {
        checkCommandHandler(command, commandLine) match {
          case Some(params: Seq[String]) => found += FoundCommandHandler(commandHandler, params)
          case _ =>
        }
      }
    }
    if (found.size == 1) {
      return Some(found(0))
    } else {
      return None
    }
  }

  def checkCommandHandler(command: String, commandLine: String): Option[(Seq[String])] = {
    val commandWords = command.split(" ").filterNot(_.isEmpty())
    val enteredWords = commandLine.split(" ").filterNot(_.isEmpty())
    if (enteredWords.length<commandWords.length){
      return None
    }
    for (i <- 0 until commandWords.length){
      if (enteredWords(i)!=commandWords(i)){
        return None
      }
    }
    Some(enteredWords.slice(commandWords.length,enteredWords.length))
  }

}