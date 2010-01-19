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
   public String getName();
   public String getTitle();
   public String getToolTip();
   public char getIconCharacter();
   public Color getColor();
   public float getXPosition();
   public float getYPosition();
   public boolean isExisting();
   public List<URI> getIntermediateEloUris();
   public List<MissionAnchor> getNextMissionAnchors();
   public List<MissionAnchor> getInputMissionAnchors();
   public List<String> getRelationNames();
   public List<URI> getResourceEloUris();
   public IMetadata getMetadata();
}
