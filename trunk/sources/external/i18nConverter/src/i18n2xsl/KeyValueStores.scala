package i18n2xsl

import collection.mutable.{ ArraySeq, HashMap }

/**
 * Created by IntelliJ IDEA.
 * User: sikken
 * Date: 10-7-11
 * Time: 18:26
 * To change this template use File | Settings | File Templates.
 */

class KeyValueStores {

   private val storeMap = HashMap[String, KeyValueStore]()

   def isEmpty: Boolean = storeMap.isEmpty

   def size: Int = storeMap.size

   def getStore(name: String): Option[KeyValueStore] =
      {
         storeMap.get(name)
      }

   def containsStore(name: String): Boolean =
      {
         storeMap.contains(name)
      }

   def getStores(): Seq[KeyValueStore] =
      {
         var stores = ArraySeq[KeyValueStore]()
         for (store <- storeMap.valuesIterator) {
            stores :+= store
         }
         stores
      }

   def addStore(store: KeyValueStore): Unit =
      {
         storeMap.put(store.name, store)
      }

   def clear(): Unit =
      {
         storeMap.clear()
      }

   def removeStores(names: List[String]): Seq[String] =
      {
         val notFoundNames = findNotExistingName(names)
         if (notFoundNames.isEmpty)
            for (name <- names) {
               if (containsStore(name)) {
                  storeMap.remove(name)
               }
            }
         notFoundNames
      }

   def keepStores(names: List[String]): Seq[String] =
      {
         val notFoundNames = findNotExistingName(names)
         if (notFoundNames.isEmpty)
            for (store <- getStores()) {
               if (!names.contains(store.name)) {
                  storeMap.remove(store.name)
               }
            }
         notFoundNames
      }

   private def findNotExistingName(names: List[String]): Seq[String] =
      {
         var notFoundNames = Seq[String]()
         for (name <- names) {
            if (!containsStore(name)) {
               notFoundNames :+= name
            }
         }
         notFoundNames
      }

   def compareWith(otherStores: KeyValueStores): String = {
      val result = new StringBuilder
      for (store <- getStores()){
         result.append("Comparing store: " + store.name + "\n")
         val otherStore = otherStores.getStore(store.name)
         otherStore match {
            case Some(aStore) => addLinesIndented(result," ",store.compareWith(aStore))
            case None => result.append(" not present in other\n");
         }
      }
      result.toString
   }
   
   private def addLinesIndented(result: StringBuilder, indentation: String, lines: String): Unit = {
      lines.split("\n").foreach(line => result.append(indentation + line + "\n"))
   }
   
   def addNewFromOtherStores(otherStores: KeyValueStores): Unit = {
      for (store <- getStores()){
         println("Checking store: " + store.name)
         val otherStore = otherStores.getStore(store.name)
         otherStore match {
            case Some(aStore) => store.addNewFromOtherStore(aStore)
            case None => 
         }
      }
   }
   
   def loadChangedFromOtherStores(otherStores: KeyValueStores): Unit = {
      for (store <- getStores()){
         println("Checking store: " + store.name)
         val otherStore = otherStores.getStore(store.name)
         otherStore match {
            case Some(aStore) => store.loadChangedFromOtherStore(aStore)
            case None => 
         }
      }
   }
   
   def removeSameStores(otherStores: KeyValueStores): Unit = {
      var storesToRemove = List[String]()
      for (store <- getStores()){
         println("Checking store: " + store.name)
         val otherStore = otherStores.getStore(store.name)
         otherStore match {
            case Some(aStore: KeyValueStore) if (store==aStore) => storesToRemove +:= store.name
            case _ =>
         }
      }
      removeStores(storesToRemove)
   }
   
}