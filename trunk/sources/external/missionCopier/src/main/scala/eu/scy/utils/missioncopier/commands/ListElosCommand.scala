package eu.scy.utils.missioncopier.commands

import eu.scy.utils.missioncopier.StateModel
import eu.scy.common.scyelo.RooloServices
import roolo.search.ISearchResult
import scala.collection.JavaConversions._

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 29-11-11
 * Time: 17:08
 * To change this template use File | Settings | File Templates.
 */

class ListElosCommand (override val stateModel: StateModel) extends StateModelCommandHandler(stateModel) {
   val commands = Seq("list elos")
   val description = "list elos"
  
   def execute(params: Seq[String]): Unit = {
     listElos(stateModel.sourceRooloServices,"Source")
     listElos(stateModel.destinationRooloServices,"Destination")
   }

  private def listElos(rooloServices: RooloServices, label: String): Unit = {
    if (rooloServices == null) {
      println(label + " is not defined")
    } else {
      println(label + ": all ELOs")
      val allElosSearchResults:Seq[ISearchResult] = rooloServices.getRepository().listAllElos()
      for (searchResult <- allElosSearchResults){
        println("- " + searchResult.getUri + ", " + searchResult.getTitles + ", " + searchResult.getTechnicalFormat + ", " + searchResult.getVersion)
      }
    }
  }
}