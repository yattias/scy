package eu.scy.common.mission;

import java.net.URI;
import java.util.Collection;
import java.util.List;

public interface MissionModelEloContent
{
   public List<UriScyElo> getLoEloUris();

   public List<Las> getLasses();

   public Las getSelectedLas();

   public void setSelectedLas(Las selectedLas);
   
   public URI getMissionMapBackgroundImageUri();

   public URI getMissionMapInstructionUri();

   public String getMissionMapButtonIconType();

   public String getWindowStatesXml(String lasId);

   public void setWindowStatesXml(String lasId, String xml);

   public Collection<String> getWindowStatesXmlIds();

   public List<MissionAnchor> getMissionAnchors();
   
   public MissionAnchor getMissionAnchor(String id);

   public MissionAnchor getMissionAnchor(URI eloUri);

   public List<ArchivedElo> getArchivedElos();

   public void addArchivedElo(ArchivedElo archivedElo);

   public void removeArchivedElo(ArchivedElo archivedElo);

   public List<URI> getEloUris(boolean allElos);
}
