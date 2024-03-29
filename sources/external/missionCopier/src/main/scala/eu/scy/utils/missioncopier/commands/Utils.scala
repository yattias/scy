package eu.scy.utils.missioncopier.commands

import eu.scy.utils.missioncopier.StateModel
import roolo.elo.api.IELO
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds
import java.util.Locale
import collection.JavaConversions._
import eu.scy.common.scyelo.ScyElo
import roolo.search.ISearchResult

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 1-12-11
 * Time: 16:45
 */

object Utils {

  def askYesNo(stateModel: StateModel, question: String): Option[Boolean] = {
    print(question + " (y|n) ?")
    val input = stateModel.readLine().trim().toLowerCase
    input match {
      case "y" => Some(true)
      case "n" => Some(false)
      case _ => None
    }
  }

  def getEloDisplayDescription(scyElo: ScyElo, stateModel: StateModel): String = getEloDisplayDescription(scyElo.getElo, stateModel)

  def getEloDisplayDescription(elo: IELO, stateModel: StateModel): String = {
    val titleKey = stateModel.metadataTypeManager.getMetadataKey(CoreRooloMetadataKeyIds.TITLE)
    val titles: scala.collection.mutable.Map[Locale, _] = elo.getMetadata.getMetadataValueContainer(titleKey).getValuesI18n()
    val display = new StringBuilder()
    for (title <- titles) {
      if (!display.isEmpty) {
        display.append(", ")
      }
      display.append(title._2)
      display.append(" (")
      display.append(title._1.getDisplayLanguage())
      display.append(")")
    }
    display.toString()
  }

  def printSearchResults(searchResults: Seq[ISearchResult], showRelevance: Boolean = true) = {
    var i: java.lang.Integer = 1;
    for (searchResult <- searchResults) {
      printSearchResult(searchResult, i, showRelevance)
      i += 1
    }
  }

  def printSearchResult(searchResult: ISearchResult, i: java.lang.Integer = 1, showRelevance: Boolean = true) = {
    val indexFormat = "%3d: "
    val formatString = if (showRelevance) indexFormat + "%1.2f, " else indexFormat
    val index = String.format(Locale.UK, formatString, i, searchResult.getRelevance().asInstanceOf[java.lang.Double])
    println(index + searchResult.getTitles + ", " + searchResult.getTechnicalFormat + ", " + searchResult.getVersion)

  }
}