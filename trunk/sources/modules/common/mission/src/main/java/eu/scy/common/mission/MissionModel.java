package eu.scy.common.mission;


import eu.scy.common.scyelo.RooloServices;

public interface MissionModel extends MissionModelEloContent
{
   public void loadMetadata(RooloServices rooloServices);
   
   public void updateElo();
   
   public MissionModelElo getMissionModelElo();
}
