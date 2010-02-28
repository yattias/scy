/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.hacks;

import java.net.URI;

/**
 *
 * @author sikkenj
 */
public interface EloSavedListener {

	 void newEloSaved(URI eloURI);
	 void forkedEloSaved(URI eloURI);
	 void eloUpdated(URI eloURI);
}
