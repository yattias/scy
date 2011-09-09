package eu.scy.common.mission;

import java.net.URI;
import java.util.List;

import eu.scy.common.scyelo.ColorSchemeId;
import eu.scy.common.scyelo.ScyElo;

public interface MissionAnchor
{
   public URI getEloUri();
   public String getId();
   public List<MissionAnchor> getInputMissionAnchors();
   public List<String> getRelationNames();
   public List<String> getDependingOnMissionAnchorIds();
   public boolean isExisting();
   public List<URI> getLoEloUris();
   public Las getLas();
   public ScyElo getScyElo();
   public URI getTargetDescriptionUri();
   public URI getAssignmentUri();
   public URI getResourcesUri();
   public URI getHelpUri();
   public String getIconType();
   public ColorSchemeId getColorSchemeId();

   public void setLas(Las las);
   public void setInputMissionAnchors(List<MissionAnchor> inputMissionAnchors);
   public void setExisting(boolean existing);
   public void setAssignmentUri(URI uri);
   public void setScyElo(ScyElo scyElo);
   public void setEloUri(URI eloUri);
   public void setLoEloUris(List<URI> loEloUris);

   public void setObligatoryInPorfolio(Boolean obligatoryInPorfolio);
   public Boolean getObligatoryInPortfolio();
}
