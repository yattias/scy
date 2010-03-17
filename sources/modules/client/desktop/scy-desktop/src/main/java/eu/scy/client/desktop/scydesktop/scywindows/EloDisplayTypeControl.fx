/*
 * EloTypeControl.fx
 *
 * Created on 17-mrt-2010, 11:33:52
 */

package eu.scy.client.desktop.scydesktop.scywindows;
import java.net.URI;

/**
 * @author sikken
 */

public mixin class EloDisplayTypeControl {

   abstract public function getEloType(eloUri:URI):String;
}
