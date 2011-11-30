package eu.scy.utils.missioncopier.commands

import eu.scy.utils.missioncopier.StateModel

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 30-11-11
 * Time: 14:12
 * To change this template use File | Settings | File Templates.
 */

class SwapSourceDestinationCommand (override val stateModel: StateModel) extends StateModelCommandHandler(stateModel) {
  val commands = Seq("swap")
  val description = "swap source destination"

  def execute(params: Seq[String]): Unit = {
    stateModel.swapSourceDestination()
  }
}