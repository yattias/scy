package eu.scy.utils.missioncopier.commands

import roolo.api.IRepository
import org.roolo.rooloimpljpa.repository.Repository
import eu.scy.utils.missioncopier.{SimpleRooloServices, StateModel}
import roolo.elo.MetadataTypeManager
import org.roolo.search.lucene.DirectoryType

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 29-11-11
 * Time: 18:46
 * To change this template use File | Settings | File Templates.
 */

class SelectRooloJpaInput(override val stateModel: StateModel) extends StateModelCommandHandler(stateModel) {
  val commands = Seq("[select] input [from] roolo-jpa")
  val description = "select roolo jpa input"

  def execute(params: Seq[String]): Unit = {
    setRepository(createRooloJpa())
  }

  protected def createRooloJpa(): IRepository = {
    if (!stateModel.metadataTypeManager.isInstanceOf[MetadataTypeManager]){
      throw new IllegalArgumentException("stateModel.metadataTypeManager must be an instance of MetadataTypeManager, but is of type " + stateModel.metadataTypeManager.getClass().getName())
    }
    val metadataTypeManager = stateModel.metadataTypeManager.asInstanceOf[MetadataTypeManager]
    new Repository(Repository.PERSISTENCE_UNIT_NAME_TESTING_H2, metadataTypeManager, stateModel.eloFactory, DirectoryType.RAM, true, false, false);
  }

  protected def setRepository(repository: IRepository) = {
    stateModel.setSource(repository,"roolo JPA",false)
  }

}