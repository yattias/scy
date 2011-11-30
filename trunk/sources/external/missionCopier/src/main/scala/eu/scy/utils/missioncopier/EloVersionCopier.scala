package eu.scy.utils.missioncopier

import roolo.elo.api.metadata.CoreRooloMetadataKeyIds
import java.net.URI
import collection.JavaConversions._
import roolo.elo.api.IELO
import collection.mutable.{Buffer, ArrayBuffer, HashMap}

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 30-11-11
 * Time: 15:46
 * To change this template use File | Settings | File Templates.
 */

class EloVersionCopier(val stateModel: StateModel) {
  private val technicalFormatKey = stateModel.metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT)
  private val titleKey = stateModel.metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE)
  private val templateKey = stateModel.metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TEMPLATE)
  private val temporaryTechnicalFormat = "empty"

  var eloUris: Seq[URI] = null

  private case class EloPair(val sourceElo: IELO, val destinationElo: IELO)

  private var eloPairs = ArrayBuffer[Buffer[EloPair]]()
  private var uriTranslationMap = HashMap[URI, URI]()

  def copyElos() = {
    loadEloPairs()
    fillAndStoreDestinationElos()
//    storeDestinationElos()
  }

  private def loadEloPairs() = {
    for (eloUri <- eloUris) {
      if (!uriTranslationMap.containsKey(eloUri)) {
        val versionList: Buffer[IELO] = stateModel.source.repository.retrieveELOAllVersions(eloUri)
        if (!versionList.isEmpty) {
          loadEloPairsForVersionList(versionList)
        }
      }
    }
  }

  private def loadEloPairsForVersionList(versionList: Buffer[IELO]) = {
    val eloPairList = ArrayBuffer[EloPair]()
    var lastNewElo: IELO = null
    for (elo <- versionList) {
      if (elo != null) {
        val newElo = if (lastNewElo == null) {
          createNewTemporaryElo(elo)
        } else {
          createNewTemporaryVersionElo(lastNewElo)
        }
        lastNewElo = newElo
        eloPairList += new EloPair(elo, newElo)
        uriTranslationMap.put(elo.getUri, newElo.getUri)
      }
    }
    eloPairs += eloPairList
  }

  private def createNewElo(sourceElo: IELO): IELO = {
    val newElo = stateModel.eloFactory.createELO()
    val technicalType = if (stateModel.destination.isRooloMock) sourceElo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue() else temporaryTechnicalFormat
    newElo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(technicalType)
    newElo.getMetadata().getMetadataValueContainer(titleKey).setValuesI18n(sourceElo.getMetadata().getMetadataValueContainer(titleKey).getValuesI18n())
    newElo.getMetadata().getMetadataValueContainer(templateKey).setValue("true")
    newElo
  }

  private def createNewTemporaryElo(sourceElo: IELO): IELO = {
    val newElo: IELO = createNewElo(sourceElo)
    val newMetadata = stateModel.destination.repository.addNewELO(newElo)
    stateModel.eloFactory.updateELOWithResult(newElo, newMetadata)
    newElo
  }

  private def createNewTemporaryVersionElo(elo: IELO): IELO = {
    val newElo = cloneElo(elo)
    val newMetadata = stateModel.destination.repository.updateELO(newElo)
    stateModel.eloFactory.updateELOWithResult(newElo, newMetadata)
    newElo
  }

  private def cloneElo(elo:IELO) : IELO = {
//    elo.clone()
     val xml = elo.getXml()
     stateModel.eloFactory.createELOFromXml(xml)
  }

  private def fillAndStoreDestinationElos() = {
    for (eloPairList <- eloPairs) {
      for (eloPair <- eloPairList) {
        fillDestinationElo(eloPair)
      }
    }
  }

  private def fillDestinationElo(eloPair: EloPair) = {
    val sourceEloXml = eloPair.sourceElo.getXml
    val destinationEloXml = replaceUris(sourceEloXml)
    val destinationElo = stateModel.eloFactory.createELOFromXml(destinationEloXml)
    stateModel.destination.repository.updateWithMinorChange(destinationElo)
  }

  def replaceUris(eloXml: String): String = {
    var xml = eloXml
    for (entry <- uriTranslationMap) {
      xml = xml.replace(entry._1.toString, entry._2.toString)
    }
    xml
  }

  private def storeDestinationElos() = {
    for (eloPairList <- eloPairs) {
      for (eloPair <- eloPairList) {
        stateModel.destination.repository.updateWithMinorChange(eloPair.destinationElo)
      }
    }
  }


}