package eu.scy.common.mission;

import java.net.URI;
import java.util.List;

import eu.scy.common.scyelo.RooloServices;

public interface MissionModel extends MissionModelEloContent
{
   public void loadMetadata(RooloServices rooloServices);
   
   public List<URI> getEloUris(boolean allElos);
   
   public void updateElo();
   
   public MissionModelElo getMissionModelElo();
}
