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
   private String name;
   private String uri;
   private float xPosition;
   private float yPosition;
   private List<String> nextMissionAnchorNames;
   private List<String> relationNames;

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public List<String> getNextMissionAnchorNames()
   {
      return nextMissionAnchorNames;
   }

   public void setNextMissionAnchorNames(List<String> nextMissionAnchorConfigUris)
   {
      this.nextMissionAnchorNames = nextMissionAnchorConfigUris;
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

   public void setxPosition(float xPosition)
   {
      this.xPosition = xPosition;
   }

   public float getYPosition()
   {
      return yPosition;
   }

   public void setyPosition(float yPosition)
   {
      this.yPosition = yPosition;
   }
   
}
