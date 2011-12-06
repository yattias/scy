package eu.scy.utils.missioncopier

import commands._
import console.{ConsoleController, CommandHandler}
import roolo.elo.JDomBasicELOFactory

class ConsoleControl {

  private val name = "mission copier"
  private val prompt = ">";


  private val stateModelVals = createStateModelVals()
  private val stateModel = new StateModel(stateModelVals._1,stateModelVals._2,stateModelVals._3)
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
    createCommandHandlers
    consoleController.showState = showState
  }

  def createStateModelVals() = {
    val metadataTypeManager = SpringLoader.getMetadataTypeManager()
    val extensionManager = SpringLoader.getExtensionManager()
    val eloFactory = new JDomBasicELOFactory(metadataTypeManager)
    (metadataTypeManager,extensionManager,eloFactory)
  }

  def createCommandHandlers() = {
    consoleController.createStandardCommandHandlers()
    consoleController.addCommandHandler(new SetShortSummaryCommand())
    consoleController.addCommandHandler(new SelectRooloMockInput(stateModel))
    consoleController.addCommandHandler(new SelectRooloMockOutput(stateModel))
    consoleController.addCommandHandler(new SelectRooloJpaMemoryInput(stateModel))
    consoleController.addCommandHandler(new SelectRooloJpaMemoryOutput(stateModel))
    consoleController.addCommandHandler(new SelectRooloJpaMySqlInput(stateModel))
    consoleController.addCommandHandler(new SelectRooloJpaMySqlOutput(stateModel))
    consoleController.addCommandHandler(new SelectRooloRemoteInput(stateModel))
    consoleController.addCommandHandler(new SelectRooloRemoteOutput(stateModel))
    consoleController.addCommandHandler(new ListMissionsCommand(stateModel))
    consoleController.addCommandHandler(new ListElosCommand(stateModel))
    consoleController.addCommandHandler(new SearchElosCommand(stateModel))
    consoleController.addCommandHandler(new CopyMissionCommand(stateModel))
    consoleController.addCommandHandler(new CopyElosCommand(stateModel))
    consoleController.addCommandHandler(new SwapSourceDestinationCommand(stateModel))
    consoleController.addCommandHandler(new TimeRetrieveElosCommand(stateModel))
  }

  def start(args : Array[String]) = {
    consoleController.start(args)
  }

  private def showState(): Unit = {
    println("Source     : " + (if (stateModel.source==null) "" else stateModel.source.name))
    println("Destination: " + (if (stateModel.destination==null) "" else stateModel.destination.name))
  }
}