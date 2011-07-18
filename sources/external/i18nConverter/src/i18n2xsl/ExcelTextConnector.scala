package i18n2xsl

import java.io.{PrintWriter, File}
import io.Source
import java.util.StringTokenizer
import collection.mutable.{ArrayBuffer, ArraySeq}

/**
 * Created by IntelliJ IDEA.
 * User: sikken
 * Date: 19-6-11
 * Time: 17:43
 * To change this template use File | Settings | File Templates.
 */

class ExcelTextConnector(nameAppend : String="") extends FormatConnector
{
  val excelExtension = ".txt"
  val deviderString = "\t"

  def loadStore(file: File, store: KeyValueStore): Unit =
  {
    val inputFile = getExcelFile(file);
    println("Reading excel file: " + inputFile.getAbsolutePath)
    store.name = file.getName()
    var firstLine = true
    val languages = ArrayBuffer[String]();
    for (line <- Source.fromFile(inputFile).getLines()) {
      val stringTokenizer = new StringTokenizer(line, deviderString)
      val key = stringTokenizer.nextToken()
      var index = 0;
      while (stringTokenizer.hasMoreTokens()) {
        val token = stringTokenizer.nextToken()
        if (firstLine) {
          languages += token
        }
        else {
          store.addKeyValue(key, languages(index), token)
        }
        index += 1
      }
      firstLine = false
    }

  }

  def saveStore(store: KeyValueStore, file: File): Unit =
  {
    if (store.getKeys().size==0){
      return
    }
    val languages = store.getSortedLanguages()
    val outputFile = getExcelFile(file);
    val out = new PrintWriter(outputFile);
    out.print("key")
    for (lang <- languages) {
      out.print(deviderString + lang)
    }
    out.println();
    for (key <- store.getSortedKeys()) {
      out.print(key)
      for (lang <- languages) {
        out.print(deviderString + store.getValue(key, lang))
      }
      out.println();
    }
    out.close()
  }

  private def getExcelFile(file: File): File =
  {
    if (!file.getName.endsWith(excelExtension)) {
      return new File(file.getParentFile, file.getName() + nameAppend + excelExtension)
    }
    return file
  }

}