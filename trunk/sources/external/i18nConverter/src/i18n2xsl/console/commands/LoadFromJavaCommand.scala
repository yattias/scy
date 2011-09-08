package i18n2xsl.console.commands
import i18n2xsl.console.PropertiesModelCommandHandler
import i18n2xsl.console.PropertiesModel
import java.io.File
import i18n2xsl.KeyValueStores
import i18n2xsl.JavaLocation
import i18n2xsl.KeyValueStoreImpl
import i18n2xsl.KeyValueStore

class LoadFromJavaCommand(override val propertiesModel: PropertiesModel) extends PropertiesModelCommandHandler(propertiesModel) {
  val commands = Seq("load [from] java")
  val description = "load properties from java properties files"

  def execute(params: Seq[String]): Unit = {
    propertiesModel.stores = loadStoresFromJava(params)
  }

  def loadStoresFromJava(params: Seq[String]): KeyValueStores = {
    val loadedStores = new KeyValueStores();
    for (location <- propertiesModel.javaLocations.locations) {
      loadedStores.addStore(loadStoreFromLocation(location))
    }
    loadedStores
  }

  private def loadStoreFromLocation(location: JavaLocation): KeyValueStore = {
    val store: KeyValueStore = new KeyValueStoreImpl()
    val languageDir = new File(propertiesModel.javaLocations.clientRoot, location.modulePath + location.languagePath)
    val javaPropFile = new File(languageDir, location.moduleName)
    if (location.javaFX) {
      propertiesModel.javaFXPropertiesConnector.loadStore(javaPropFile, store)
    } else {
      propertiesModel.javaPropertiesConnector.loadStore(javaPropFile, store)
    }
    store.name = location.name
    store
  }
}