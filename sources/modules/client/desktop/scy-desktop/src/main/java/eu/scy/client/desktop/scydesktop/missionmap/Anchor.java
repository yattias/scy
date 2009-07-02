/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.missionmap;

import java.awt.Color;
import java.net.URI;
import java.util.List;

/**
 *
 * @author sikkenj
 */
public interface Anchor {

   public URI getEloUr();

   public String getTitle();

   public Color getColor();

   public float getXPos();

   public float getYPos();

   public List<Anchor> getNextAnchors();

   public List<String> getRelationNames();
}
