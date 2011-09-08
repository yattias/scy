package i18n2xsl.console
import i18n2xsl.JavaLocations
import i18n2xsl.KeyValueStores
import i18n2xsl.ExcelXmlConnector
import i18n2xsl.JavaPropertiesConnector
import i18n2xsl.JavaFXPropertiesConnector
import i18n2xsl.ExcelTextConnector
import java.io.File

class PropertiesModel {
  val javaLocations = new JavaLocations()
  val excelFilesDir = new File("excelFiles")
  var excelFileName = "properties.xml"
  var excelFile = new File(excelFilesDir, excelFileName)
  val javaPropertiesConnector = new JavaPropertiesConnector()
  val javaFXPropertiesConnector = new JavaFXPropertiesConnector()
  val excelTextConnector = new ExcelTextConnector()
  val excelTextConnectorFX = new ExcelTextConnector("FX")
  val excelXmlConnector = new ExcelXmlConnector()
  val excelXmlConnectorFX = new ExcelXmlConnector("FX")
  var stores = new KeyValueStores()
  var exit = false

}