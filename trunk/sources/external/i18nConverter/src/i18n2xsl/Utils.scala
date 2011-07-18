package i18n2xsl

object Utils {
  def getSeqDisplay(names: Seq[String]): String =
  {
    val stringBuilder = new StringBuilder()
    var firstName = true
    for (name <- names) {
      if (!firstName) {
        stringBuilder.append(", ")
      }
      stringBuilder.append(name)
      firstName = false
    }
    stringBuilder.toString
  }


}