package i18n2xsl.console.commands
import i18n2xsl.console.PropertiesModel

class RemoveSameStoresAsJavaCommand(override val propertiesModel: PropertiesModel) extends LoadFromJavaCommand(propertiesModel) {
  override val commands = Seq("remove same [stores] [as] java")
  override val description = "remove same stores as in java properties"

  override def execute(params: Seq[String]): Unit = {
    val javaStores = loadStoresFromJava(params);
    propertiesModel.stores.removeSameStores(javaStores)
  }

}