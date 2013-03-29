package eu.scy.utils.missioncopier.copying

import roolo.elo.api.metadata.CoreRooloMetadataKeyIds
import java.net.URI
import roolo.elo.api.IELO
import collection.JavaConversions._
import collection.mutable.HashMap
import eu.scy.utils.missioncopier.StateModel

/**
 * Created by IntelliJ IDEA.
 * User: sikken
 * Date: 28-11-11
 * Time: 17:22
 */

class EloCopier(val stateModel: StateModel) {
  private val technicalFormatKey = stateModel.metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT)
  private val titleKey = stateModel.metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE)
  private val templateKey = stateModel.metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TEMPLATE)

  var eloUris: Seq[URI] = null
  var sourceMissionSpecificationElo: IELO = null
  var destinationMissionSpecificationElo: IELO = null
  var missionSpecificationXml: String = null
  var copyAllVersions = false

  def copyElos(): Unit = {
    if (destinationMissionSpecificationElo != null && sourceMissionSpecificationElo == null) {
      throw new IllegalArgumentException("the sourceMissionSpecificationElo is null, then destinationMissionSpecificationElo must be null to")
    }
    println("Loading ELOs")
    var sourceElos = loadElos(eloUris)
    print("Creating new temporary ELOs")
    var temporaryDestinationElos = createNewTemporaryElos(sourceElos)
    if (sourceMissionSpecificationElo != null) {
      sourceElos :+= sourceMissionSpecificationElo
    }
    if (destinationMissionSpecificationElo != null) {
      temporaryDestinationElos :+= createNewTemporyMissionSpecificationElo(destinationMissionSpecificationElo)
    } else if (sourceMissionSpecificationElo != null) {
      temporaryDestinationElos :+= createNewTemporaryElo(sourceMissionSpecificationElo)
    }
    println("\nUpdating URIs in new ELOs")
    val uriTranslationMap = createUriTranslationMap(sourceElos, temporaryDestinationElos)
    val destinationElos = createNewElos(sourceElos, uriTranslationMap)
    print("Storing new ELOs")
    storeElos(destinationElos)
    println()
    if (missionSpecificationXml != null && !missionSpecificationXml.isEmpty) {
      println("Updating URIs in mission specification XML")
      missionSpecificationXml = replaceUris(missionSpecificationXml, uriTranslationMap)
    }
    println("Finshed copying ELOs")
  }

  private def createNewTemporyMissionSpecificationElo(destinationMissionSpecificationElo: IELO): IELO = {
    val newElo: IELO = createNewElo(destinationMissionSpecificationElo)
    val newMetadata = stateModel.destination.repository.updateELO(newElo, destinationMissionSpecificationElo.getUri)
    stateModel.eloFactory.updateELOWithResult(newElo, newMetadata)
    newElo
  }

  private def loadElos(eloUris: scala.collection.Seq[URI]): Seq[IELO] = {
//    val elos = stateModel.source.repository.retrieveELOs(eloUris)
    val elos = eloUris.map(stateModel.source.repository.retrieveELO(_))
    elos.filter(_ != null)
  }

  private def createNewElo(sourceElo: IELO): IELO = {
    val newElo = stateModel.eloFactory.createELO()
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
      val elo = createNewTemporaryElo(sourceElo)
      print(".")
      elo
    }
  }

  private def storeElos(destinationElos: Seq[IELO]) = {
    for (elo <- destinationElos) {
      stateModel.destination.repository.updateWithMinorChange(elo)
      print(".")
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