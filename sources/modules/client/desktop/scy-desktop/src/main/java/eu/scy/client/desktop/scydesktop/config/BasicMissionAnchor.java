/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.config;

import eu.scy.client.desktop.scydesktop.tools.corner.missionmap.MissionAnchor;
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
   private char iconCharacter;
   private Color color;
   private float xPosition;
   private float yPosition;
   private boolean existing;
   private List<MissionAnchor> nextMissionAnchors;
   private List<String> relationNames;
   private List<URI> helpEloUris;
   private IMetadata metadata;

   public Color getColor()
   {
      return color;
   }

   public void setColor(Color color)
   {
      this.color = color;
   }

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

   public boolean isExisting()
   {
      return existing;
   }

   public void setExisting(boolean existing)
   {
      this.existing = existing;
   }

   public char getIconCharacter()
   {
      return iconCharacter;
   }

   public void setIconCharacter(char iconCharacter)
   {
      this.iconCharacter = iconCharacter;
   }

   public List<MissionAnchor> getNextMissionAnchors()
   {
      return nextMissionAnchors;
   }

   public void setNextMissionAnchors(List<MissionAnchor> nextMissionAnchors)
   {
      this.nextMissionAnchors = nextMissionAnchors;
   }

   public List<String> getRelationNames()
   {
      return relationNames;
   }

   public void setRelationNames(List<String> relationNames)
   {
      this.relationNames = relationNames;
   }

   public String getTitle()
   {
      return title;
   }

   public void setTitle(String title)
   {
      this.title = title;
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

   @Override
   public List<URI> getHelpEloUris()
   {
      return helpEloUris;
   }

   public void setHelpEloUris(List<URI> helpEloUris)
   {
      this.helpEloUris = helpEloUris;
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
