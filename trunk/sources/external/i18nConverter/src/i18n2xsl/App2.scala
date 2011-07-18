package i18n2xsl

import java.io.File
import scala.Predef._

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 30-6-11
 * Time: 15:10
 * To change this template use File | Settings | File Templates.
 */

object App2 {
  private val clientRoot = new File("d:\\Projects\\scy\\code\\scy-trunk\\sources\\modules\\client\\");
  private val javaLanguagePath = "/src/main/java/languages/"
  private val resourcesLanguagePath = "/src/main/resources/languages/"
  private val excelFilesDir = new File("excelFiles")
  private val javaPropertiesConnector = new JavaPropertiesConnector();
  private val javaFXPropertiesConnector = new JavaFXPropertiesConnector();
  private val excelTextConnector = new ExcelTextConnector();
  private val excelTextConnectorFX = new ExcelTextConnector("FX");
  private val excelXmlConnector = new ExcelXmlConnector();
  private val excelXmlConnectorFX = new ExcelXmlConnector("FX");

  case class Location(val name: String, val modulePath: String, val moduleName: String, val languagePath: String, val javaFX: Boolean = false)

  val locations = List(
    new Location("scydesktop", "desktop/scy-desktop", "scydesktop", javaLanguagePath, false),
    new Location("scydesktopFX", "desktop/scy-desktop", "scydesktop", javaLanguagePath, true),
    new Location("copex", "tools/copex", "copex", resourcesLanguagePath, false),
    new Location("fitex", "tools/dataprocesstool", "fitex", resourcesLanguagePath, false),
    new Location("copexFX", "tools/fx-copex", "fxcopex", javaLanguagePath, false),
    new Location("fx-socialtaggingtool", "tools/fx-socialtaggingtool", "fxsocialtaggingtool", javaLanguagePath, true),
    new Location("Health passport", "tools/resultBinder", "resultbinder", resourcesLanguagePath, false),
    new Location("scydynamics", "tools/scydynamics", "scydynamics", javaLanguagePath, false),
    new Location("scymapper", "tools/scymapper", "scymapper", resourcesLanguagePath, false),
    new Location("simulatorFX", "tools/fx-simulator", "fxsimulator", javaLanguagePath, true),
    new Location("scysimulator", "tools/scysimulator", "scysimulator", javaLanguagePath, false),
    new Location("flyingsaucer", "tools/fx-flying-saucer", "fxflyingsaucer", javaLanguagePath, false),
    new Location("interview", "tools/fx-interviewtool", "interviewtool", javaLanguagePath, false),
    new Location("interviewFX", "tools/fx-interviewtool", "interviewtool", javaLanguagePath, true),
    new Location("richtexteditor", "client-common/rich-text-editor", "richtexteditor", resourcesLanguagePath, false)
  )

  def main(args: Array[String]): Unit = {
    println("Welcome to java(FX) properties <-> excel convertor")
//    createExcelFile(locations, new File(excelFilesDir, "properties.xml"))
//    test1()
      propertyFilesToExcel()   
//    excelToPropertyFiles()
  }

  private def test1()= {
    val stores = loadStoresFromPropertyFiles(locations)
    val excelFile = new File(excelFilesDir, "properties.xml")
    excelXmlConnector.saveStores(stores, excelFile)
    val stores2 = excelXmlConnector.loadStores(excelFile)
    excelXmlConnector.saveStores(stores, new File(excelFilesDir, "properties2.xml"))
  }

  private def createExcelFile(locations: List[Location], file: File) = {
    val stores =
      for (location <- locations) yield {
        val store = loadStoreFromLocation(location)
        store.name = location.name
        store
      }
    excelXmlConnector.saveStores(stores, file)
  }

  private def propertyFilesToExcel() = {
    val stores = loadStoresFromPropertyFiles(locations)
    val excelFile = new File(excelFilesDir, "properties.xml")
    excelXmlConnector.saveStores(stores, excelFile)
  }

  private def excelToPropertyFiles() = {
    val excelFile = new File(excelFilesDir, "properties.xml")
    val stores = excelXmlConnector.loadStoresOld(excelFile)
    saveStoresToLocations(stores,locations)
  }

  private def loadStoresFromPropertyFiles(locations: List[Location]): List[KeyValueStore] = {
    for (location <- locations) yield {
      val store = loadStoreFromLocation(location)
      store.name = location.name
      store
    }
  }

  private def loadStoreFromLocation(location: Location): KeyValueStore = {
    val store: KeyValueStore = new KeyValueStoreImpl()
    store.name = location.name
    val languageDir = new File(clientRoot, location.modulePath + location.languagePath)
    val javaPropFile = new File(languageDir, location.moduleName)
    if (location.javaFX) {
      javaFXPropertiesConnector.loadStore(javaPropFile, store)
    } else {
      javaPropertiesConnector.loadStore(javaPropFile, store)
    }
    store
  }

  private def saveStoresToLocations(stores: Seq[KeyValueStore], locations: List[Location]) = {
    for (store <- stores){
      val location = findLocation(store.name, locations)
      val languageDir = new File(clientRoot, location.modulePath + location.languagePath)
      val javaPropFile = new File(languageDir, location.moduleName)
      if (location.javaFX) {
        javaFXPropertiesConnector.saveStore(store, javaPropFile)
      } else {
        javaPropertiesConnector.saveStore(store, javaPropFile)
      }
    }
  }

  private def findLocation(name: String, locations: List[Location]): Location = {
    for (location <- locations){
      if (location.name==name){
        return location
      }
    }
    return null
  }
}