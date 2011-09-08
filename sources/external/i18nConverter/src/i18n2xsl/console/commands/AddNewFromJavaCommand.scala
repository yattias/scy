package i18n2xsl.console.commands
import i18n2xsl.console.PropertiesModel

class AddNewFromJavaCommand(override val propertiesModel: PropertiesModel) extends LoadFromJavaCommand(propertiesModel) {
  override val commands = Seq("add new [from] java")
  override val description = "add new from java properties"

  override def execute(params: Seq[String]): Unit = {
    val javaStores = loadStoresFromJava(params);
    propertiesModel.stores.loadChangedFromOtherStores(javaStores)
  }

}