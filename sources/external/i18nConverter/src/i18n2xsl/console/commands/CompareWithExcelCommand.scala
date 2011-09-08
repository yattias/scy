package i18n2xsl.console.commands
import i18n2xsl.console.PropertiesModel
import i18n2xsl.KeyValueStores

class CompareWithExcelCommand(override val propertiesModel: PropertiesModel) extends LoadFromExcelCommand(propertiesModel) {
  override val commands = Seq("compare [with] excel")
  override val description = "compare properties with excel xml file"

  override def execute(params: Seq[String]): Unit = {
    val excelStores = loadStoresFromExcel(params)
    println(propertiesModel.stores.compareWith(excelStores))
  }

}