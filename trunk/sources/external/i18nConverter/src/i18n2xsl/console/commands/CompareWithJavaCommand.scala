package i18n2xsl.console.commands
import i18n2xsl.KeyValueStores
import i18n2xsl.console.PropertiesModel

class CompareWithJavaCommand(override val propertiesModel: PropertiesModel) extends LoadFromJavaCommand(propertiesModel) {
  override val commands = Seq("compare [with] java")
  override val description = "compare properties with java properties"

  override def execute(params: Seq[String]): Unit = {
    val javaStores = loadStoresFromJava(params);
    println(propertiesModel.stores.compareWith(javaStores))
  }

}