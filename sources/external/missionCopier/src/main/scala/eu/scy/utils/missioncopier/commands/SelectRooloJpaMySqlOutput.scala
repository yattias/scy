package eu.scy.utils.missioncopier.commands

import eu.scy.utils.missioncopier.StateModel
import roolo.api.IRepository

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 1-12-11
 * Time: 9:27
 */

class SelectRooloJpaMySqlOutput (override val stateModel: StateModel) extends SelectRooloJpaMySqlInput(stateModel) {
  override val commands = Seq("[select] output [to] jpa mysql")
  override val description = "select roolo jpa mysql output"

  override protected val isForSource = false

}