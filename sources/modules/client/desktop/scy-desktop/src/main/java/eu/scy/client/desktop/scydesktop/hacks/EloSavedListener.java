/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.hacks;

import java.net.URI;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadata;

/**
 *
 * @author sikkenj
 */
public interface EloSavedListener {

	 void newEloSaved(URI eloURI, IELO elo, IMetadata metadata);
	 void forkedEloSaved(URI eloURI, IELO elo, IMetadata metadata);
	 void eloUpdated(URI eloURI, IELO elo, IMetadata metadata);
	 void metadataChanged(URI eloURI, IMetadata metadata);
}
