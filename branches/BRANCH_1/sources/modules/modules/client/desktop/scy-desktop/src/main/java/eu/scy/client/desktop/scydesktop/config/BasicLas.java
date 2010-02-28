/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.client.desktop.scydesktop.config;

import java.net.URI;
import java.util.List;

/**
 *
 * @author sikken
 */
public class BasicLas {
   private String id;
   private float xPosition;
   private float yPosition;
   private List<URI> loEloUris;
   private List<String> nextLasses;
   private String anchorEloId;
   private List<String> intermediateEloIds;
   private String tooltip;

   public String getAnchorEloId()
   {
      return anchorEloId;
   }

   public void setAnchorEloId(String anchorEloId)
   {
      this.anchorEloId = anchorEloId;
   }

   public String getId()
   {
      return id;
   }

   public void setId(String id)
   {
      this.id = id;
   }

   public List<String> getIntermediateEloIds()
   {
      return intermediateEloIds;
   }

   public void setIntermediateEloIds(List<String> intermediateEloIds)
   {
      this.intermediateEloIds = intermediateEloIds;
   }

   public List<URI> getLoEloUris()
   {
      return loEloUris;
   }

   public void setLoEloUris(List<URI> loEloUris)
   {
      this.loEloUris = loEloUris;
   }

   public List<String> getNextLasses()
   {
      return nextLasses;
   }

   public void setNextLasses(List<String> nextLasses)
   {
      this.nextLasses = nextLasses;
   }

   public String getTooltip()
   {
      return tooltip;
   }

   public void setTooltip(String tooltip)
   {
      this.tooltip = tooltip;
   }

   public float getxPosition()
   {
      return xPosition;
   }

   public void setxPosition(float xPosition)
   {
      this.xPosition = xPosition;
   }

   public float getyPosition()
   {
      return yPosition;
   }

   public void setyPosition(float yPosition)
   {
      this.yPosition = yPosition;
   }

   
}
