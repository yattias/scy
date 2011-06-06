package eu.scy.common.mission;

import java.net.URI;
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
   
}
