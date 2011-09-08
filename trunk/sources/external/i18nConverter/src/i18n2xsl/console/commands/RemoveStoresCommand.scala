package i18n2xsl.console.commands
import i18n2xsl.console.PropertiesModelCommandHandler
import i18n2xsl.console.PropertiesModel
import i18n2xsl.Utils

class RemoveStoresCommand(override val propertiesModel: PropertiesModel) extends PropertiesModelCommandHandler(propertiesModel) {
  val commands = Seq("remove [stores]")
  val description = "remove stores"
  override val paramDescription = "list of store names"

  def execute(params: Seq[String]): Unit = {
    if (params.isEmpty) {
      propertiesModel.stores.clear()
    } else {
      val notFoundNames = propertiesModel.stores.removeStores(params)
      if (!notFoundNames.isEmpty) {
        println("Could not find stores: " + Utils.getSeqDisplay(notFoundNames))
      }
    }
  }
}