package i18n2xsl.console
import i18n2xsl.KeyValueStore
import i18n2xsl.console.commands.ExitCommand
import i18n2xsl.console.commands.HelpCommand
import i18n2xsl.console.commands.LoadFromExcelCommand
import i18n2xsl.console.commands.LoadFromJavaCommand
import i18n2xsl.console.commands.SaveToExcelCommand
import i18n2xsl.console.commands.RemoveStoresCommand
import i18n2xsl.console.commands.KeepStoresCommand
import i18n2xsl.console.commands.CompareWithExcelCommand
import i18n2xsl.console.commands.CompareWithJavaCommand
import i18n2xsl.console.commands.SaveToJavaCommand
import i18n2xsl.console.commands.AddNewFromExcelCommand
import i18n2xsl.console.commands.AddNewFromJavaCommand
import i18n2xsl.console.commands.RemoveSameStoresAsJavaCommand
import i18n2xsl.console.commands.RemoveSameStoresAsExcelCommand
import i18n2xsl.console.commands.ListStores

class ConsoleControl2 {
  //  private val commandExecuter = new SimpleCommandExecuter()
  private val commandExecuter = new CommandExecuter2(new AdvancedCommandHandlerFinder())

  private val name = "java(FX) properties files <-> excel converter"
  private val prompt = ">";
  private var forLastLine = "";
  private var lastLine = "";
  private val propertiesModel = new PropertiesModel()
  private var showShortSummary = true

  class RepeatCommand extends CommandHandler {
    val commands = Seq("repeat", "r")
    val description = "repeats last line"
    def execute(params: Seq[String]): Unit = {
      lastLine = forLastLine
      commandExecuter.executeCommand(lastLine)
    }
  }

  class SetShortSummaryCommand extends CommandHandler {
    val commands = Seq("set [show] short [summary]")
    val description = "set show short summary on/off"
    override val paramDescription = "true|false | on|off"
    def execute(params: Seq[String]): Unit = {
      if (!params.isEmpty) {
        params.head.toLowerCase match {
          case "true" => showShortSummary = true
          case "false" => showShortSummary = false
          case "on" => showShortSummary = true
          case "off" => showShortSummary = false
          case param: String => println("can't understand: " + param)
        }
      }
    }
  }

  def createCommandHandlers() = {
    commandExecuter.registerCommandHandler(new RepeatCommand())
    commandExecuter.registerCommandHandler(new SetShortSummaryCommand())
    commandExecuter.registerCommandHandler(new ExitCommand(propertiesModel))
    commandExecuter.registerCommandHandler(new HelpCommand(propertiesModel, commandExecuter))
    commandExecuter.registerCommandHandler(new LoadFromExcelCommand(propertiesModel))
    commandExecuter.registerCommandHandler(new LoadFromJavaCommand(propertiesModel))
    commandExecuter.registerCommandHandler(new SaveToExcelCommand(propertiesModel))
    commandExecuter.registerCommandHandler(new SaveToJavaCommand(propertiesModel))
    commandExecuter.registerCommandHandler(new RemoveStoresCommand(propertiesModel))
    commandExecuter.registerCommandHandler(new KeepStoresCommand(propertiesModel))
    commandExecuter.registerCommandHandler(new CompareWithExcelCommand(propertiesModel))
    commandExecuter.registerCommandHandler(new CompareWithJavaCommand(propertiesModel))
    commandExecuter.registerCommandHandler(new AddNewFromExcelCommand(propertiesModel))
    commandExecuter.registerCommandHandler(new AddNewFromJavaCommand(propertiesModel))
    commandExecuter.registerCommandHandler(new RemoveSameStoresAsExcelCommand(propertiesModel))
    commandExecuter.registerCommandHandler(new RemoveSameStoresAsJavaCommand(propertiesModel))
    commandExecuter.registerCommandHandler(new ListStores(propertiesModel))
  }

  def start() =
    {
      println("Welcome to " + name)
      do {
        printStoresSummary
        print(prompt)
        val line = readLine().trim
        if (line.length() > 0) {
          val originalForLastLine = forLastLine
          val orginalLastLine = lastLine
          forLastLine = lastLine
          lastLine = line
          if (commandExecuter.executeCommand(line)) {
            forLastLine = originalForLastLine
            lastLine = orginalLastLine
          } else {
            println("Did not understand: " + line)
          }
        }
      } while (!propertiesModel.exit)
      println("Thank you for using " + name)
    }

  private def printStoresSummary() =
    {
      println("Nr of stores: " + propertiesModel.stores.size)
      if (!showShortSummary) {
        var i = 1;
        for (store <- propertiesModel.stores.getStores()) {
          println(i + ": " + getStoreSummary(store))
          i += 1
        }
      }
    }

  private def getStoreSummary(store: KeyValueStore): String =
    {
      val stringBuilder = new StringBuilder
      stringBuilder.append(store.name)
      stringBuilder.append(" in ")
      var firstLang = true
      for (lang <- store.getSortedLanguages()) {
        if (!firstLang) {
          stringBuilder.append(", ")
        }
        stringBuilder.append(lang)
        firstLang = false
      }
      stringBuilder.append(" with ")
      stringBuilder.append(store.getKeys().size)
      stringBuilder.append(" keys")
      stringBuilder.toString()
    }

}