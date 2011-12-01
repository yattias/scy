package eu.scy.utils.missioncopier.commands

import roolo.api.IRepository
import org.roolo.rooloimpljpa.repository.Repository
import eu.scy.utils.missioncopier.StateModel
import roolo.elo.MetadataTypeManager
import org.roolo.search.lucene.DirectoryType

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 29-11-11
 * Time: 18:46
 */

class SelectRooloJpaMemoryInput(override val stateModel: StateModel) extends StateModelCommandHandler(stateModel) {
  val commands = Seq("[select] input [from] jpa memory")
  val description = "select roolo jpa memory input"

  protected def isForSource = true

  protected def repositoryName = "roolo JPA memory"

  private var repository: IRepository = null

  def execute(params: Seq[String]): Unit = {
    if (!stateModel.metadataTypeManager.isInstanceOf[MetadataTypeManager]) {
      throw new IllegalArgumentException("stateModel.metadataTypeManager must be an instance of MetadataTypeManager, but is of type " + stateModel.metadataTypeManager.getClass().getName())
    }
    checkIfRepositoryIsCreated()

    if (checkIfAlreadySet) {
      println((if (isForSource) "Source" else "Destination") + " is already set to " + repositoryName)
      return
    }
   if (checkIfRepositoryIsUsed) {
      println(repositoryName + " cannot be used for source and destination")
      return
    }
    setRepository(repository)
  }

  private def checkIfRepositoryIsCreated() = {
    if (repository == null) {
      repository = createRooloJpa()
    }
  }

  private def checkIfAlreadySet(): Boolean = {
    if (isForSource){
      stateModel.source!=null && (stateModel.source.repository eq repository)
    } else {
      stateModel.destination!=null && (stateModel.destination.repository eq repository)
    }
  }

  private def checkIfRepositoryIsUsed(): Boolean = {
    if (stateModel.source != null && !isForSource) {
      if (repository eq stateModel.source.repository) {
        return true
      }
    }
    if (stateModel.destination != null && isForSource) {
      if (repository eq stateModel.destination.repository) {
        return true
      }
    }
    return false
  }

  protected def createRooloJpa(): IRepository = {
    RooloJpaMemory.repository(stateModel)
  }

  protected def setRepository(repository: IRepository) = {
    if (isForSource) {
      stateModel.setSource(repository, repositoryName, false)
    } else {
      stateModel.setDestination(repository, repositoryName, false)
    }
  }

}

private object RooloJpaMemory {
  private var _repository: IRepository = null

  def repository(stateModel:StateModel) = {
    if (_repository == null) {
      _repository = createRooloJpaMemory(stateModel)
    }
    _repository
  }

  private def createRooloJpaMemory(stateModel:StateModel): IRepository = {
    val metadataTypeManager = stateModel.metadataTypeManager.asInstanceOf[MetadataTypeManager]
    new Repository(Repository.PERSISTENCE_UNIT_NAME_TESTING_H2, metadataTypeManager, stateModel.eloFactory, DirectoryType.RAM, true, false, false);
  }
}

