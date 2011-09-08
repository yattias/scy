package i18n2xsl.console.commands
import i18n2xsl.console.PropertiesModelCommandHandler
import i18n2xsl.console.PropertiesModel
import i18n2xsl.Utils

class KeepStoresCommand(override val propertiesModel: PropertiesModel) extends PropertiesModelCommandHandler(propertiesModel) {
  val commands = Seq("keep [stores]")
  val description = "keep stores"
  override val paramDescription = "list of store names"

  def execute(params: Seq[String]): Unit = {
    val notFoundNames = propertiesModel.stores.keepStores(params)
    if (!notFoundNames.isEmpty) {
      println("Could not find stores: " + Utils.getSeqDisplay(notFoundNames))
    }
  }
}