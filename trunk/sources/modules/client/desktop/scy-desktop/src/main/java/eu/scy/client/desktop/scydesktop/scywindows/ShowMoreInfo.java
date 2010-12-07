/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.scywindows;

import eu.scy.common.scyelo.ScyElo;
import java.net.URI;

/**
 *
 * @author SikkenJ
 */
public interface ShowMoreInfo
{

   public void showMoreInfo(URI infoUri, MoreInfoTypes type, ScyElo scyElo);

   public void showMoreInfo(URI infoUri, MoreInfoTypes type, URI eloUri);
}
