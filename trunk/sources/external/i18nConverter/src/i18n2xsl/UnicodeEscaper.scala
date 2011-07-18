package i18n2xsl

import java.lang.IllegalArgumentException

/**
 * Created by IntelliJ IDEA.
 * User: sikken
 * Date: 25-6-11
 * Time: 10:47
 * To change this template use File | Settings | File Templates.
 */

object UnicodeEscaper
{
  val hexChar = Array('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F');

  def escape(string: String): String =
  {
    val stringBuilder = new StringBuilder();
    for (i <- 0 until string.length()) {
      val c = string.charAt(i);
      if ((c >> 7) > 0) {
        stringBuilder.append("\\u");
        stringBuilder.append(hexChar((c >> 12) & 0xF)); // append the hex character for the left-most 4-bits
        stringBuilder.append(hexChar((c >> 8) & 0xF)); // hex for the second group of 4-bits from the left
        stringBuilder.append(hexChar((c >> 4) & 0xF)); // hex for the third group
        stringBuilder.append(hexChar(c & 0xF)); // hex for the last group, e.g., the right most 4-bits
      }
      else {
        stringBuilder.append(c);
      }
    }
    stringBuilder.toString
  }

  def deescape(string: String): String =
  {
    val stringBuilder = new StringBuilder();
    var i = 0;
    while (i<string.length()) {
      val c = string.charAt(i);
      if (c=='\\'){
        i+=1
        val c2 =string.charAt(i)
        if (c2=='u'){
          i+=1
          if (i<(string.length()+4)){
            val uniString = string.substring(i,i+4)
            val uniChar = Integer.valueOf(uniString,16)
            stringBuilder.append(uniChar.toChar)
            i+=4
          }
          else{
            throw new IllegalArgumentException("not enough charaters for unicode value")
          }
        }
        else{
          stringBuilder.append(c);
          stringBuilder.append(c2);
          i+=1
        }
      }
      else{
        stringBuilder.append(c);
        i+=1
      }
    }
    stringBuilder.toString
  }

}