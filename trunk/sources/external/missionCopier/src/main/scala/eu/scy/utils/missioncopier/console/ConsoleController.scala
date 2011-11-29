package eu.scy.utils.missioncopier.console


/**
 * Created by IntelliJ IDEA.
 * User: sikken
 * Date: 27-11-11
 * Time: 15:11
 * To change this template use File | Settings | File Templates.
 */

class ConsoleController(val name: String, val prompt: String, val consoleModel: ConsoleModel) {

  var showState: () => Unit = null

  private val commandExecutor = new CommandExecutor2(new AdvancedCommandHandlerFinder())
  private var forLastLine = "";
  private var lastLine = "";
  private var originalForLastLine = ""
  private var originalLastLine = ""

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

  def start() = {
    println("Welcome to " + name)
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


}