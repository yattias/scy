package eu.scy.utils.missioncopier.commands

import roolo.api.IRepository
import eu.scy.utils.missioncopier.{SimpleRooloServices, StateModel}

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 30-11-11
 * Time: 11:10
 * To change this template use File | Settings | File Templates.
 */

class SelectRooloJpaOutput(override val stateModel: StateModel) extends SelectRooloJpaInput(stateModel) {
  override val commands = Seq("[select] output [to] roolo-jpa")
  override val description = "select roolo jpa output"

  override protected def setRepository(repository: IRepository) = {
    stateModel.setDestination(repository,"roolo JPA",false)
  }

}