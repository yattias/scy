package eu.scy.utils.missioncopier.console

import java.io.File
import io.Source


/**
 * Created by IntelliJ IDEA.
 * User: sikken
 * Date: 28-11-11
 * Time: 20:12
 * To change this template use File | Settings | File Templates.
 */

class FileInputCommand(val consoleModel: ConsoleModel) extends CommandHandler {
   val commands = Seq("run [commands] [from] file")
   val description = "run commands from file"
   override val paramDescription = "input file"
   private var inputLines: Iterator[String] = null;

   def execute(params: Seq[String]): Unit = {
      if (params.isEmpty) {
         println("no input file specified")
         return
      }
      val inputFile = new File(params.head)
      if (!inputFile.exists()) {
         println("cannot find input file: " + inputFile.getAbsolutePath)
         return
      }
      if (!inputFile.isFile()) {
         println("input file is not a file: " + inputFile.getAbsolutePath)
         return
      }
      redirectInput(inputFile)
   }

   private def redirectInput(inputFile: File): Unit = {
      inputLines = Source.fromFile(inputFile).getLines()
      consoleModel.readLine = readLineFromInputFile
   }

   private def readLineFromInputFile(): String = {
      if (inputLines.hasNext) {
         val line = inputLines.next()
         println(line)
         line
      } else {
         consoleModel.readLine = Predef.readLine
         consoleModel.readLine()
      }
   }

}