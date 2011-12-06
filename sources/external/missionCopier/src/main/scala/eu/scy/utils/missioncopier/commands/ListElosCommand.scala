package eu.scy.utils.missioncopier.commands

import roolo.search.ISearchResult
import eu.scy.utils.missioncopier.{RepositoryDefinition, StateModel}
import scala.collection.JavaConversions._

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 29-11-11
 * Time: 17:08
 */

class ListElosCommand (override val stateModel: StateModel) extends StateModelCommandHandler(stateModel) {
   val commands = Seq("list elos")
   val description = "list elos"
  
   def execute(params: Seq[String]): Unit = {
     listElos(stateModel.source,"Source")
     listElos(stateModel.destination,"Destination")
   }

  private def listElos(repositoryDefinition: RepositoryDefinition, label: String): Unit = {
    if (repositoryDefinition == null) {
      println(label + " is not defined")
    } else {
      println(label + ": all ELOs")
      val allElosSearchResults:Seq[ISearchResult] = repositoryDefinition.repository.listAllElos()
      Utils.printSearchResults(allElosSearchResults,false)
    }
  }
}