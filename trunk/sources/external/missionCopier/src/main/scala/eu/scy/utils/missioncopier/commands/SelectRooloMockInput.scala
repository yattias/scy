package eu.scy.utils.missioncopier.commands

import java.io.File
import eu.scy.utils.missioncopier.StateModel
import roolo.api.IRepository
import roolo.cms.repository.mock.MockRepository
import eu.scy.utils.missioncopier.SimpleRooloServices

class SelectRooloMockInput(override val stateModel: StateModel) extends StateModelCommandHandler(stateModel) {
  val commands = Seq("[select] input [from] elostore")
  val description = "select roolo mock input"
  override val paramDescription = "directory"
  val eloStoreDirectoryName = "eloStore"

  def execute(params: Seq[String]): Unit = {
    if (params.isEmpty) {
      println("eloStore directory is not specified")
    } else {
      val eloStoreDirectory = findEloStoreDirectory(new File(params.head))
      if (eloStoreDirectory != null) {
        setRepository(loadRooloMock(eloStoreDirectory),eloStoreDirectory)
      }
    }
  }

  private def findEloStoreDirectory(directory: File): File = {
    var eloStoreDirectory = directory
    if (eloStoreDirectory.getName() != eloStoreDirectoryName) {
      eloStoreDirectory = new File(eloStoreDirectory, "eloStore")
    }
    if (!isExistingDirectory(eloStoreDirectory)) {
      handleNonExistingEloStoreDirectory(eloStoreDirectory)
    }
    if (isExistingDirectory(eloStoreDirectory)) {
      return eloStoreDirectory
    }
    return null
  }

  private def isExistingDirectory(directory: File): Boolean = {
    if (!directory.exists()) {
      return false
    }
    if (!directory.isDirectory()) {
      return false
    }
    true
  }

  protected def handleNonExistingEloStoreDirectory(eloStoreDirectory: File): Unit = {
    println("eloStoreDirectory does not exist or is not a directory: " + eloStoreDirectory.getAbsolutePath())
  }

  private def loadRooloMock(eloStoreDirectory: File): IRepository = {
    val repository = new MockRepository()
    repository.setEloStoreDirectory(eloStoreDirectory)
    repository.setEloFactory(stateModel.eloFactory)
    repository.setExtensionManager(stateModel.extensionManager)
    repository.setMetadataTypeManager(stateModel.metadataTypeManager)
    //      repository.initialise()
    repository
  }

  protected def setRepository(repository: IRepository, eloStoreDirectory: File) = {
    stateModel.sourceRepository = repository
    stateModel.sourceRooloServices = new SimpleRooloServices(stateModel.sourceRepository, stateModel.metadataTypeManager, stateModel.extensionManager, stateModel.eloFactory)
    stateModel.sourceIsRooloMock = true
    stateModel.sourceName = "eloStore in directory: " + eloStoreDirectory.getAbsolutePath
  }
}