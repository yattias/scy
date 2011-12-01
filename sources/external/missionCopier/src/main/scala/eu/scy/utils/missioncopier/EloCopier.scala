package eu.scy.utils.missioncopier

import roolo.elo.api.metadata.CoreRooloMetadataKeyIds
import java.net.URI
import roolo.elo.api.IELO
import collection.JavaConversions._
import collection.mutable.{ArrayBuffer, HashMap}

/**
 * Created by IntelliJ IDEA.
 * User: sikken
 * Date: 28-11-11
 * Time: 17:22
 * To change this template use File | Settings | File Templates.
 */

class EloCopier(val stateModel: StateModel) {
  private val technicalFormatKey = stateModel.metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT)
  private val titleKey = stateModel.metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE)
  private val templateKey = stateModel.metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TEMPLATE)
  private val temporaryTechnicalFormat = "empty"

  var eloUris: Seq[URI] = null
  var sourceMissionSpecificationElo: IELO = null
  var destinationMissionSpecificationElo: IELO = null
  var missionSpecificationXml: String = null
  var copyAllVersions = false

  def copyElos(): Unit = {
    var sourceElos = loadElos(eloUris)
    var temporaryDestinationElos = createNewTemporaryElos(sourceElos)
    if (sourceMissionSpecificationElo != null) {
      sourceElos :+= sourceMissionSpecificationElo
    }
    if (destinationMissionSpecificationElo != null) {
      if (sourceMissionSpecificationElo == null) {
        throw new IllegalArgumentException("the sourceMissionSpecificationElo is null, then destinationMissionSpecificationElo must be null to")
      }
      temporaryDestinationElos :+= createNewTemporyMissionSpecificationElo(destinationMissionSpecificationElo)
    } else if (sourceMissionSpecificationElo != null) {
      temporaryDestinationElos :+= createNewTemporaryElo(sourceMissionSpecificationElo)
    }
    val uriTranslationMap = createUriTranslationMap(sourceElos, temporaryDestinationElos)
    val destinationElos = createNewElos(sourceElos, uriTranslationMap)
    storeElos(destinationElos)
    if (missionSpecificationXml != null && !missionSpecificationXml.isEmpty) {
      missionSpecificationXml = replaceUris(missionSpecificationXml, uriTranslationMap)
    }
  }

  private def createNewTemporyMissionSpecificationElo(destinationMissionSpecificationElo: IELO): IELO = {
    val newElo: IELO = createNewElo(destinationMissionSpecificationElo)
    val newMetadata = stateModel.destination.repository.updateELO(newElo, destinationMissionSpecificationElo.getUri)
    stateModel.eloFactory.updateELOWithResult(newElo, newMetadata)
    newElo
  }

  private def loadElos(eloUris: scala.collection.Seq[URI]): Seq[IELO] = {
    val elos = stateModel.source.repository.retrieveELOs(eloUris)
    elos.filter(_ != null)
  }

  private def createNewElo(sourceElo: IELO): IELO = {
    val newElo = stateModel.eloFactory.createELO()
//    val technicalType = if (stateModel.destination.isRooloMock) sourceElo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue() else temporaryTechnicalFormat
    val technicalType = sourceElo.getMetadata().getMetadataValueContainer(technicalFormatKey).getValue()
    newElo.getMetadata().getMetadataValueContainer(technicalFormatKey).setValue(technicalType)
    newElo.getMetadata().getMetadataValueContainer(titleKey).setValuesI18n(sourceElo.getMetadata().getMetadataValueContainer(titleKey).getValuesI18n())
    newElo.getMetadata().getMetadataValueContainer(templateKey).setValue("true")
    newElo
  }

  def createNewTemporaryElo(sourceElo: IELO): IELO = {
    val newElo: IELO = createNewElo(sourceElo)
    val newMetadata = stateModel.destination.repository.addNewELO(newElo)
    stateModel.eloFactory.updateELOWithResult(newElo, newMetadata)
    newElo
  }

  private def createNewTemporaryElos(sourceElos: Seq[IELO]): Seq[IELO] = {
    for (sourceElo <- sourceElos) yield {
      createNewTemporaryElo(sourceElo)
    }
  }

  private def storeElos(destinationElos: Seq[IELO]) = {
    for (elo <- destinationElos) {
      stateModel.destination.repository.updateWithMinorChange(elo)
    }
  }

  private def createUriTranslationMap(sourceElos: Seq[IELO], destinationElos: Seq[IELO]): Map[URI, URI] = {
    val translationMap = HashMap[URI, URI]()
    var localDestinationElos = destinationElos
    for (sourceElo <- sourceElos) {
      translationMap.put(sourceElo.getUri, localDestinationElos.head.getUri)
      localDestinationElos = localDestinationElos.tail
    }
    translationMap.toMap
  }

  private def createNewElos(sourceElos: Seq[IELO], uriTranslationMap: Map[URI, URI]): Seq[IELO] = {
    for (sourceElo <- sourceElos) yield {
      createNewElo(sourceElo, uriTranslationMap)
    }
  }

  private def createNewElo(sourceElo: IELO, uriTranslationMap: Map[URI, URI]): IELO = {
    val sourceEloXml = sourceElo.getXml
    val destinationEloXml = replaceUris(sourceEloXml, uriTranslationMap)
    stateModel.eloFactory.createELOFromXml(destinationEloXml)
  }

  def replaceUris(eloXml: String, uriTranslationMap: Map[URI, URI]): String = {
    var xml = eloXml
    for (entry <- uriTranslationMap) {
      xml = xml.replace(entry._1.toString, entry._2.toString)
    }
    xml
  }
}