package i18n2xsl.console.commands
import i18n2xsl.console.PropertiesModelCommandHandler
import i18n2xsl.console.PropertiesModel
import java.io.File

class SaveToExcelCommand(override val propertiesModel: PropertiesModel) extends LoadFromExcelCommand(propertiesModel) {
  override val commands = Seq("save [to] excel")
  override val description = "save properties to excel xml file"

  override def execute(params: Seq[String]): Unit = {
    val file = getExcelFile(params)
    propertiesModel.excelXmlConnector.saveStores(propertiesModel.stores, file)
  }
}