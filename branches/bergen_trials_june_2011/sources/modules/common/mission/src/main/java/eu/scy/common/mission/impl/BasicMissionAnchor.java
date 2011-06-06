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
   private List<MissionAnchor> inputMissionAnchors = new ArrayList<MissionAnchor>();
   private List<String> relationNames = new ArrayList<String>();
   private List<URI> loEloUris = new ArrayList<URI>();
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
      if (scyElo != null)
      {
         return scyElo.getUri();
      }
      return eloUri;
   }

   public void setEloUri(URI eloUri)
   {
      this.eloUri = eloUri;
      if (scyElo != null)
      {
         setExisting(scyElo.reloadFrom(eloUri));
      }
   }

   public List<MissionAnchor> getInputMissionAnchors()
   {
      return inputMissionAnchors;
   }

   public void setInputMissionAnchors(List<MissionAnchor> inputMissionAnchors)
   {
      assert inputMissionAnchors != null;
      this.inputMissionAnchors = inputMissionAnchors;
   }

   public List<String> getRelationNames()
   {
      return relationNames;
   }

   public void setRelationNames(List<String> relationNames)
   {
      assert relationNames != null;
      this.relationNames = relationNames;
   }

   public List<URI> getLoEloUris()
   {
      return loEloUris;
   }

   public void setLoEloUris(List<URI> loEloUris)
   {
      assert loEloUris != null;
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

    @Override
    public void setObligatoryInPorfolio(Boolean obligatoryInPorfolio) {
        getScyElo().setObligatoryInPortfolio(obligatoryInPorfolio);
    }

    @Override
    public Boolean getObligatoryInPortfolio() {
        return getScyElo().getObligatoryInPortfolio();
    }


}
