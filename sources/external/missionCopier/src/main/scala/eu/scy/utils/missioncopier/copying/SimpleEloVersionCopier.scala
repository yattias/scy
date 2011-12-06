package eu.scy.utils.missioncopier.copying

import roolo.elo.api.metadata.CoreRooloMetadataKeyIds
import java.net.URI
import collection.JavaConversions._
import roolo.elo.api.IELO
import collection.mutable.{Buffer, ArrayBuffer, HashMap}
import eu.scy.utils.missioncopier.StateModel

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 30-11-11
 * Time: 15:46
 */

class SimpleEloVersionCopier(val stateModel: StateModel) extends EloVersionCopier {
  private val technicalFormatKey = stateModel.metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT)
  private val titleKey = stateModel.metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE)
  private val templateKey = stateModel.metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TEMPLATE)

  private var eloUris: Seq[URI] = null

  private case class EloPair(val sourceElo: IELO, var destinationElo: IELO)

  private var eloPairs = ArrayBuffer[Buffer[EloPair]]()
  private var uriTranslationMap = HashMap[URI, URI]()

  def copyElos(eloUris: Seq[URI]) = {
    this.eloUris = eloUris
    print("Loading ELOs and creating new temporary")
    loadEloPairs()
    print("\nUpdating URIs in new ELOs")
    fillDestinationElos()
    print("\nStoring new ELOs")
    storeDestinationElos()
    println("\nFinished copying ELOs")
  }

  private def loadEloPairs() = {
    for (eloUri <- eloUris) {
      if (!uriTranslationMap.containsKey(eloUri)) {
        val versionList: Buffer[IELO] = stateModel.source.repository.retrieveELOAllVersions(eloUri)
        if (!versionList.isEmpty) {
          loadEloPairsForVersionList(versionList)
          print(".")
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
    val technicalType = sourceElo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue()
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

  private def fillDestinationElos() = {
    for (eloPairList <- eloPairs) {
      for (eloPair <- eloPairList) {
        fillDestinationElo(eloPair)
        print(".")
      }
    }
  }

  private def fillDestinationElo(eloPair: EloPair) = {
    val sourceEloXml = eloPair.sourceElo.getXml
    val destinationEloXml = replaceUris(sourceEloXml)
    eloPair.destinationElo = stateModel.eloFactory.createELOFromXml(destinationEloXml)
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
        print(".")
      }
    }
  }


}