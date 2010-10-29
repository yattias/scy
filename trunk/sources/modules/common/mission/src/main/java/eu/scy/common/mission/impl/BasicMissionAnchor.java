package eu.scy.common.mission.impl;

import java.net.URI;
import java.util.List;

import eu.scy.common.mission.ColorSchemeId;
import eu.scy.common.mission.Las;
import eu.scy.common.mission.MissionAnchor;
import eu.scy.common.scyelo.ScyElo;

public class BasicMissionAnchor implements MissionAnchor
{
   private URI eloUri;
   private List<MissionAnchor> inputMissionAnchors;
   private List<String> relationNames;
   private List<URI> loEloUris;
   private boolean existing;
   private Las las;
   private ScyElo scyElo;
   private URI targetDescriptionUri;
   private URI assignmentUri;
   private URI resourcesUri;
   private String iconType;
   private ColorSchemeId colorSchemeId;
   
   public URI getEloUri()
   {
      if (scyElo!=null){
         return scyElo.getUri();
      }
      if (!existing){
         return null;
      }
      return eloUri;
   }
   public void setEloUri(URI eloUri)
   {
      this.eloUri = eloUri;
   }
   public List<MissionAnchor> getInputMissionAnchors()
   {
      return inputMissionAnchors;
   }
   public void setInputMissionAnchors(List<MissionAnchor> inputMissionAnchors)
   {
      this.inputMissionAnchors = inputMissionAnchors;
   }
   public List<String> getRelationNames()
   {
      return relationNames;
   }
   public void setRelationNames(List<String> relationNames)
   {
      this.relationNames = relationNames;
   }
   public List<URI> getLoEloUris()
   {
      return loEloUris;
   }
   public void setLoEloUris(List<URI> loEloUris)
   {
      this.loEloUris = loEloUris;
   }
   public boolean isExisting()
   {
      return existing;
   }
   public void setExisting(boolean existing)
   {
      this.existing = existing;
   }
   public Las getLas()
   {
      return las;
   }
   public void setLas(Las las)
   {
      this.las = las;
   }
   public ScyElo getScyElo()
   {
      return scyElo;
   }
   public void setScyElo(ScyElo scyElo)
   {
      this.scyElo = scyElo;
   }
   public URI getTargetDescriptionUri()
   {
      return targetDescriptionUri;
   }
   public void setTargetDescriptionUri(URI targetDescriptionUri)
   {
      this.targetDescriptionUri = targetDescriptionUri;
   }
   public URI getAssignmentUri()
   {
      return assignmentUri;
   }
   public void setAssignmentUri(URI assignmentUri)
   {
      this.assignmentUri = assignmentUri;
   }
   public URI getResourcesUri()
   {
      return resourcesUri;
   }
   public void setResourcesUri(URI resourcesUri)
   {
      this.resourcesUri = resourcesUri;
   }
   public ColorSchemeId getColorSchemeId()
   {
      return colorSchemeId;
   }
   public void setColorSchemeId(ColorSchemeId colorSchemeId)
   {
      this.colorSchemeId = colorSchemeId;
   }
   public String getIconType()
   {
      return iconType;
   }
   public void setIconType(String iconType)
   {
      this.iconType = iconType;
   }
   
}
