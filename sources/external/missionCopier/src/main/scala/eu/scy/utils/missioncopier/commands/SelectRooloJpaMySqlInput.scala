package eu.scy.utils.missioncopier.commands

import eu.scy.utils.missioncopier.StateModel
import roolo.api.IRepository
import roolo.elo.MetadataTypeManager
import org.roolo.rooloimpljpa.repository.Repository
import org.roolo.search.lucene.DirectoryType

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 1-12-11
 * Time: 9:22
 */

class SelectRooloJpaMySqlInput(override val stateModel: StateModel) extends SelectRooloJpaMemoryInput(stateModel) {
  override val commands = Seq("[select] input [from] jpa mysql")
  override val description = "select roolo jpa mysql input"

  override protected val repositoryName = "roolo JPA MySql"

  override protected def createRooloJpa(): IRepository = {
    RooloJpaMySql.repository(stateModel)
  }

}

private object RooloJpaMySql {
  private var _repository: IRepository = null

  def repository(stateModel:StateModel) = {
    if (_repository == null) {
      _repository = createRooloJpaMemory(stateModel)
    }
    _repository
  }

  private def createRooloJpaMemory(stateModel:StateModel): IRepository = {
    val metadataTypeManager = stateModel.metadataTypeManager.asInstanceOf[MetadataTypeManager]
    new Repository(metadataTypeManager, stateModel.extensionManager, stateModel.eloFactory);
  }
}

