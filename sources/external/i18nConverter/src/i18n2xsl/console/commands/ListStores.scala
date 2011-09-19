package i18n2xsl.console.commands
import i18n2xsl.console.CommandHandler
import i18n2xsl.console.PropertiesModelCommandHandler
import i18n2xsl.console.PropertiesModel
import i18n2xsl.KeyValueStore

class ListStores(override val propertiesModel: PropertiesModel) extends PropertiesModelCommandHandler(propertiesModel) {
  override val commands = Seq("list [stores]")
  override val description = "list stores"

  override def execute(params: Seq[String]): Unit = {
    var i = 1;
    for (store <- propertiesModel.stores.getStores()) {
      println(i + ": " + getStoreSummary(store))
      i += 1
    }
  }
  
  private def getStoreSummary(store: KeyValueStore): String =
    {
      val stringBuilder = new StringBuilder
      stringBuilder.append(store.name)
      stringBuilder.append(" in ")
      var firstLang = true
      for (lang <- store.getSortedLanguages()) {
        if (!firstLang) {
          stringBuilder.append(", ")
        }
        stringBuilder.append(lang)
        firstLang = false
      }
      stringBuilder.append(" with ")
      stringBuilder.append(store.getKeys().size)
      stringBuilder.append(" keys")
      stringBuilder.toString()
    }

}