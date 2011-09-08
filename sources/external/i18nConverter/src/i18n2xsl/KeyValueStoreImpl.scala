package i18n2xsl

import java.util.Properties
import java.io.{ FileReader, File }
import collection.immutable.HashMap
import scala.Array

/**
 * Created by IntelliJ IDEA.
 * User: sikken
 * Date: 13-6-11
 * Time: 15:37
 * To change this template use File | Settings | File Templates.
 */

class KeyValueStoreImpl extends KeyValueStore {
  //  val name: String = ""

  private var languages: List[String] = List[String]();

  private var keys = Set[String]();

  private var propertiesMap = HashMap[String, KeyValueMap]()

  override def toString(): String = {
    "languages=" + languages
    ",keys=" + keys
  }

  override def equals(obj: Any): Boolean = {
    if (obj.isInstanceOf[KeyValueStoreImpl]) {
      val otherStore = obj.asInstanceOf[KeyValueStoreImpl]
      if (!getSortedLanguages().sameElements(otherStore.getSortedLanguages())) {
        return false
      }
      if (!getSortedKeys().sameElements(otherStore.getSortedKeys())) {
        return false
      }
      for (lang <- getSortedLanguages()) {
        val myKeyValueMap = getKeyValueMap(lang)
        val otherKeyValueMap = otherStore.getKeyValueMap(lang)
        if (myKeyValueMap != otherKeyValueMap) {
          return false
        }
      }
      return true
    }
    return false
  }

  override def addKeyValue(key: String, language: String, value: String) = {
    if (!keys.contains(key)) {
      keys += key
    }
    getKeyValueMap(language).put(key, value)
    //    propertiesMap.get(language).get.put(key, value)
  }

  override def getValue(key: String, language: String): Option[String] =
    {
      val result = propertiesMap.get(language).get.get(key)
      if (result != None) {
        return result
      }
      val resultEn = propertiesMap.get("en").get.get(key)
      if (resultEn != None) {
        return resultEn
      }
      return None
    }

  override def getKeys() = keys.toList

  override def getLanguages() = languages

  override def getKeyValueMap(language: String): KeyValueMap =
    {
      val keyValueMapOption = propertiesMap.get(language);
      if (keyValueMapOption != None) {
        return keyValueMapOption.get
      } else {
        languages = language :: languages
        val keyValueMap = new KeyValueMap()
        propertiesMap += language -> keyValueMap
        return keyValueMap
      }
    }

  override def compareWith(store: KeyValueStore): String = {
    val label1 = "loaded"
    val label2 = "other"
    val result = new StringBuilder
    result.append("languages:")
    result.append(compare(label1, getSortedLanguages, label2, store.getSortedLanguages))
    result.append("keys     :")
    result.append(compare(label1, getSortedKeys, label2, store.getSortedKeys))
    var identicalLanguages = Seq[String]()
    for (lang <- getSortedLanguages) {
      if (getKeyValueMap(lang) == store.getKeyValueMap(lang)) {
        identicalLanguages :+= lang
      }
    }
    result.append("values   :")
    result.append(seqToLine("\nidentical", identicalLanguages))
    for (lang <- getSortedLanguages if (!identicalLanguages.contains(lang))) {
      result.append("language: ")
      result.append(lang)
      result.append("\n")
      result.append(showDifferencesIncompareKeyValueMaps(getKeyValueMap(lang), store.getKeyValueMap(lang)))
      result.append("\n")
    }
    result.toString
  }

  private def compare(label1: String, list1: List[String], label2: String, list2: List[String]): String = {
    val result = new StringBuilder
    val duplicates = for { s <- list1 if list2.contains(s) } yield s
    val equals = list1.size == duplicates.size
    if (equals) {
      result.append(" identical\n")
    } else {
      result.append("\n")
      result.append(seqToLine(" duplicate", duplicates))
      result.append(seqToLine(" only in " + label1, for { s <- list1 if !duplicates.contains(s) } yield s))
      result.append(seqToLine(" only in " + label2, for { s <- list2 if !duplicates.contains(s) } yield s))
    }
    result.toString
  }

  private def seqToLine(label: String, seq: Seq[String]): String = {
    label + ": " + Utils.getSeqDisplay(seq) + "\n"
  }

  private def showDifferencesIncompareKeyValueMaps(keyValueMap1: KeyValueMap, keyValueMap2: KeyValueMap): String = {
    val result = new StringBuilder
    keyValueMap1.foreach {
      case (key1, value1) => {
        val value2 = keyValueMap2.get(key1)
        keyValueMap2.get(key1) match {
          case Some(value2) if (value2 != value1) => result.append(key1 + ": " + value1 + " != " + value2 + "\n")
          case None => result.append(key1 + ": " + value1 + " != UNDEFINED\n")
          case _ =>
        }
      }
    }
    result.toString
  }

  override def addNewFromOtherStore(otherStore: KeyValueStore): Unit = {
    for (lang <- getSortedLanguages) {
      val myKeyValueMap = getKeyValueMap(lang)
      val otherKeyValueMap = otherStore.getKeyValueMap(lang)
      addNewFromOtherKeyValueMap(lang, myKeyValueMap, otherKeyValueMap)
    }
  }

  private def addNewFromOtherKeyValueMap(language: String, myKeyValueMap: KeyValueMap, otherKeyValueMap: KeyValueMap): Unit = {
    otherKeyValueMap.foreach {
      case (key, value) => {
        if (!myKeyValueMap.contains(key)) {
          addKeyValue(key, language, value)
          println("Added key '" + key + "', language '" + language + "' and value '" + value + "'")
        }
      }
    }
  }

  override def loadChangedFromOtherStore(otherStore: KeyValueStore): Unit = {
    for (lang <- getSortedLanguages) {
      val myKeyValueMap = getKeyValueMap(lang)
      val otherKeyValueMap = otherStore.getKeyValueMap(lang)
      loadChangedFromOtherKeyValueMap(lang, myKeyValueMap, otherKeyValueMap)
    }
  }

  private def loadChangedFromOtherKeyValueMap(language: String, myKeyValueMap: KeyValueMap, otherKeyValueMap: KeyValueMap): Unit = {
    myKeyValueMap.foreach {
      case (key, value) => {
        otherKeyValueMap.get(key) match {
          case Some(otherValue) if (value != otherValue) =>
            addKeyValue(key, language, value)
            println("Loaded changed value for key '" + key + "', language '" + language + "' and value '" + value + "'")
          case _ =>
        }
      }
    }
  }

}