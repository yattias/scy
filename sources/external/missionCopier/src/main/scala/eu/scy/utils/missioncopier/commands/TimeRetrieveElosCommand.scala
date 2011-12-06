package eu.scy.utils.missioncopier.commands

import eu.scy.utils.missioncopier.StateModel
import roolo.search.ISearchResult
import java.net.URI
import java.util.Locale

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 6-12-11
 * Time: 18:38
 */

class TimeRetrieveElosCommand(override val stateModel: StateModel) extends SearchElosCommand(stateModel) {
  override val commands = Seq("time [elos]")
  override val description = "time retrieve elos"

  override protected def handleSearchResults(searchResults: Seq[ISearchResult]) = {
    Utils.printSearchResults(searchResults)
  }

  private def timeRetrieve(eloUri: URI) = {

  }
}

class TimeAction(val action: ()=>Unit){
  private val multiCount = 10;
  val singleTime = timeAction(1)
  val multiTime = timeAction(multiCount)

  private def timeAction(times: Int):Long = {
    val startNanos = System.nanoTime()
    for (i <- 0 to times){
      action
    }
    val nanosUsed = System.nanoTime()-startNanos
    nanosUsed
  }

  def printTimes() = {
    print(nanosToDisplayMillis(singleTime) + " " + nanosToDisplayMillis(multiTime/multiCount))
  }

  private def nanosToDisplayMillis(nanos:Long):String = {
    val millis: java.lang.Double = nanos/1e6
    String.format(Locale.UK, "%4.1f", millis)
  }

}