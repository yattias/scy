package eu.scy.utils.missioncopier.commands

import eu.scy.utils.missioncopier.copying.RealMissionCopier
import eu.scy.utils.missioncopier.{RepositoryDefinition, StateModel}

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
    listMissions(stateModel.source, "Source")
    listMissions(stateModel.destination, "Destination")
  }

  private def listMissions(repositoryDefinition: RepositoryDefinition, label: String): Unit = {
    if (repositoryDefinition == null) {
      println(label + " is not defined")
    } else {
      val missionCopier = new RealMissionCopier(stateModel)
      println(label + ":")
      val missionSpecificationElos = missionCopier.findMissionSpecificationElos(repositoryDefinition)
      for (missionSpecificationElo <- missionSpecificationElos) {
        val allMissionSpecificationEloMetadatas = repositoryDefinition.repository.retrieveMetadataAllVersions(missionSpecificationElo.getUri)
        val nrOfVersions = allMissionSpecificationEloMetadatas.size()
        println("- " + Utils.getEloDisplayDescription(missionSpecificationElo,stateModel) + ", (" + nrOfVersions + " versions)")
      }
    }
  }
}
