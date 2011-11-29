package eu.scy.utils.missioncopier.commands

import eu.scy.utils.missioncopier.StateModel
import roolo.api.IRepository
import eu.scy.utils.missioncopier.SimpleRooloServices
import java.io.File

class SelectRooloMockOutput(override val stateModel: StateModel) extends SelectRooloMockInput(stateModel) {
  override val commands = Seq("[select] output [to] elostore")
  override val description = "select roolo mock output"

  override protected def handleNonExistingEloStoreDirectory(eloStoreDirectory: File): Unit = {
    println("eloStoreDirectory does not exist or is not a directory: " + eloStoreDirectory.getAbsolutePath())
    print("Do you want to create it (y|n) ?")
    val input = stateModel.readLine().trim().toLowerCase
    input match {
      case "y" => if (eloStoreDirectory.mkdirs()) {
        return eloStoreDirectory
      } else {
        println("failed to create eloStoreDirectory: " + eloStoreDirectory)
      }
      case _ =>
    }
  }

  override protected def setRepository(repository: IRepository, eloStoreDirectory: File) = {
    stateModel.destinationRepository = repository
    stateModel.destinationRooloServices = new SimpleRooloServices(stateModel.destinationRepository, stateModel.metadataTypeManager, stateModel.extensionManager, stateModel.eloFactory)
    stateModel.destinationIsRooloMock = true
    stateModel.destinationName = "eloStore in directory: " + eloStoreDirectory.getAbsolutePath
  }

}