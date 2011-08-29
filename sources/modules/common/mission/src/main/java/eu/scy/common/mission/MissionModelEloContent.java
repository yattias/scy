package eu.scy.common.mission;

import java.net.URI;
import java.util.Collection;
import java.util.List;

public interface MissionModelEloContent
{
   public List<URI> getLoEloUris();

   public List<Las> getLasses();

   public Las getSelectedLas();

   public void setSelectedLas(Las selectedLas);
   
   public URI getMissionMapBackgroundImageUri();

   public URI getMissionMapInstructionUri();

   public String getMissionMapButtonIconType();

   public String getWindowStatesXml(String lasId);

   public void setWindowStatesXml(String lasId, String xml);

   public Collection<String> getWindowStatesXmlIds();
}
