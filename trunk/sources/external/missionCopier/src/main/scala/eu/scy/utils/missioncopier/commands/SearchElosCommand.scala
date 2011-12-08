package eu.scy.utils.missioncopier.commands

import eu.scy.utils.missioncopier.{RepositoryDefinition, StateModel}
import scala.collection.JavaConversions._
import roolo.search.{Query, MetadataQueryComponent, ISearchResult}

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 2-12-11
 * Time: 15:22
 */

class SearchElosCommand(override val stateModel: StateModel) extends StateModelCommandHandler(stateModel) {
  val commands = Seq("search [elos]")
  val description = "search elos"
  override val paramDescription = "query"
  protected val maxNrOfSearchResults = 50

  def execute(params: Seq[String]): Unit = {
    if (params.isEmpty){
      println("Please specify the search query")
      return
    }
    searchElos(stateModel.source, "Source", params)
    searchElos(stateModel.destination, "Destination", params)
  }

  private def searchElos(repositoryDefinition: RepositoryDefinition, label: String, params: Seq[String]): Unit = {
    if (repositoryDefinition == null) {
      println(label + " is not defined")
    } else {
      val searchText = params.mkString(" ")
      println(label + ": " + searchText)
      val queryComponent = new MetadataQueryComponent("contents", searchText)
      val query = new Query(queryComponent)
      query.setMaxResults(maxNrOfSearchResults)
      val searchResults: Seq[ISearchResult] = repositoryDefinition.repository.search(query)
      handleSearchResults(searchResults, repositoryDefinition)
    }
  }

  protected def handleSearchResults(searchResults: Seq[ISearchResult], repositoryDefinition: RepositoryDefinition) = {
    Utils.printSearchResults(searchResults)
  }
}