package i18n2xsl

import java.io.File
import java.lang.StringBuilder

class ConsoleControl {
   private val name = "java(FX) properties files <-> excel converter"
   private val javaLocations = new JavaLocations()
   private val excelFilesDir = new File("excelFiles")
   private var excelFileName = "properties.xml"
   private var excelFile = new File(excelFilesDir, excelFileName)
   private val javaPropertiesConnector = new JavaPropertiesConnector();
   private val javaFXPropertiesConnector = new JavaFXPropertiesConnector();
   private val excelTextConnector = new ExcelTextConnector();
   private val excelTextConnectorFX = new ExcelTextConnector("FX");
   private val excelXmlConnector = new ExcelXmlConnector();
   private val excelXmlConnectorFX = new ExcelXmlConnector("FX");
   private var prompt = ">";
   private var stores = new KeyValueStores()
   private var lastLine = "";

   def start() =
      {
         println("Welcome to " + name)
         var exit = false;
         do {
            printStoresSummary
            print(prompt)
            val line = readLine();
            exit = !handleCommand(line)
         } while (!exit)
         println("Thank you for using " + name)
      }

   private def handleCommand(line: String): Boolean = {
      val previousLine = lastLine
      lastLine = line
      val list = line.split(' ').toList
      list match {
         case "exit" :: tail => return false
         case "repeat" :: tail => repeatCommand(previousLine)
         case "r" :: tail => repeatCommand(previousLine)
         case "remove" ::"same" ::"excel" :: tail => removeSameStoresExcel(tail)
         case "remove" ::"same" ::"java" :: tail => removeSameStoresJava(tail)
         case "remove" :: tail => removeStores(tail)
         case "keep" :: tail => keepStores(tail)
         case "load" :: "excel" :: tail => loadExcel(tail)
         case "load" :: "java" :: tail => loadJava(tail)
         case "save" :: "excel" :: tail => saveExcel(tail)
         case "save" :: "java" :: tail => saveJava(tail)
         case "compare" :: "excel" :: tail => compareWithExcel(tail)
         case "compare" :: "java" :: tail => compareWithJava(tail)
         case "add" :: "new" :: "from" :: "excel" :: tail => addNewFromExcel(tail)
         case "add" :: "new" :: "from" :: "java" :: tail => addNewFromJava(tail)
         case "load" :: "changed" :: "from" :: "excel" :: tail => loadChangedFromExcel(tail)
         case "load" :: "changed" :: "from" :: "java" :: tail => loadChangedFromJava(tail)
         case _ => println("unknown option: " + list.head);
      }
      return true
   }

   private def printStoresSummary() =
      {
         println("Nr of stores: " + stores.size)
         var i = 1;
         for (store <- stores.getStores()) {
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

   private def repeatCommand(previousLine: String) = {
      println("Repeating: " + previousLine)
      handleCommand(previousLine)
      lastLine = previousLine
   }

   private def removeStores(params: List[String]) =
      {
         if (params.isEmpty) {
            stores.clear()
         } else {
            val notFoundNames = stores.removeStores(params)
            if (!notFoundNames.isEmpty) {
               println("Could not find stores: " + Utils.getSeqDisplay(notFoundNames))
            }
         }
      }

   private def keepStores(params: List[String]) =
      {
         val notFoundNames = stores.keepStores(params)
         if (!notFoundNames.isEmpty) {
            println("Could not find stores: " + Utils.getSeqDisplay(notFoundNames))
         }
      }

   private def loadExcel(params: List[String]) =
      {
         stores = realLoadExcel(params)
      }

   private def realLoadExcel(params: List[String]): KeyValueStores =
      {
         val file = if (params.isEmpty) excelFile else new File(excelFilesDir, params.head)
         excelXmlConnector.loadStores(file)
      }

   private def loadJava(params: List[String]) =
      {
         stores = realLoadJava(params)
      }

   private def realLoadJava(params: List[String]): KeyValueStores =
      {
         val loadedStores = new KeyValueStores();
         for (location <- javaLocations.locations) {
            loadedStores.addStore(loadStoreFromLocation(location))
         }
         loadedStores
      }

   private def loadStoreFromLocation(location: JavaLocation): KeyValueStore =
      {
         val store: KeyValueStore = new KeyValueStoreImpl()
         val languageDir = new File(javaLocations.clientRoot, location.modulePath + location.languagePath)
         val javaPropFile = new File(languageDir, location.moduleName)
         if (location.javaFX) {
            javaFXPropertiesConnector.loadStore(javaPropFile, store)
         } else {
            javaPropertiesConnector.loadStore(javaPropFile, store)
         }
         store.name = location.name
         store
      }

   private def saveExcel(params: List[String]) =
      {
         val file = if (params.isEmpty) excelFile else new File(excelFilesDir, params.head)
         excelXmlConnector.saveStores(stores, file)
      }

   private def saveJava(params: List[String]) =
      {
         for (store <- stores.getStores()) {
            val location = javaLocations.findJavaLocation(store.name)
            val languageDir = new File(javaLocations.clientRoot, location.modulePath + location.languagePath)
            val javaPropFile = new File(languageDir, location.moduleName)
            if (location.javaFX) {
               javaFXPropertiesConnector.saveStore(store, javaPropFile)
            } else {
               javaPropertiesConnector.saveStore(store, javaPropFile)
            }
         }
      }

   private def findLocations(params: List[String]): Seq[JavaLocation] =
      {
         var foundLocations = List[JavaLocation]()
         for (param <- params) {
            val location = javaLocations.findJavaLocation(param)
            if (location != null) {
               if (!foundLocations.contains(location)) {
                  foundLocations ::= location
               } else {
                  println("duplication location: " + param)
               }
            } else {
               println("cannot find location: " + param)
            }
         }
         foundLocations
      }

   private def findStores(params: List[String]): Seq[KeyValueStore] =
      {
         var foundStores = List[KeyValueStore]()
         for (param <- params) {
            val store = stores.getStore(param)
            if (store != None) {
               if (!foundStores.contains(store)) {
                  foundStores ::= store.get
               } else {
                  println("duplication store: " + param)
               }
            } else {
               println("cannot find store: " + param)
            }
         }
         foundStores
      }

   private def compareWithExcel(params: List[String]) =
      {
         compareWithOtherStores(realLoadExcel(params))
      }

   private def compareWithJava(params: List[String]) =
      {
         compareWithOtherStores(realLoadJava(params))
      }

   private def compareWithOtherStores(otherStores: KeyValueStores) {
      println(stores.compareWith(otherStores))
   }

   private def addNewFromExcel(params: List[String]) = {
      addNewFromOtherStores(realLoadExcel(params))
   }

   private def addNewFromJava(params: List[String]) = {
      addNewFromOtherStores(realLoadJava(params))
   }

   private def addNewFromOtherStores(otherStores: KeyValueStores) {
      stores.addNewFromOtherStores(otherStores)
   }

   private def loadChangedFromExcel(params: List[String]) = {
      loadChangedFromOtherStores(realLoadExcel(params))
   }

   private def loadChangedFromJava(params: List[String]) = {
      loadChangedFromOtherStores(realLoadJava(params))
   }

   private def loadChangedFromOtherStores(otherStores: KeyValueStores) {
      stores.loadChangedFromOtherStores(otherStores)
   }

   private def removeSameStoresExcel(params: List[String]) = {
      removeSameStores(realLoadExcel(params))
   }

   private def removeSameStoresJava(params: List[String]) = {
      removeSameStores(realLoadJava(params))
   }

   private def removeSameStores(otherStores: KeyValueStores) {
      stores.removeSameStores(otherStores)
   }

}