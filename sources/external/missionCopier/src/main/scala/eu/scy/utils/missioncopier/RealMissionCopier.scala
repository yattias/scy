package eu.scy.utils.missioncopier

import commands.Utils
import eu.scy.common.mission.MissionSpecificationElo
import roolo.search.MetadataQueryComponent
import roolo.search.Query
import roolo.search.SearchOperation
import eu.scy.common.mission.MissionEloType
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds
import scala.collection.JavaConversions._
import java.net.URI
import eu.scy.common.mission.MissionModelElo
import scala.collection.mutable.ArrayBuffer

class RealMissionCopier(val stateModel: StateModel) {
  private val technicalFormatKey = stateModel.metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT)
  val missionSpecificationElos = findMissionSpecificationElos(stateModel.source)

  def findMissionSpecificationElos(repositoryDefinition: RepositoryDefinition): Seq[MissionSpecificationElo] = {
    if (repositoryDefinition == null) {
      return Seq[MissionSpecificationElo]()
    }
    val missionSpecificationQueryComponent = new MetadataQueryComponent(technicalFormatKey,
      SearchOperation.EQUALS, MissionEloType.MISSION_SPECIFICATIOM.getType());
    val missionSpecificationQuery = new Query(missionSpecificationQueryComponent);
    val missionSpecificationResults = repositoryDefinition.rooloServices.getRepository.search(missionSpecificationQuery);
    for (missionSpecificationResult <- missionSpecificationResults) yield {
      MissionSpecificationElo.loadElo(missionSpecificationResult.getUri(), repositoryDefinition.rooloServices);
    }
  }

  def copyMission(missionName: String): Unit = {
    println("now trying to copy mission: " + missionName)
    def missionSpecificationElo = findMissionSpecificationElo(missionName)
    if (missionSpecificationElo == null) {
      println("cannot find mission named: " + missionName)
    } else {
      copyMission(missionSpecificationElo)
    }
  }

  private def findMissionSpecificationElo(missionName: String): MissionSpecificationElo = {
    for (missionSpecificationElo <- missionSpecificationElos) {
      if (missionSpecificationElo.getTitle() == missionName) {
        return missionSpecificationElo
      }
    }
    return null
  }

  def copyMission(missionSpecificationElo: MissionSpecificationElo): Boolean = {
    println("trying to copy mission: " + missionSpecificationElo.getUri())
    val eloCopier = new EloCopier(stateModel)
    eloCopier.eloUris = getAllEloUris(missionSpecificationElo)
    eloCopier.sourceMissionSpecificationElo = missionSpecificationElo.getElo
    val oldMissionSpecificationElo = findOldMissionSpecificationElo(missionSpecificationElo)
    if (oldMissionSpecificationElo != null) {
      println("Mission does already exist: " + Utils.getEloDisplayDescription(missionSpecificationElo, stateModel))
      Utils.askYesNo(stateModel, "Do you want to update the mission") match {
        case Some(true) =>
        case _ => return false
      }
      eloCopier.destinationMissionSpecificationElo = findOldMissionSpecificationElo(missionSpecificationElo).getElo
    }
    eloCopier.copyElos()
    return true
  }

  private def findOldMissionSpecificationElo(newMissionSpecificationElo: MissionSpecificationElo): MissionSpecificationElo = {
    def presentMissionSpecificationElos = findMissionSpecificationElos(stateModel.destination)
    for (missionSpecificationElo <- presentMissionSpecificationElos) {
      if (missionSpecificationElo.getMissionId() == newMissionSpecificationElo.getMissionId() &&
        missionSpecificationElo.getContent.getLanguages() == newMissionSpecificationElo.getContent.getLanguages()) {
        return missionSpecificationElo
      }
    }
    return null
  }

  private def getAllEloUris(missionSpecificationElo: MissionSpecificationElo): Seq[URI] = {
    def missionSpecificationEloContent = missionSpecificationElo.getTypedContent()
    val missionMapModelElo = MissionModelElo.loadElo(missionSpecificationEloContent.getMissionMapModelEloUri(), stateModel.source.rooloServices);
    val eloUris = ArrayBuffer[URI]()
    eloUris += missionSpecificationEloContent.getMissionMapModelEloUri()
    eloUris += missionSpecificationEloContent.getEloToolConfigsEloUri()
    eloUris += missionSpecificationEloContent.getTemplateElosEloUri
    eloUris += missionSpecificationEloContent.getRuntimeSettingsEloUri
    eloUris += missionSpecificationEloContent.getAgentModelsEloUri()
    eloUris += missionSpecificationEloContent.getPedagogicalPlanSettingsEloUri()
    eloUris += missionSpecificationEloContent.getColorSchemesEloUri()
    eloUris ++= missionMapModelElo.getTypedContent().getEloUris(true)
    //      eloUris += missionSpecificationElo.getUri()
    eloUris.filter(_ != null)
  }
}