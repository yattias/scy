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
public class BasicMissionModel implements MissionModel {

   private List<Anchor> anchors;
   private Anchor activeAnchor;

   public BasicMissionModel(List<Anchor> anchors, Anchor activeAnchor)
   {
      this.anchors = anchors;
      this.activeAnchor = activeAnchor;
   }

   @Override
   public List<Anchor> getAnchors()
   {
      return anchors;
   }

   @Override
   public void setActiveAnchor(Anchor anchor)
   {
      activeAnchor = anchor;
   }

   @Override
   public Anchor getActiveAnchor()
   {
      return activeAnchor;
   }

   @Override
   public void addActiveAnchorChangeListener(ActiveAnchorChangedListener activeAnchorChangeListener)
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }

   @Override
   public void removeActiveAnchorChangeListener(ActiveAnchorChangedListener activeAnchorChangeListener)
   {
      throw new UnsupportedOperationException("Not supported yet.");
   }

}
