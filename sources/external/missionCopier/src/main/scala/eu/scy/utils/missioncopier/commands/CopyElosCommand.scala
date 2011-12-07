package eu.scy.utils.missioncopier.commands

import scala.collection.JavaConversions._
import eu.scy.utils.missioncopier.StateModel
import eu.scy.utils.missioncopier.copying.{SaverEloVersionCopier, EloVersionCopier}
import roolo.search.{Query, MetadataQueryComponent, IQuery, ISearchResult}


/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 30-11-11
 * Time: 14:25
 * To change this template use File | Settings | File Templates.
 */

class CopyElosCommand(override val stateModel: StateModel) extends StateModelCommandHandler(stateModel) {
  val commands = Seq("copy elos")
  val description = "copy elos"
  override val paramDescription = "[query]"

  def execute(params: Seq[String]): Unit = {
    if (stateModel.source == null) {
      println("cannot copy elos if the source repository is not defined")
      return
    }
    if (stateModel.destination == null) {
      println("cannot copy elos if the destination repository is not defined")
      return
    }
    copyElos(getEloSearchResults(params))
  }

  private def copyElos(eloSearchResults: Seq[ISearchResult]): Unit = {
    if (!eloSearchResults.isEmpty) {
      val eloUris = for (searchResult <- eloSearchResults) yield searchResult.getUri
      val eloVersionCopier: EloVersionCopier = new SaverEloVersionCopier(stateModel)
      eloVersionCopier.copyElos(eloUris)
    }
  }

  private def getEloSearchResults(params: Seq[String]): Seq[ISearchResult] = {
    var eloSearchResults = Seq[ISearchResult]()
    if (params.isEmpty) {
      Utils.askYesNo(stateModel, "Copy all ELOs") match {
        case Some(true) =>
          eloSearchResults = stateModel.source.repository.listAllElos()
        case _ =>
      }
    } else {
      val searchText = params.mkString(" ")
      val queryComponent = new MetadataQueryComponent("contents", searchText);
      val query = new Query(queryComponent);
      query.setMaxResults(Integer.MAX_VALUE)
      println("Searching for ELOs with: " + searchText)
      val searchResults = stateModel.source.repository.search(query)
      if (searchResults.isEmpty) {
        println("No ELOs found")
      } else {
        Utils.printSearchResults(searchResults)
        Utils.askYesNo(stateModel, "Copy these ELOs") match {
          case Some(true) =>
            eloSearchResults = searchResults
          case _ =>
        }
      }
    }
    eloSearchResults
  }
}