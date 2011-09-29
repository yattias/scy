package eu.scy.common.mission.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import eu.scy.common.mission.Las;
import eu.scy.common.mission.MissionAnchor;
import eu.scy.common.scyelo.ColorSchemeId;
import eu.scy.common.scyelo.ScyElo;

public class BasicMissionAnchor implements MissionAnchor
{
   private URI eloUri;
   private String id;
   private List<MissionAnchor> inputMissionAnchors = new ArrayList<MissionAnchor>();
   private List<String> relationNames = new ArrayList<String>();
   private List<String> dependingOnMissionAnchorIds = new ArrayList<String>();
   private List<URI> loEloUris = new ArrayList<URI>();
   private boolean existing;
   private Las las;
   private ScyElo scyElo;
   private URI targetDescriptionUri;
   private URI assignmentUri;
   private URI resourcesUri;
   private URI helpUri;
   private URI webNewsUri;
   private String iconType;
   private ColorSchemeId colorSchemeId;

   @Override
   public URI getEloUri()
   {
      if (scyElo != null)
      {
         return scyElo.getUri();
      }
      return eloUri;
   }

   @Override
   public void setEloUri(URI eloUri)
   {
      this.eloUri = eloUri;
      if (scyElo != null)
      {
         setExisting(scyElo.reloadFrom(eloUri));
      }
   }

   @Override
   public String getId()
   {
      return id;
   }

   public void setId(String id)
   {
      this.id = id;
   }

   @Override
   public List<MissionAnchor> getInputMissionAnchors()
   {
      return inputMissionAnchors;
   }

   @Override
   public void setInputMissionAnchors(List<MissionAnchor> inputMissionAnchors)
   {
      assert inputMissionAnchors != null;
      this.inputMissionAnchors = inputMissionAnchors;
   }

   @Override
   public List<String> getRelationNames()
   {
      return relationNames;
   }

   public void setRelationNames(List<String> relationNames)
   {
      assert relationNames != null;
      this.relationNames = relationNames;
   }

   @Override
   public List<String> getDependingOnMissionAnchorIds()
   {
      return dependingOnMissionAnchorIds;
   }

   public void setDependingOnMissionAnchorIds(List<String> dependingOnMissionAnchorIds)
   {
      this.dependingOnMissionAnchorIds = dependingOnMissionAnchorIds;
   }

   @Override
   public List<URI> getLoEloUris()
   {
      return loEloUris;
   }

   @Override
   public void setLoEloUris(List<URI> loEloUris)
   {
      assert loEloUris != null;
      this.loEloUris = loEloUris;
   }

   @Override
   public boolean isExisting()
   {
      return existing;
   }

   @Override
   public void setExisting(boolean existing)
   {
      this.existing = existing;
   }

   @Override
   public Las getLas()
   {
      return las;
   }

   @Override
   public void setLas(Las las)
   {
      this.las = las;
   }

   @Override
   public ScyElo getScyElo()
   {
      return scyElo;
   }

   @Override
   public void setScyElo(ScyElo scyElo)
   {
      this.scyElo = scyElo;
   }

   @Override
   public URI getTargetDescriptionUri()
   {
      return targetDescriptionUri;
   }

   public void setTargetDescriptionUri(URI targetDescriptionUri)
   {
      this.targetDescriptionUri = targetDescriptionUri;
   }

   @Override
   public URI getAssignmentUri()
   {
      return assignmentUri;
   }

   @Override
   public void setAssignmentUri(URI assignmentUri)
   {
      this.assignmentUri = assignmentUri;
   }

   @Override
   public URI getResourcesUri()
   {
      return resourcesUri;
   }

   public void setResourcesUri(URI resourcesUri)
   {
      this.resourcesUri = resourcesUri;
   }

   @Override
   public ColorSchemeId getColorSchemeId()
   {
      return colorSchemeId;
   }

   public void setColorSchemeId(ColorSchemeId colorSchemeId)
   {
      this.colorSchemeId = colorSchemeId;
   }

   @Override
   public String getIconType()
   {
      return iconType;
   }

   public void setIconType(String iconType)
   {
      this.iconType = iconType;
   }

    @Override
    public void setObligatoryInPorfolio(Boolean obligatoryInPorfolio) {
        getScyElo().setObligatoryInPortfolio(obligatoryInPorfolio);
    }

    @Override
    public Boolean getObligatoryInPortfolio() {
        return getScyElo().getObligatoryInPortfolio();
    }

   @Override
   public URI getHelpUri()
   {
      return helpUri;
   }

   public void setHelpUri(URI helpUri)
   {
      this.helpUri = helpUri;
   }

   @Override
   public URI getWebNewsUri()
   {
      return webNewsUri;
   }

   public void setWebNewsUri(URI webNewsUri)
   {
      this.webNewsUri = webNewsUri;
   }
}
