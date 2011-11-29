package eu.scy.utils.missioncopier.commands

import eu.scy.utils.missioncopier.{RealMissionCopier, StateModel}
import eu.scy.common.scyelo.RooloServices

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 29-11-11
 * Time: 15:59
 * To change this template use File | Settings | File Templates.
 */

class ListMissionsCommand(override val stateModel: StateModel) extends StateModelCommandHandler(stateModel) {
  val commands = Seq("list missions")
  val description = "list mission"

  def execute(params: Seq[String]): Unit = {
    listMissions(stateModel.sourceRooloServices, "Source")
    listMissions(stateModel.destinationRooloServices, "Destination")
  }

  private def listMissions(rooloServices: RooloServices, label: String): Unit = {
    if (rooloServices == null) {
      println(label + " is not defined")
    } else {
      val missionCopier = new RealMissionCopier(stateModel)
      println(label + ":")
      val missionSpecificationElos = missionCopier.findMissionSpecificationElos(rooloServices)
      for (missionSpecificationElo <- missionSpecificationElos) {
        val allMissionSpecificationEloMetadatas = rooloServices.getRepository().retrieveMetadataAllVersions(missionSpecificationElo.getUri)
        val nrOfVersions = allMissionSpecificationEloMetadatas.size()
        println("- " + missionSpecificationElo.getTitle + " (" + missionSpecificationElo.getContent().getLanguages + ", " + nrOfVersions + " versions)")
      }
    }
  }
}
