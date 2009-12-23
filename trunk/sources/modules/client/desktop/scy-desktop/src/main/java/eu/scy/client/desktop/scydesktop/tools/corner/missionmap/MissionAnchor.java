/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.tools.corner.missionmap;

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
   public char getIconCharacter();
   public Color getColor();
   public float getXPosition();
   public float getYPosition();
   public boolean isExisting();
   public List<MissionAnchor> getNextMissionAnchors();
   public List<String> getRelationNames();
   public List<URI> getHelpEloUris();
   public IMetadata getMetadata();
}
