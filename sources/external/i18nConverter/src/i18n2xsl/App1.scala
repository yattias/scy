package i18n2xsl

import java.io.File
import java.lang.Boolean

/**
 * Created by IntelliJ IDEA.
 * User: sikken
 * Date: 13-6-11
 * Time: 16:41
 * To change this template use File | Settings | File Templates.
 */

object App1
{
  private val clientRoot = new File("d:\\Projects\\scy\\code\\scy-trunk\\sources\\modules\\client\\");
  private val javaLanguagePath = "/src/main/java/languages/"
  private val resourcesLanguagePath = "/src/main/resources/languages/"
  private val excelFilesDir = new File("excelFiles")
  private val javaPropertiesConnector = new JavaPropertiesConnector();
  private val javaFXPropertiesConnector = new JavaFXPropertiesConnector();
  private val excelTextConnector = new ExcelTextConnector();
  private val excelTextConnectorFX = new ExcelTextConnector("FX");
  private val excelXmlConnector = new ExcelXmlConnector();
  private val excelXmlConnectorFX = new ExcelXmlConnector("FX");

  def main(args: Array[String]): Unit =
  {
    println("Welcome to java(FX) properties <-> excel convertor")
//    test3()
//    createExcelFiles()
    writeJavaPropertyFiles()
  }

  private def test1() = {
    val store: KeyValueStore = new KeyValueStoreImpl();
    val scyDesktopFile = new File("files/scydesktop")
    javaPropertiesConnector.loadStore(scyDesktopFile, store)
    excelTextConnector.saveStore(store, scyDesktopFile)
    val store2: KeyValueStore = new KeyValueStoreImpl();
    val scyDesktopFile2 = new File("files/scydesktop2")
    excelTextConnector.loadStore(scyDesktopFile,store2)
    excelTextConnector.saveStore(store2, scyDesktopFile2)
    javaPropertiesConnector.saveStore(store2,scyDesktopFile2)
    val storeFX: KeyValueStore = new KeyValueStoreImpl();
    javaFXPropertiesConnector.loadStore(scyDesktopFile,storeFX)
    excelTextConnectorFX.saveStore(storeFX,scyDesktopFile)
  }

  private def test2() = {
    val store: KeyValueStore = new KeyValueStoreImpl();
    val storeFX: KeyValueStore = new KeyValueStoreImpl();
    val scyDesktopFile = new File("files/scydesktop")
    val scyDesktopFXFile = new File("files/scydesktopFX")
    excelTextConnector.loadStore(scyDesktopFile,store)
    javaPropertiesConnector.saveStore(store,scyDesktopFile)
    excelTextConnector.loadStore(scyDesktopFXFile,storeFX)
    javaFXPropertiesConnector.saveStore(storeFX,scyDesktopFXFile)

  }

  private def test3()={
    val store: KeyValueStore = new KeyValueStoreImpl();
    val store2: KeyValueStore = new KeyValueStoreImpl();
    val scyDesktopFile = new File("files/scydesktop")
    val scyDesktop2File = new File("files/scydesktop2")
    excelTextConnector.loadStore(scyDesktopFile,store)
    excelXmlConnector.saveStore(store,scyDesktopFile)
    excelXmlConnector.loadStore(scyDesktopFile,store2)
    excelXmlConnector.saveStore(store2,scyDesktop2File)
  }

  private def createExcelFiles() = {
    createExcelFile("desktop/scy-desktop","scydesktop",javaLanguagePath,true)
    createExcelFile("desktop/desktop-utils","desktoputils",javaLanguagePath)
    createExcelFile("tools/copex","copex",resourcesLanguagePath)
    createExcelFile("tools/dataprocesstool","fitex",resourcesLanguagePath)
    createExcelFile("tools/fx-copex","fxcopex",javaLanguagePath)
//    createExcelFile("tools/fx-resultBinder","fxresultbinder",javaLanguagePath,true)
    createExcelFile("tools/fx-simulator","fxsimulator",javaLanguagePath,true)
    createExcelFile("tools/fx-socialtaggingtool","fxsocialtaggingtool",javaLanguagePath,true)
    createExcelFile("tools/resultBinder","resultbinder",resourcesLanguagePath)
    createExcelFile("tools/scydynamics","scydynamics",javaLanguagePath)
    createExcelFile("tools/scysimulator","scysimulator",javaLanguagePath)
    createExcelFile("tools/scymapper","scymapper",resourcesLanguagePath)
  }

  private def createExcelFile(modulePath: String, moduleName: String, languagePath: String, includeFX: Boolean=false) = {
    println("creating excel files for " + moduleName);
    val languageDir = new File(clientRoot,modulePath + languagePath)
    val store: KeyValueStore = new KeyValueStoreImpl();
    val javaPropFile = new File(languageDir,moduleName);
    javaPropertiesConnector.loadStore(javaPropFile,store)
    val excelFile = new File(excelFilesDir,moduleName)
    excelXmlConnector.saveStore(store,excelFile)
    if (includeFX){
      val store: KeyValueStore = new KeyValueStoreImpl();
      javaFXPropertiesConnector.loadStore(javaPropFile,store)
      val excelFile = new File(excelFilesDir,moduleName)
      excelXmlConnectorFX.saveStore(store,excelFile)
    }
  }

  private def writeJavaPropertyFiles() = {
    writeJavaPropertyFile("desktop/scy-desktop","scydesktop",javaLanguagePath,true)
//    writeJavaPropertyFile("desktop/desktop-utils","desktoputils",javaLanguagePath)
//    writeJavaPropertyFile("tools/copex","copex",resourcesLanguagePath)
//    writeJavaPropertyFile("tools/dataprocesstool","fitex",resourcesLanguagePath)
//    writeJavaPropertyFile("tools/fx-copex","fxcopex",javaLanguagePath)
////    writeJavaPropertyFile("tools/fx-resultBinder","fxresultbinder",javaLanguagePath,true)
//    writeJavaPropertyFile("tools/fx-simulator","fxsimulator",javaLanguagePath,true)
//    writeJavaPropertyFile("tools/fx-socialtaggingtool","fxsocialtaggingtool",javaLanguagePath,true)
//    writeJavaPropertyFile("tools/resultBinder","resultbinder",resourcesLanguagePath)
//    writeJavaPropertyFile("tools/scydynamics","scydynamics",javaLanguagePath)
//    writeJavaPropertyFile("tools/scysimulator","scysimulator",javaLanguagePath)
//    writeJavaPropertyFile("tools/scymapper","scymapper",resourcesLanguagePath)
  }

  private def writeJavaPropertyFile(modulePath: String, moduleName: String, languagePath: String, includeFX: Boolean=false) = {
    println("writing java porperty files for " + moduleName);
    val languageDir = new File(clientRoot,modulePath + languagePath)
    val store: KeyValueStore = new KeyValueStoreImpl();
    val excelFile = new File(excelFilesDir,moduleName)
    excelXmlConnector.loadStore(excelFile,store)
    val javaPropFile = new File(languageDir,moduleName);
    javaPropertiesConnector.saveStore(store,javaPropFile)
    if (includeFX){
      val store: KeyValueStore = new KeyValueStoreImpl();
      val excelFile = new File(excelFilesDir,moduleName)
      excelXmlConnectorFX.loadStore(excelFile,store)
      javaFXPropertiesConnector.saveStore(store,javaPropFile)
    }
  }


}