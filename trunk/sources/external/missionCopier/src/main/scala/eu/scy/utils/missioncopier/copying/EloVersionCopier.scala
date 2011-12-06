package eu.scy.utils.missioncopier.copying

import java.net.URI

/**
 * Created by IntelliJ IDEA.
 * User: SikkenJ
 * Date: 6-12-11
 * Time: 10:37
 */

trait EloVersionCopier {
  def copyElos(eloUris: Seq[URI])
}