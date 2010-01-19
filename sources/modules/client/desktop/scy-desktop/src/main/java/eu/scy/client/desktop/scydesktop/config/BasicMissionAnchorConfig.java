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
public class BasicMissionAnchorConfig {
   private String name;
   private String toolTip;
   private URI uri;
   private float xPosition;
   private float yPosition;
   private List<URI> intermediateEloUris;
   private List<String> nextMissionAnchorNames;
   private List<String> relationNames;
   private List<URI> resourceEloUris;
   private List<String> inputMissionAnchorNames;
   private boolean mainAnchorElo;

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getToolTip()
   {
      return toolTip;
   }

   public void setToolTip(String toolTip)
   {
      this.toolTip = toolTip;
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

   public List<URI> getIntermediateEloUris()
   {
      return intermediateEloUris;
   }

   public void setIntermediateEloUris(List<URI> intermediateEloUris)
   {
      this.intermediateEloUris = intermediateEloUris;
   }

   public List<URI> getResourceEloUris()
   {
      return resourceEloUris;
   }

   public void setResourceEloUris(List<URI> resourceEloUris)
   {
      this.resourceEloUris = resourceEloUris;
   }

   public List<String> getInputMissionAnchorNames()
   {
      return inputMissionAnchorNames;
   }

   public void setInputMissionAnchorNames(List<String> inputMissionAnchorNames)
   {
      this.inputMissionAnchorNames = inputMissionAnchorNames;
   }

   public boolean isMainAnchorElo()
   {
      return mainAnchorElo;
   }

   public void setMainAnchorElo(boolean mainAnchorElo)
   {
      this.mainAnchorElo = mainAnchorElo;
   }

   public URI getUri()
   {
      return uri;
   }

   public void setUri(URI uri)
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
