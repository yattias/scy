/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.config;

import java.awt.Color;
import java.net.URI;
import java.util.List;
import roolo.elo.api.IMetadata;

/**
 *
 * @author sikken
 */
public interface MissionAnchor {

   public URI getEloUri();
   public IMetadata getMetadata();
   public String getId();
   public List<URI> getLoEloUris();
   public List<String> getInputMissionAnchorIds();
   public List<String> getRelationNames();
}
