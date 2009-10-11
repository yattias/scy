/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.config;

import java.util.List;

/**
 *
 * @author sikken
 */
public class BasicMissionAnchorConfig {
   private String uri;
   private float xPosition;
   private float yPosition;
   private List<String> nextMissionAnchorConfigUris;
   private List<String> relationNames;

   public List<String> getNextMissionAnchorConfigUris()
   {
      return nextMissionAnchorConfigUris;
   }

   public void setNextMissionAnchorConfigUris(List<String> nextMissionAnchorConfigUris)
   {
      this.nextMissionAnchorConfigUris = nextMissionAnchorConfigUris;
   }

   public List<String> getRelationNames()
   {
      return relationNames;
   }

   public void setRelationNames(List<String> relationNames)
   {
      this.relationNames = relationNames;
   }

   public String getUri()
   {
      return uri;
   }

   public void setUri(String uri)
   {
      this.uri = uri;
   }

   public float getXPosition()
   {
      return xPosition;
   }

   public void setXPosition(float xPosition)
   {
      this.xPosition = xPosition;
   }

   public float getYPosition()
   {
      return yPosition;
   }

   public void setYPosition(float yPosition)
   {
      this.yPosition = yPosition;
   }
   
}
