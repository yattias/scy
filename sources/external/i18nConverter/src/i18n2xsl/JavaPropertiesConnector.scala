package i18n2xsl

import io.Source
import java.security.Key
import sun.awt.SunHints.Value
import java.io.{OutputStream, PrintWriter, File}
import java.lang.{StringBuilder, Boolean, String}

/**
 * Created by IntelliJ IDEA.
 * User: sikken
 * Date: 19-6-11
 * Time: 17:06
 * To change this template use File | Settings | File Templates.
 */

class JavaPropertiesConnector(encoding: String = "ISO-8859-1") extends FormatConnector
{
  protected def fileExtension(): String =
  {
    ".properties"
  }

  protected def writeKeyValue(key: String, value: String, out: PrintWriter): Unit =
  {
//    println(key + "=" + value)
    out.println(key + "=" + value)
  }

  protected def readKeyValue(line: String): Option[(String, String)] =
  {
    val equalsPos = line.indexOf('=')
    if (equalsPos > 0) {
      val key = line.substring(0, equalsPos);
      val value = line.substring(equalsPos + 1)
      return Some((key, value))
    }
    return None
  }

  def loadStore(file: File, store: KeyValueStore): Unit =
  {
    store.name = file.getName()
    loadPropertiesFiles(file.getParentFile, file.getName(), store)
  }

  def saveStore(store: KeyValueStore, file: File): Unit =
  {
    for (language <- store.getLanguages()) {
      val outputFile = new File(file.getParentFile(), file.getName() + "_" + language.toLowerCase() + fileExtension());
      println("writing to file: " + outputFile)
      val out = new PrintWriter(outputFile,encoding)
      for (key <- store.getSortedKeys()) {
        val value = store.getValue(key, language)
        value match {
          case Some(v: String) => writeKeyValue(key,UnicodeEscaper.escape(v) , out)
          case _ =>
        }
      }
      out.close()
    }
  }

  private def loadPropertiesFiles(dir: File, name: String, store: KeyValueStore): Unit =
  {
    if (!dir.isDirectory()){
      println("properties directory is not a directory: " + dir.getAbsolutePath)
      return
    }
    if (!dir.exists()){
      println("properties directory does not exists: " + dir.getAbsolutePath)
      return
    }
    for (file <- dir.listFiles()) {
      val fileName = file.getName();
      if (fileName.startsWith(name) && fileName.endsWith(fileExtension())) {
        val underscorePos = fileName.indexOf('_', name.length());
        if (underscorePos == name.length()) {
          val language = fileName.substring(underscorePos + 1, fileName.length() - fileExtension().length());
          loadJavaPropertiesFile(file, language, store)
        }
      }
    }
  }

  private def loadJavaPropertiesFile(file: File, language: String, store: KeyValueStore): Unit =
  {
    val properties = readPropertiesFile(file);
    for (key <- properties.keys) {
      store.addKeyValue(key, language, properties.get(key).get)
    }
  }

  protected def readPropertiesFile(file: File): KeyValueMap =
  {
    println("Reading prop in " + file.getAbsolutePath)
    val keyValueMap = new KeyValueMap();
    for (line <- Source.fromFile(file).getLines()) {
//      println("processing line: " + line)
      if (line.length() > 0 && line.charAt(0) != '#') {
        def keyValue = readKeyValue(line)
        if (keyValue != None) {
          keyValueMap.put(keyValue.get._1, UnicodeEscaper.deescape(keyValue.get._2))
        }
      }
    }
    return keyValueMap;
  }
}