/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.config;

import eu.scy.common.mission.LasType;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author sikken
 */
public class BasicLas
{

   private String id;
   private float xPosition;
   private float yPosition;
   private List<URI> loEloUris = new ArrayList<URI>();
   private List<String> nextLasses = new ArrayList<String>();
   private String anchorEloId;
   private List<String> intermediateEloIds = new ArrayList<String>();
   private String tooltip;
   private URI instructionUri;
   private LasType lasType;
   private GroupFormationConfig groupFormationConfig;

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
      if (intermediateEloIds != null)
      {
         this.intermediateEloIds = intermediateEloIds;
      }
   }

   public List<URI> getLoEloUris()
   {
      return loEloUris;
   }

   public void setLoEloUris(List<URI> loEloUris)
   {
      if (loEloUris != null)
      {
         this.loEloUris = loEloUris;
      }
   }

   public List<String> getNextLasses()
   {
      return nextLasses;
   }

   public void setNextLasses(List<String> nextLasses)
   {
      if (nextLasses != null)
      {
         this.nextLasses = nextLasses;
      }
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

   public URI getInstructionUri()
   {
      return instructionUri;
   }

   public void setInstructionUri(URI instructionUri)
   {
      this.instructionUri = instructionUri;
   }

   public LasType getLasType()
   {
      return lasType;
   }

   public void setLasType(LasType lasType)
   {
      this.lasType = lasType;
   }

   public GroupFormationConfig getGroupFormationConfig()
   {
      return groupFormationConfig;
   }

   public void setGroupFormationConfig(GroupFormationConfig groupFormationConfig)
   {
      this.groupFormationConfig = groupFormationConfig;
   }
}
