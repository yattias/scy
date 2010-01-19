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
public class BasicMissionAnchor implements MissionAnchor
{

   private URI eloUri;
   private String name;
   private String title;
   private String toolTip;
   private char iconCharacter;
   private Color color;
   private float xPosition;
   private float yPosition;
   private boolean existing;
   private List<URI> intermediateEloUris;
   private List<MissionAnchor> nextMissionAnchors;
   private List<MissionAnchor> inputMissionAnchors;
   private List<String> relationNames;
   private List<URI> resourceEloUris;
   private IMetadata metadata;

   @Override
   public Color getColor()
   {
      return color;
   }

   public void setColor(Color color)
   {
      this.color = color;
   }

   @Override
   public URI getEloUri()
   {
      return eloUri;
   }

   public void setEloUri(URI eloUri)
   {
      this.eloUri = eloUri;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   @Override
   public String getName()
   {
      return name;
   }

   @Override
   public boolean isExisting()
   {
      return existing;
   }

   public void setExisting(boolean existing)
   {
      this.existing = existing;
   }

   @Override
   public char getIconCharacter()
   {
      return iconCharacter;
   }

   public void setIconCharacter(char iconCharacter)
   {
      this.iconCharacter = iconCharacter;
   }

   public void setIntermediateEloUris(List<URI> intermediateEloUris)
   {
      this.intermediateEloUris = intermediateEloUris;
   }

   @Override
   public List<URI> getIntermediateEloUris()
   {
      return intermediateEloUris;
   }

   @Override
   public List<MissionAnchor> getNextMissionAnchors()
   {
      return nextMissionAnchors;
   }

   public void setNextMissionAnchors(List<MissionAnchor> nextMissionAnchors)
   {
      this.nextMissionAnchors = nextMissionAnchors;
   }

   @Override
   public List<MissionAnchor> getInputMissionAnchors()
   {
      return inputMissionAnchors;
   }

   public void setInputMissionAnchors(List<MissionAnchor> inputMissionAnchors)
   {
      this.inputMissionAnchors = inputMissionAnchors;
   }

   @Override
   public List<String> getRelationNames()
   {
      return relationNames;
   }

   public void setRelationNames(List<String> relationNames)
   {
      this.relationNames = relationNames;
   }

   @Override
   public String getTitle()
   {
      return title;
   }

   public void setTitle(String title)
   {
      this.title = title;
   }

   @Override
   public String getToolTip()
   {
      return toolTip;
   }

   public void setToolTip(String toolTip)
   {
      this.toolTip = toolTip;
   }

   @Override
   public float getXPosition()
   {
      return xPosition;
   }

   public void setXPosition(float xPosition)
   {
      this.xPosition = xPosition;
   }

   @Override
   public float getYPosition()
   {
      return yPosition;
   }

   public void setYPosition(float yPosition)
   {
      this.yPosition = yPosition;
   }

   public List<URI> getResourceEloUris()
   {
      return resourceEloUris;
   }

   public void setResourceEloUris(List<URI> resourceEloUris)
   {
      this.resourceEloUris = resourceEloUris;
   }

   public void setMetadata(IMetadata metadata)
   {
      this.metadata = metadata;
   }

   @Override
   public IMetadata getMetadata()
   {
      return metadata;
   }


}
