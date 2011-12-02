package eu.scy.utils.missioncopier.commands

import eu.scy.utils.missioncopier.{RepositoryDefinition, StateModel}
import scala.collection.JavaConversions._
import eu.scy.common.mission.MissionEloType
import roolo.search.{Query, SearchOperation, MetadataQueryComponent, ISearchResult}

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 2-12-11
 * Time: 15:22
 */

class SearchSourceElosCommand  (override val stateModel: StateModel) extends StateModelCommandHandler(stateModel) {
   val commands = Seq("search [elos]")
   val description = "search elos"

   def execute(params: Seq[String]): Unit = {
     searchElos(stateModel.source,"Source",params)
     searchElos(stateModel.destination,"Destination",params)
   }

  private def searchElos(repositoryDefinition: RepositoryDefinition, label: String, params: Seq[String]): Unit = {
    if (repositoryDefinition == null) {
      println(label + " is not defined")
    } else {
      val searchText = params.foldLeft("")((a,b) => a + " " + b)
      println(label + ": " + searchText)
      val queryComponent = new MetadataQueryComponent("contents",searchText);
      val query = new Query(queryComponent);
      val searchResults:Seq[ISearchResult] = repositoryDefinition.repository.search(query)
      Utils.printSearchResults(searchResults)
    }
  }
}