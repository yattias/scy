package i18n2xsl.console

import scala.collection.Seq
import scala.collection.mutable.ArrayBuffer

case class CheckResult(val noOfExtends: Int, params: Seq[String])
class AdvancedCommandHandlerFinder extends CommandHandlerFinder {

  case class FoundResult(val nrOfExtends: Int, found: FoundCommandHandler)

  override def findCommandHandler(commandHandlers: Seq[CommandHandler], commandLine: String): Option[FoundCommandHandler] = {
    val found = ArrayBuffer[FoundResult]()
    for (commandHandler <- commandHandlers) {
      for (command <- commandHandler.commands) {
        checkCommandHandler(command, commandLine) match {
          case Some(CheckResult(nrOfExtends, params)) => found += FoundResult(nrOfExtends, FoundCommandHandler(commandHandler, params))
          case _ =>
        }
      }
    }
    found.size match {
      case 0 => None
      case 1 => Some(found(0).found)
      case _ =>
//        val sortedFound = found.sortBy(f => f.nrOfExtends)
        val sortedFound = found.sortWith(commandHandlerComparator)
        Some(sortedFound(0).found)
    }
  }

  def checkCommandHandler(command: String, commandLine: String): Option[CheckResult] = {
    val commandWords = command.split(" ").filterNot(_.isEmpty())
    val enteredWords = commandLine.split(" ").filterNot(_.isEmpty())
    //    if (enteredWords.length < commandWords.length) {
    //      return None
    //    }
    compareSequences(commandWords, enteredWords)
  }

  def compareSequences(commandWords: Seq[String], enteredWords: Seq[String]): Option[CheckResult] = {
    if (commandWords == Nil) {
      return Some(new CheckResult(0, enteredWords))
    }
    val analysedCommandWord = analyseCommand(commandWords.head)
    val commandWord = analysedCommandWord._1
    val optionalCommand = analysedCommandWord._2
    if (enteredWords == Nil) {
      if (optionalCommand) {
        return Some(new CheckResult(0, Nil))
      }
      return None
    }
    val enteredWord = enteredWords.head.toLowerCase()
    if (commandWord == enteredWord) {
      nextCompareSequences(commandWords, enteredWords, 0)
    } else if (commandWord.startsWith(enteredWord)) {
      nextCompareSequences(commandWords, enteredWords, 1)
    } else if (optionalCommand) {
      compareSequences(commandWords.tail, enteredWords)
    } else {
      None
    }
  }

  def analyseCommand(commandWord: String): (String, Boolean) = {
    if (commandWord.startsWith("[") && commandWord.endsWith("]")) {
      (commandWord.substring(1, commandWord.length() - 1), true)
    } else {
      (commandWord, false)
    }
  }

  def nextCompareSequences(commandWords: Seq[String], enteredWords: Seq[String], addNrOfExtends: Int): Option[CheckResult] = {
    val nextCompareResult = compareSequences(commandWords.tail, enteredWords.tail)
    nextCompareResult match {
      case Some(CheckResult(nrOfExtends, params)) => return Some(new CheckResult(nrOfExtends + addNrOfExtends, params))
      case _ => return None
    }
  }
  
  def commandHandlerComparator(foundResult1: FoundResult, foundResult2: FoundResult): Boolean = {
    if (foundResult1.nrOfExtends>foundResult2.nrOfExtends){
      return true
    } 
    if (foundResult1.nrOfExtends<foundResult2.nrOfExtends) {
      return false
    }
    if (foundResult1.found.commandHandler.commands(0).size>foundResult2.found.commandHandler.commands(0).size){
      return true
    }
    return false
  }

}