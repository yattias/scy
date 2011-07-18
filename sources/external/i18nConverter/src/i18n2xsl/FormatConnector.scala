package i18n2xsl

import java.io.File

/**
 * Created by IntelliJ IDEA.
 * User: sikken
 * Date: 19-6-11
 * Time: 16:02
 * To change this template use File | Settings | File Templates.
 */

trait FormatConnector
{
  def loadStore(file: File, store: KeyValueStore): Unit

  def saveStore(store: KeyValueStore, file: File) : Unit
}