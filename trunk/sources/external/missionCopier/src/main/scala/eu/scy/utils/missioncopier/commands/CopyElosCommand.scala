package eu.scy.utils.missioncopier.commands

import scala.collection.JavaConversions._
import roolo.search.ISearchResult
import eu.scy.utils.missioncopier.{EloVersionCopier, EloCopier, StateModel}


/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 30-11-11
 * Time: 14:25
 * To change this template use File | Settings | File Templates.
 */

class CopyElosCommand  (override val stateModel: StateModel) extends StateModelCommandHandler(stateModel) {
   val commands = Seq("copy elos")
   val description = "copy elos"

   def execute(params: Seq[String]): Unit = {
     if (stateModel.source == null) {
        println("cannot copy elos if the source repository is not defined")
        return
     }
     if (stateModel.destination == null) {
        println("cannot copy elos if the destination repository is not defined")
        return
     }
     copyElos()
   }

  private def copyElos() = {
    val eloVersionCopier = new EloVersionCopier(stateModel)
    val allElosSearchResults:Seq[ISearchResult] = stateModel.source.repository.listAllElos()
    eloVersionCopier.eloUris = for (searchResult <- allElosSearchResults) yield searchResult.getUri
    eloVersionCopier.copyElos()
  }
}