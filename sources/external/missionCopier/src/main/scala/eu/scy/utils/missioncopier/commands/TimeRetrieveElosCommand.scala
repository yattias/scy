package eu.scy.utils.missioncopier.commands

import roolo.search.ISearchResult
import java.net.URI
import java.util.Locale
import eu.scy.utils.missioncopier.{RepositoryDefinition, StateModel}

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 6-12-11
 * Time: 18:38
 */

class TimeRetrieveElosCommand(override val stateModel: StateModel) extends SearchElosCommand(stateModel) {
  override val commands = Seq("time [elos]")
  override val description = "time retrieve elos"
  override protected val maxNrOfSearchResults = 10

  override protected def handleSearchResults(searchResults: Seq[ISearchResult], repositoryDefinition: RepositoryDefinition) = {
    Utils.printSearchResults(searchResults)
    var i = 1;
    for (searchResult <- searchResults){
      timeRetrieve(i, searchResult.getUri, repositoryDefinition)
      i+=1
    }
  }

  private def timeRetrieve(i:Int, eloUri: URI, repositoryDefinition: RepositoryDefinition) = {
    def retrieveElo():Unit = repositoryDefinition.repository.retrieveELO(eloUri)
    val retrieveEloAction = new TimeAction(retrieveElo)
    def retrieveEloAllVersions():Unit = repositoryDefinition.repository.retrieveELOAllVersions(eloUri)
    val retrieveEloAllVersionsAction = new TimeAction(retrieveEloAllVersions)
    val elo = repositoryDefinition.repository.retrieveELO(eloUri)
    val xml = elo.getXml()
    val metadata = elo.getMetadata()
    val metadataXml = metadata.getXml()
    def convertToFromXml():Unit = {
      val xml = elo.getXml()
      val copyElo = stateModel.eloFactory.createELOFromXml(xml)
    }
    val convertToFromXmlAction = new TimeAction(convertToFromXml)
    def convertToXml():Unit = elo.getXml()
    val convertToXmlAction = new TimeAction(convertToXml)
    def convertFromXml():Unit = stateModel.eloFactory.createELOFromXml(xml)
    val convertFromXmlAction = new TimeAction(convertFromXml)
    def convertMetadataToXml():Unit = metadata.getXml()
    val convertMetadataToXmlAction = new TimeAction(convertMetadataToXml)
    def convertMetadataFromXml():Unit = stateModel.eloFactory.createMetadataFromXml(metadataXml)
    val convertMetadataFromXmlAction = new TimeAction(convertMetadataFromXml)
    printf("%3d: %s %s %s %s %s %s\n",i,retrieveEloAction.getDisplayTimes(),retrieveEloAllVersionsAction.getDisplayTimes(),
      convertToXmlAction.getDisplayTimes(),convertFromXmlAction.getDisplayTimes(),
      convertMetadataToXmlAction.getDisplayTimes(),convertMetadataFromXmlAction.getDisplayTimes())
  }
}

class TimeAction(val action: ()=>Unit){
  private val multiCount = 10;
  val singleTime = timeAction(1)
  val multiTime = timeAction(multiCount)

  private def timeAction(times: Int):Long = {
//    println("timeAction(" + times + ")")
    val startNanos = System.nanoTime()
    for (i <- 0 until times){
      action()
    }
    val nanosUsed = System.nanoTime()-startNanos
    nanosUsed/times
  }

  def getDisplayTimes():String = {
    nanosToDisplayMillis(singleTime) + " " + nanosToDisplayMillis(multiTime)
  }

  private def nanosToDisplayMillis(nanos:Long):String = {
    val millis: java.lang.Double = nanos/1e6
    String.format(Locale.UK, "%8.3f", millis)
  }

}