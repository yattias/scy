/*
 * EloInfo.fx
 *
 * Created on 24-jun-2009, 17:37:54
 */

package eu.scy.client.desktop.scydesktop.scywindows;

import java.net.URI;

/**
 * @author sikkenj
 */

public mixin class EloInfoControl {

   public abstract function getEloType(eloUri:URI):String;

   public abstract function getEloTitle(eloUri:URI):String;

}
