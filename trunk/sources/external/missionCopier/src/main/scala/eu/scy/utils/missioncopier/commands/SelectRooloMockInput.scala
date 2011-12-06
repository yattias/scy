package eu.scy.utils.missioncopier.commands

import java.io.File
import eu.scy.utils.missioncopier.StateModel
import roolo.api.IRepository
import roolo.cms.repository.mock.MockRepository

class SelectRooloMockInput(override val stateModel: StateModel) extends StateModelCommandHandler(stateModel) {
  val commands = Seq("[select] input [from] elostore")
  val description = "select roolo mock input"
  override val paramDescription = "directory [minimumNewVersionId]"
  val eloStoreDirectoryName = "eloStore"

  def execute(params: Seq[String]): Unit = {
    if (params.isEmpty) {
      println("eloStore directory is not specified")
    } else {
      val eloStoreDirectory = findEloStoreDirectory(new File(params.head))
      if (eloStoreDirectory != null) {
        val minimumNewVersionId = findMinimumNewVersionId(params.tail)
        if (minimumNewVersionId != null)
          setRepository(loadRooloMock(eloStoreDirectory, minimumNewVersionId), eloStoreDirectory)
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

  private def findMinimumNewVersionId(params: Seq[String]): java.lang.Integer = {
    var minimumNewVersionId = 0
    if (!params.isEmpty) {
      val minimumNewVersionIdString = params.head
      try {
        minimumNewVersionId = Integer.parseInt(minimumNewVersionIdString)
      } catch {
        case e: NumberFormatException =>
          println("minimumNewVersionId (" + minimumNewVersionIdString + ") must be a positive number, " + e.getMessage)
          return null
      }
      if (minimumNewVersionId < 0) {
        println("minimumNewVersionId (" + minimumNewVersionIdString + ") must be a positive number")
        return null
      }
    }
    minimumNewVersionId
  }

  protected def handleNonExistingEloStoreDirectory(eloStoreDirectory: File): Unit = {
    println("eloStoreDirectory does not exist or is not a directory: " + eloStoreDirectory.getCanonicalPath())
  }

  private def loadRooloMock(eloStoreDirectory: File, minimumNewVersionId: java.lang.Integer): IRepository = {
    val repository = new MockRepository()
    repository.setEloStoreDirectory(eloStoreDirectory)
    repository.setEloFactory(stateModel.eloFactory)
    repository.setExtensionManager(stateModel.extensionManager)
    repository.setMetadataTypeManager(stateModel.metadataTypeManager)
    repository.setMinimumNewVersionListId(minimumNewVersionId.toString)
    //      repository.initialise()
    repository
  }

  protected def setRepository(repository: IRepository, eloStoreDirectory: File) = {
    stateModel.setSource(repository, "eloStore in directory: " + eloStoreDirectory.getCanonicalPath, true)
  }
}