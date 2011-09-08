package i18n2xsl.console.commands
import i18n2xsl.console.PropertiesModel

class LoadChangesFromExcelCommand(override val propertiesModel: PropertiesModel) extends LoadFromExcelCommand(propertiesModel) {
  override val commands = Seq("load changed [from] excel")
  override val description = "load changed from excel xml file"

  override def execute(params: Seq[String]): Unit = {
    val file = getExcelFile(params)
    val excelStores = propertiesModel.excelXmlConnector.loadStores(file)
    propertiesModel.stores.addNewFromOtherStores(excelStores)
  }

}