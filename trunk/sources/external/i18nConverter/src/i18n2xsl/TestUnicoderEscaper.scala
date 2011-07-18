package i18n2xsl

/**
 * Created by IntelliJ IDEA.
 * User: sikken
 * Date: 25-6-11
 * Time: 11:46
 * To change this template use File | Settings | File Templates.
 */

object TestUnicoderEscaper
{
  def main(args: Array[String]): Unit =
  {
    println("Testing TestUnicoderEscaper")
    testString("tes\\\"ting")
    testString("Αποδοχή;")
    testDescape("\\u03a3\\u03cd\\u03bd\\u03b4\\u03b5\\u03c3\\u03b7")
    testDescape("Entrez une fonction param\\u00e9tr\\u00e9e. Par exemple, a*x+b pour une droite.")
  }

  def testString(string: String) =
  {
    val escaped = UnicodeEscaper.escape(string)
    val deescaped = UnicodeEscaper.deescape(escaped)
    if (string.equals(deescaped)) {
      println("Ok: " + string + " -> " + escaped)
    }
    else {
      println("Error: " + string + " -> " + escaped + " -> " + deescaped)
    }
  }

  def testDescape(string: String) =
  {
    val deescaped = UnicodeEscaper.deescape(string)
    println("Escaped: " + string + " -> " + deescaped)
  }

}