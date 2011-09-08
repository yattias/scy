package i18n2xsl.console.commands
import i18n2xsl.console.PropertiesModel

class RemoveSameStoresAsExcelCommand(override val propertiesModel: PropertiesModel) extends LoadFromExcelCommand(propertiesModel) {
  override val commands = Seq("remove same [stores] [as] excel")
  override val description = "remove same stores as in excel xml file"

  override def execute(params: Seq[String]): Unit = {
    val excelStores = loadStoresFromExcel(params)
    propertiesModel.stores.removeSameStores(excelStores)
  }

}