package eu.scy.utils.missioncopier.copying

import eu.scy.utils.missioncopier.StateModel
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds
import java.net.URI
import roolo.elo.api.IELO
import collection.JavaConversions._
import collection.mutable.{HashSet, HashMap, ArrayBuffer, Buffer}
import java.lang.IllegalStateException

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 6-12-11
 * Time: 10:50
 */

class SaverEloVersionCopier(val stateModel: StateModel) extends EloVersionCopier {
  private val technicalFormatKey = stateModel.metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TECHNICAL_FORMAT)
  private val titleKey = stateModel.metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE)
  private val templateKey = stateModel.metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TEMPLATE)

  private var eloUris: Seq[URI] = null

  private case class EloPair(val sourceElo: IELO, var destinationElo: IELO)

  private case class EloPairWithSize(val eloPair: EloPair, val xmlSize: java.lang.Integer)

  private var eloPairVersionList = ArrayBuffer[ArrayBuffer[EloPair]]()
  private var sourceUris = HashSet[URI]()

  private var largestElo: EloPairWithSize = null

  private var uriTranslationMap = HashMap[URI, URI]()

  private val progressDisplay = new ProgressDisplay()

  def copyElos(eloUris: Seq[URI]): Unit = {
    this.eloUris = eloUris
    println("Loading ELOs")
    var startNanos = System.nanoTime()
    loadElos()
    val usedNanosForLoadElos = System.nanoTime()-startNanos
    if (notEnoughMemory()) {
      println("There is not enough memory to convert the source ELOs to the destination ELOs")
      return
    }
    println("Creating new temporary ELOs")
    startNanos = System.nanoTime()
    createTemporaryElos()
    val usedNanosForCreateTemporaryElos = System.nanoTime()-startNanos
    println("Storing new ELOs")
    startNanos = System.nanoTime()
    fillAndStoreDestinationElos()
    val usedNanosForFillAndStoreDestinationElos = System.nanoTime()-startNanos
    val nrOfElos = uriTranslationMap.size
    if (nrOfElos>0){
      def printTime(label:String,  nanos: Long) = {
        val millis = nanos/1e6;
        val millesPerElo = millis/nrOfElos
        printf("%s: %8.3f  %8.3f\n",label,millis,millesPerElo)
      }
      println("Time used (total and per ELO, in ms):")
      printTime("Loading         ", usedNanosForLoadElos)
      printTime("Create temp     ", usedNanosForCreateTemporaryElos)
      printTime("Fill and storing", usedNanosForFillAndStoreDestinationElos)
    }
    println("Finished copying ELOs")
  }

  private def loadElos() = {
    progressDisplay.start()
    for (eloUri <- eloUris) {
      if (!sourceUris.contains(eloUri)) {
        val versionList: Buffer[IELO] = stateModel.source.repository.retrieveELOAllVersions(eloUri)
        if (!versionList.isEmpty) {
          eloPairVersionList += createEloPairVersionList(versionList)
          progressDisplay.step()
        }
      }
    }
    progressDisplay.stop()
  }

  private def createEloPairVersionList(versionList: Buffer[IELO]) = {
    val eloPairVersionList = ArrayBuffer[EloPair]()
    for (elo <- versionList) {
      if (elo != null) {
        val eloPair = new EloPair(elo, null)
        eloPairVersionList += eloPair
        // make sure we can convert the elo to xml and back
        val eloXml = elo.getXml
        val eloFromXml = stateModel.eloFactory.createELOFromXml(eloXml)
        val checkXml = eloFromXml.getXml
        if (largestElo == null) {
          largestElo = new EloPairWithSize(eloPair, eloXml.size)
        } else if (eloXml.size > largestElo.xmlSize) {
          largestElo = new EloPairWithSize(eloPair, eloXml.size)
        }
        sourceUris+=elo.getUri
      }
    }
    eloPairVersionList
  }

  private def notEnoughMemory(): Boolean = {
    def enoughFreeMemory() = Runtime.getRuntime().freeMemory() > 4 * largestElo.xmlSize
    if (enoughFreeMemory) {
      return false
    }
    Runtime.getRuntime().gc()
    !enoughFreeMemory()
  }

  private def createTemporaryElos() = {
    progressDisplay.start()
    for (versionList <- eloPairVersionList) {
      var lastNewElo: IELO = null
      for (eloPair <- versionList) {
        val newElo = if (lastNewElo == null) {
          createNewTemporaryElo(eloPair.sourceElo)
        } else {
          createNewTemporaryVersionElo(lastNewElo)
        }
        lastNewElo = newElo
        eloPair.destinationElo = newElo
        uriTranslationMap.put(eloPair.sourceElo.getUri, newElo.getUri)
      }
      progressDisplay.step()
    }
    progressDisplay.stop()
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

  private def cloneElo(elo: IELO): IELO = {
    //    elo.clone()
    val xml = elo.getXml()
    stateModel.eloFactory.createELOFromXml(xml)
  }

  private def fillDestinationElo(eloPair: EloPair) = {
    val sourceEloXml = eloPair.sourceElo.getXml
    val destinationEloXml = replaceUris(sourceEloXml)
    eloPair.destinationElo = stateModel.eloFactory.createELOFromXml(destinationEloXml)
  }

  private def replaceUris(eloXml: String): String = {
    var xml = eloXml
    for (entry <- uriTranslationMap) {
      xml = xml.replace(entry._1.toString, entry._2.toString)
    }
    xml
  }

  private def fillAndStoreDestinationElos(): Unit = {
    progressDisplay.start()
    // remove the eloPairs from memory, when they have been processed
    while (!eloPairVersionList.isEmpty) {
      var versionList = eloPairVersionList.head
      eloPairVersionList = eloPairVersionList.tail
      while (!versionList.isEmpty) {
        val eloPair = versionList.head
        fillDestinationElo(eloPair)
        stateModel.destination.repository.updateWithMinorChange(eloPair.destinationElo)
        versionList = versionList.tail
      }
      progressDisplay.step()
    }
    progressDisplay.stop()
  }

}