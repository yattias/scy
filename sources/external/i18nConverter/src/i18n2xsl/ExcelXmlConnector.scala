package i18n2xsl

import java.io.File
import collection.mutable.ArrayBuffer
import xml.{NodeSeq, Elem, XML}
import java.lang.Boolean

/**
 * Created by IntelliJ IDEA.
 * User: sikken
 * Date: 25-6-11
 * Time: 15:42
 * To change this template use File | Settings | File Templates.
 */

class ExcelXmlConnector(nameAppend: String = "") extends FormatConnector
{
  val excelExtension = ".xml"
  val encoding = "UTF-8"

  def loadStore(file: File, store: KeyValueStore): Unit =
  {
    val inputFile = getExcelFile(file);
    if (!inputFile.exists()){
      println("Could not find excel xml file: " + inputFile.getAbsolutePath)
      return
    }
    println("Reading excel xml file: " + inputFile.getAbsolutePath)
    val xml = XML.loadFile(inputFile)
    val worksheetXml = xml \ "Worksheet"
    worksheetXmlToStore(worksheetXml, store)
  }

  def saveStore(store: KeyValueStore, file: File): Unit =
  {
    if (store.getKeys().size == 0) {
      return
    }
    val outputFile = getExcelFile(file);
    println("Saving in excel xml file: " + outputFile.getAbsolutePath)
    val worksheetXml = storeToWorksheetXml(store)
    val xml = getWorkbookXml(List(worksheetXml))
    XML.save(outputFile.getAbsolutePath, xml, encoding, true)
  }

  def loadStoresOld(file: File): Seq[KeyValueStore] = {
    var stores = Seq[KeyValueStore]();
    val inputFile = getExcelFile(file);
    if (!inputFile.exists()){
      println("Could not find excel xml file: " + inputFile.getAbsolutePath)
      return stores
    }
    println("Reading stores from excel xml file: " + inputFile.getAbsolutePath)
    val xml = XML.loadFile(inputFile)
    stores = for (worksheetXml <- xml \ "Worksheet") yield {
      val store: KeyValueStore = new KeyValueStoreImpl()
      worksheetXmlToStore(worksheetXml, store)
      store
    }
    return stores
  }

  def loadStores(file: File): KeyValueStores = {
    val stores = new KeyValueStores();
    val inputFile = getExcelFile(file);
    if (!inputFile.exists()){
      println("Could not find excel xml file: " + inputFile.getAbsolutePath)
      return stores
    }
    println("Reading stores from excel xml file: " + inputFile.getAbsolutePath)
    val xml = XML.loadFile(inputFile)
    for (worksheetXml <- xml \ "Worksheet") {
      val store: KeyValueStore = new KeyValueStoreImpl()
      worksheetXmlToStore(worksheetXml, store)
      stores.addStore(store)
    }
    return stores
  }

  def saveStores(stores: List[KeyValueStore], file: File): Unit =
  {
    val outputFile = getExcelFile(file);
    println("Saving in excel xml file: " + outputFile.getAbsolutePath)
    val worksheetXmlList = for (store <- stores) yield {
      storeToWorksheetXml(store)
    }
    val xml = getWorkbookXml(worksheetXmlList)
    XML.save(outputFile.getAbsolutePath, xml, encoding, true)
  }

  def saveStores(stores: KeyValueStores, file: File): Unit =
  {
    val outputFile = getExcelFile(file);
    println("Saving in excel xml file: " + outputFile.getAbsolutePath)
    val worksheetXmlList = for (store <- stores.getStores()) yield {
      storeToWorksheetXml(store)
    }
    val xml = getWorkbookXml(worksheetXmlList)
    XML.save(outputFile.getAbsolutePath, xml, encoding, true)
  }

  def saveStores(stores: Seq[KeyValueStore], file: File): Unit =
  {
    val outputFile = getExcelFile(file);
    println("Saving in excel xml file: " + outputFile.getAbsolutePath)
    val worksheetXmlList = for (store <- stores) yield {
      storeToWorksheetXml(store)
    }
    val xml = getWorkbookXml(worksheetXmlList)
    XML.save(outputFile.getAbsolutePath, xml, encoding, true)
  }

  private def worksheetXmlToStore(worksheetXml: NodeSeq, store: KeyValueStore) = {
    val worksheetNode = worksheetXml.head
//    store.name = worksheetXml \ "@{urn:schemas-microsoft-com:office:spreadsheet}Name".head.text
    val nameAttributes = worksheetXml \ "@{urn:schemas-microsoft-com:office:spreadsheet}Name"
    if (!nameAttributes.isEmpty){
      store.name = nameAttributes.head.text
    }
    val rows = worksheetXml \ "Table" \ "Row"
    val languages = ArrayBuffer[String]()
    val headerRow = rows.head
    val headerDatas = rows.head \\ "Data"
    for (data <- (rows.head \\ "Data").tail){
      languages += data.text
    }
    for (row <- rows.tail){
      val datas = row \\ "Data"
      val key = datas.head.text
      var index = 0;
      for (data <- datas.tail){
        store.addKeyValue(key, languages(index), data.text)
        index+=1
      }
    }
  }

  private def getWorkbookXml(worksheetXmlList: List[Elem]): Elem = {
    <Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet"
              xmlns:o="urn:schemas-microsoft-com:office:office"
              xmlns:x="urn:schemas-microsoft-com:office:excel"
              xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet">
      {for (worksheetXml <- worksheetXmlList) yield
      worksheetXml}
    </Workbook>
  }

  private def getWorkbookXml(worksheetXmlList: Seq[Elem]): Elem = {
    <Workbook xmlns="urn:schemas-microsoft-com:office:spreadsheet"
              xmlns:o="urn:schemas-microsoft-com:office:office"
              xmlns:x="urn:schemas-microsoft-com:office:excel"
              xmlns:ss="urn:schemas-microsoft-com:office:spreadsheet">
      {for (worksheetXml <- worksheetXmlList) yield
      worksheetXml}
    </Workbook>
  }

  private def storeToWorksheetXml(store: KeyValueStore) : Elem = {
    val languages = store.getSortedLanguages()
      <Worksheet ss:Name={store.name}>
        <Table>
          <Row>
            <Cell>
              <Data ss:Type="String">Key</Data>
            </Cell>{for (lang <- languages) yield
            <Cell>
              <Data ss:Type="String">{lang}</Data>
            </Cell>}
          </Row>{for (key <- store.getSortedKeys()) yield
          <Row>
            <Cell>
              <Data ss:Type="String">{key}</Data>
            </Cell>{for (lang <- languages) yield
            <Cell>
              <Data ss:Type="String">{store.getValue(key, lang) match {case Some(value:String) => value; case _ =>}}</Data>
            </Cell>}
          </Row>}
        </Table>
      </Worksheet>
  }

  private def compareStrings(s1: String, s2: String) : Boolean = {
    s1.compareToIgnoreCase(s2)<0
  }

  private def getExcelFile(file: File): File =
  {
    if (!file.getName.endsWith(excelExtension)) {
      return new File(file.getParentFile, file.getName() + nameAppend + excelExtension)
    }
    return file
  }

}