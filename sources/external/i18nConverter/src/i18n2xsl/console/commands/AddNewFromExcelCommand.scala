package i18n2xsl.console.commands
import i18n2xsl.console.PropertiesModel

class AddNewFromExcelCommand(override val propertiesModel: PropertiesModel) extends LoadFromExcelCommand(propertiesModel) {
  override val commands = Seq("add new [from] excel")
  override val description = "add new from excel xml file"

  override def execute(params: Seq[String]): Unit = {
    val excelStores = loadStoresFromExcel(params)
    propertiesModel.stores.loadChangedFromOtherStores(excelStores)
  }

}