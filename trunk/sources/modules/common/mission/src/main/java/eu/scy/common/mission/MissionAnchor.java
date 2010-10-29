package eu.scy.common.mission;

import java.net.URI;
import java.util.List;

import eu.scy.common.scyelo.ScyElo;

public interface MissionAnchor
{
   public URI getEloUri();
   public List<MissionAnchor> getInputMissionAnchors();
   public List<String> getRelationNames();
   public boolean isExisting();
   public List<URI> getLoEloUris();
   public Las getLas();
   public ScyElo getScyElo();
   public URI getTargetDescriptionUri();
   public URI getAssignmentUri();
   public URI getResourcesUri();
   public String getIconType();
   public ColorSchemeId getColorSchemeId();
   
   public void setLas(Las las);
   public void setInputMissionAnchors(List<MissionAnchor> inputMissionAnchors);
   public void setExisting(boolean existing);
   public void setAssignmentUri(URI uri);
   public void setScyElo(ScyElo scyElo);
   
}
