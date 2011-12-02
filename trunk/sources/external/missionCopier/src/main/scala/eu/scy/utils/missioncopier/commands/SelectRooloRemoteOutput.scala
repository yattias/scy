package eu.scy.utils.missioncopier.commands

import eu.scy.utils.missioncopier.StateModel

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 2-12-11
 * Time: 17:27
 */

class SelectRooloRemoteOutput  (override val stateModel: StateModel) extends SelectRooloRemoteInput(stateModel) {
  override val commands = Seq("[select] output [from] remote")
  override val description = "select roolo remote output"

  override protected val isForSource = false

}