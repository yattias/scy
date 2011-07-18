package i18n2xsl

/**
 * Created by IntelliJ IDEA.
 * User: sikken
 * Date: 13-6-11
 * Time: 15:35
 * To change this template use File | Settings | File Templates.
 */

trait KeyValueStore
{
  var name: String = ""
  
  def getLanguages(): List[String]

  def getSortedLanguages(): List[String] = {
    getLanguages().sortWith((s1,s2) => s1.compareToIgnoreCase(s2)<0)
  }

  def getKeys(): List[String]

  def getSortedKeys(): List[String] = {
    getKeys().sortWith((s1,s2) => s1.compareToIgnoreCase(s2)<0)
  }

  def getValue(key: String, lang: String) : String

  def addKeyValue(key: String, lang: String, value: String) : Unit

  def compareWith(store: KeyValueStore): String
  
  def getKeyValueMap(language: String): KeyValueMap
  
  def addNewFromOtherStore(otherStore: KeyValueStore): Unit
  
  def loadChangedFromOtherStore(otherStore: KeyValueStore): Unit
  
}