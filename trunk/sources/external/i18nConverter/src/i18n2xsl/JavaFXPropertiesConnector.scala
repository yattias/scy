package i18n2xsl

import java.io.PrintWriter

/**
 * Created by IntelliJ IDEA.
 * User: sikken
 * Date: 19-6-11
 * Time: 21:12
 * To change this template use File | Settings | File Templates.
 */

class JavaFXPropertiesConnector extends JavaPropertiesConnector {
  override protected def fileExtension(): String =
  {
    ".fxproperties"
  }

  override protected def writeKeyValue(key: String, value: String, out: PrintWriter): Unit =
  {
    out.println("\"" + key + "\"=\"" + escapeQuotes(value) + "\"")
  }

  override protected def readKeyValue(line: String): Option[(String, String)] =
  {
    val equalsPos = line.indexOf('=')
    if (equalsPos > 0) {
      val key = line.substring(0, equalsPos);
      val value = line.substring(equalsPos + 1)
      return Some((getValueOutString(key), getValueOutString(value)))
    }
    return None
  }

  private def getValueOutString(string: String) : String = {
    val firstQuotePos = string.indexOf('"')
    val lastQuotePos = string.lastIndexOf('"')
    deescapeQuotes(string.substring(firstQuotePos+1,lastQuotePos))
  }

  def escapeQuotes(string: String) : String = {
    val stringBuilder = new StringBuilder();
    for (i <- 0 until string.length()) {
      val c = string.charAt(i);
      if (c == '"') {
        stringBuilder.append("\\");
      }
      stringBuilder.append(c);
    }
    stringBuilder.toString
  }

  def deescapeQuotes(string: String) : String = {
    val stringBuilder = new StringBuilder();
    var i = 0;
    while (i<string.length()) {
      val c = string.charAt(i);
      if (c=='\\'){
        i+=1
        val c2 =string.charAt(i)
        if (c2=='"'){
          stringBuilder.append(c2);
        }
        else{
          stringBuilder.append(c);
          stringBuilder.append(c2);
        }
      }
      else{
        stringBuilder.append(c);
      }
      i+=1
    }
    stringBuilder.toString
  }
}