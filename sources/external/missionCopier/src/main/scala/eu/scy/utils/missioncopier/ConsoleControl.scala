package eu.scy.utils.missioncopier

import commands._
import console.{ConsoleController, CommandHandler}
import roolo.elo.JDomBasicELOFactory

class ConsoleControl {

  private val name = "mission copier"
  private val prompt = ">";
  private val stateModel = new StateModel
  private val consoleController = new ConsoleController(name, prompt, stateModel)
  private var showShortSummary = true

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

  def intialize():Unit = {
    initStateModel
    createCommandHandlers
    consoleController.showState = showState
  }

  private def initStateModel() = {
    stateModel.metadataTypeManager = SpringLoader.getMetadataTypeManager()
    stateModel.extensionManager = SpringLoader.getExtensionManager()
    stateModel.eloFactory = new JDomBasicELOFactory(stateModel.metadataTypeManager)
  }

  def createCommandHandlers() = {
    consoleController.createStandardCommandHandlers()
    consoleController.addCommandHandler(new SetShortSummaryCommand())
    consoleController.addCommandHandler(new SelectRooloMockInput(stateModel))
    consoleController.addCommandHandler(new SelectRooloMockOutput(stateModel))
    consoleController.addCommandHandler(new ListMissionsCommand(stateModel))
    consoleController.addCommandHandler(new ListElosCommand(stateModel))
    consoleController.addCommandHandler(new CopyMissionCommand(stateModel))
  }

  def start() = {
    consoleController.start()
  }

  private def showState(): Unit = {
    println("Source     : " + stateModel.sourceName)
    println("Destination: " + stateModel.destinationName)
  }
}