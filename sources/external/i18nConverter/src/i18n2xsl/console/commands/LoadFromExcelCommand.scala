package i18n2xsl.console.commands
import i18n2xsl.console.PropertiesModelCommandHandler
import i18n2xsl.console.PropertiesModel
import java.io.File
import i18n2xsl.KeyValueStores

class LoadFromExcelCommand(override val propertiesModel: PropertiesModel) extends PropertiesModelCommandHandler(propertiesModel) {
  val commands = Seq("load [from] excel")
  val description = "load properties from excel xml file"
  override val paramDescription = "excel xml file"

  def execute(params: Seq[String]): Unit = {
    propertiesModel.stores = loadStoresFromExcel(params)
  }
  
  def loadStoresFromExcel(params: Seq[String]): KeyValueStores = {
     val file = getExcelFile(params)
     propertiesModel.excelXmlConnector.loadStores(file)
  }


  def getExcelFile(params: Seq[String]): File = {
    if (params.isEmpty) propertiesModel.excelFile else new File(propertiesModel.excelFilesDir, params.head)
  }
}