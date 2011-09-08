package i18n2xsl.console.commands
import i18n2xsl.console.PropertiesModel
import java.io.File

class SaveToJavaCommand(override val propertiesModel: PropertiesModel) extends LoadFromExcelCommand(propertiesModel) {
  override val commands = Seq("save [to] java")
  override val description = "save properties to java properties files"

  override def execute(params: Seq[String]): Unit = {
    for (store <- propertiesModel.stores.getStores()) {
      val location = propertiesModel.javaLocations.findJavaLocation(store.name)
      val languageDir = new File(propertiesModel.javaLocations.clientRoot, location.modulePath + location.languagePath)
      val javaPropFile = new File(languageDir, location.moduleName)
      if (location.javaFX) {
        propertiesModel.javaFXPropertiesConnector.saveStore(store, javaPropFile)
      } else {
        propertiesModel.javaPropertiesConnector.saveStore(store, javaPropFile)
      }
    }
  }
}