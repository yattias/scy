package eu.scy.utils.missioncopier.commands

import roolo.api.IRepository
import eu.scy.utils.missioncopier.{SimpleRooloServices, StateModel}

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 30-11-11
 * Time: 11:10
 */

class SelectRooloJpaMemoryOutput(override val stateModel: StateModel) extends SelectRooloJpaMemoryInput(stateModel) {
  override val commands = Seq("[select] output [to] jpa memory")
  override val description = "select roolo jpa memory output"

  override protected def isForSource = false

}