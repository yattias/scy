package eu.scy.common.mission.impl;

import java.net.URI;
import java.util.List;

import eu.scy.common.mission.Las;
import eu.scy.common.mission.MissionModel;
import eu.scy.common.mission.MissionModelElo;
import eu.scy.common.mission.MissionModelEloContent;

public class BasicMissionModel implements MissionModel
{
   private MissionModelElo missionModelElo;
   private MissionModelEloContent missionModelEloContent;
   

   public BasicMissionModel(MissionModelElo missionModelElo)
   {
      super();
      this.missionModelElo = missionModelElo;
      missionModelEloContent = missionModelElo.getTypedContent();
   }

   @Override
   public List<Las> getLasses()
   {
      return missionModelEloContent.getLasses();
   }

   @Override
   public List<URI> getLoEloUris()
   {
      return missionModelEloContent.getLoEloUris();
   }

   @Override
   public String getName()
   {
      return missionModelEloContent.getName();
   }

   @Override
   public Las getSelectedLas()
   {
      return missionModelEloContent.getSelectedLas();
   }

   @Override
   public void setSelectedLas(Las selectedLas)
   {
      missionModelEloContent.setSelectedLas(selectedLas);
   }

   @Override
   public List<URI> getEloUris(boolean allElos)
   {
      // TODO Auto-generated method stub
      return null;
   }

   @Override
   public void updateElo()
   {
      // TODO Auto-generated method stub
      
   }
   
}
