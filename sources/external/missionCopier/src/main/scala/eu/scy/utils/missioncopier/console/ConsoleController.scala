package eu.scy.utils.missioncopier.console


/**
 * Created by IntelliJ IDEA.
 * User: sikken
 * Date: 27-11-11
 * Time: 15:11
 */

class ConsoleController(val name: String, val prompt: String, val consoleModel: ConsoleModel) {

  var showState: () => Unit = null

  private val commandExecutor = new CommandExecutor2(new AdvancedCommandHandlerFinder())
  private var forLastLine = "";
  private var lastLine = "";
  private var originalForLastLine = ""
  private var originalLastLine = ""
  private var initialArguments = ""

  class RepeatCommand extends CommandHandler {
    val commands = Seq("repeat", "r")
    val description = "repeats last line"

    def execute(params: Seq[String]): Unit = {
      commandExecutor.executeCommand(forLastLine)
      forLastLine = originalForLastLine
      lastLine = originalLastLine
    }
  }

  def createStandardCommandHandlers() = {
    addCommandHandler(new RepeatCommand())
    addCommandHandler(new ExitCommand(consoleModel))
    addCommandHandler(new HelpCommand(commandExecutor))
    addCommandHandler(new FileInputCommand(consoleModel))
  }

  def addCommandHandler(commandHandler: CommandHandler) = commandExecutor.registerCommandHandler(commandHandler)

  def start(args : Array[String]) = {
    println("Welcome to " + name)
    handleArguments(args)
    do {
      if (showState != null) {
        showState()
      }
      print(prompt)
      val line = consoleModel.readLine().trim
      if (line.length() > 0) {
        originalForLastLine = forLastLine
        originalLastLine = lastLine
        forLastLine = lastLine
        lastLine = line
        if (!commandExecutor.executeCommand(line)) {
          forLastLine = originalForLastLine
          lastLine = originalLastLine
          println("Did not understand: " + line)
        }
      }
    } while (!consoleModel.exit)
    println("Thank you for using " + name)
  }

  private def handleArguments(args : Array[String]) = {
    if (args!=null && !args.isEmpty){
      initialArguments = args.foldLeft("")((a,b) => a + " " + b)
      consoleModel.readLine = readLineFromArguments
    }
  }

  private def readLineFromArguments():String = {
    if (initialArguments.isEmpty){
      consoleModel.readLine = Predef.readLine
      consoleModel.readLine()
    } else {
      val line = initialArguments
      initialArguments = ""
      println(line)
      line
    }
  }


}