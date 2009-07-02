/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.missionmap;

import java.util.List;

/**
 *
 * @author sikkenj
 */
public interface MissionModel
{
   public List<Anchor> getAnchors();

   public void setActiveAnchor(Anchor anchor);

   public Anchor getActiveAnchor();

   public void addActiveAnchorChangeListener(ActiveAnchorChangedListener activeAnchorChangeListener);
   
   public void removeActiveAnchorChangeListener(ActiveAnchorChangedListener activeAnchorChangeListener);
}
