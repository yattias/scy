package eu.scy.utils.missioncopier

import console.ConsoleModel
import roolo.elo.api.IMetadataTypeManager
import roolo.elo.api.IELOFactory
import roolo.api.IRepository
import roolo.api.IExtensionManager

class StateModel(val metadataTypeManager: IMetadataTypeManager, val extensionManager: IExtensionManager, val eloFactory: IELOFactory) extends ConsoleModel {

  def source = sourceRepository
  def destination = destinationRepository

  private var sourceRepository: RepositoryDefinition = null
  private var destinationRepository: RepositoryDefinition = null

  def setSource(repository: IRepository, name: String, isRooloMock: Boolean) = {
    sourceRepository = new RepositoryDefinition(repository, name, isRooloMock, this)
  }

  def setDestination(repository: IRepository, name: String, isRooloMock: Boolean) = {
    destinationRepository = new RepositoryDefinition(repository, name, isRooloMock, this)
  }

  def swapSourceDestination() = {
    val temporarySourceRepository = sourceRepository
    sourceRepository = destinationRepository
    destinationRepository = temporarySourceRepository
  }

}

case class RepositoryDefinition(val repository: IRepository, val name: String, val isRooloMock: Boolean, val stateModel: StateModel) {
  val rooloServices = new SimpleRooloServices(repository, stateModel.metadataTypeManager, stateModel.extensionManager, stateModel.eloFactory)
}


