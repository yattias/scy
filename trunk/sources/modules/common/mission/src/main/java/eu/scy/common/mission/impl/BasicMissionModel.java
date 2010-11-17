package eu.scy.common.mission.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import eu.scy.common.mission.Las;
import eu.scy.common.mission.MissionAnchor;
import eu.scy.common.mission.MissionModel;
import eu.scy.common.mission.MissionModelElo;
import eu.scy.common.mission.MissionModelEloContent;
import eu.scy.common.scyelo.RooloServices;
import eu.scy.common.scyelo.ScyElo;

public class BasicMissionModel implements MissionModel
{
   private final static Logger logger = Logger.getLogger(BasicMissionModel.class);
   
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
      Set<URI> allEloUris = new HashSet<URI>();
      if (allElos)
      {
         addAllUris(allEloUris, missionModelEloContent.getLoEloUris());
      }
      for (Las las : missionModelEloContent.getLasses())
      {
         if (allElos)
         {
            addAllUris(allEloUris, las.getLoEloUris());
         }
         addMissionAnchorEloUris(allEloUris, las.getMissionAnchor(), allElos);
         if (las.getIntermediateAnchors() != null)
         {
            for (MissionAnchor intermediateAnchor : las.getIntermediateAnchors())
            {
               addMissionAnchorEloUris(allEloUris, intermediateAnchor, allElos);
            }
         }
      }
      return new ArrayList<URI>(allEloUris);
   }

   private void addAllUris(Set<URI> allEloUris, List<URI> eloUris)
   {
      if (eloUris != null)
      {
         allEloUris.addAll(eloUris);
      }
   }

   private void addMissionAnchorEloUris(Set<URI> allEloUris, MissionAnchor missionAnchor,
            boolean allElos)
   {
      if (missionAnchor.getEloUri() != null)
      {
         allEloUris.add(missionAnchor.getEloUri());
         if (allElos)
         {
            addAllUris(allEloUris, missionAnchor.getLoEloUris());
         }
      }
   }

   @Override
   public void loadMetadata(RooloServices rooloServices)
   {
      for (Las las : missionModelEloContent.getLasses())
      {
         loadMetadata(las.getMissionAnchor(), rooloServices);
         las.setExisting(las.getMissionAnchor().isExisting());
         for (MissionAnchor intermediateAnchor : las.getIntermediateAnchors())
         {
            loadMetadata(intermediateAnchor, rooloServices);
         }
      }
   }

   private void loadMetadata(MissionAnchor missionAnchor, RooloServices rooloServices)
   {
      ScyElo scyElo = ScyElo.loadMetadata(missionAnchor.getEloUri(), rooloServices);
      missionAnchor.setScyElo(scyElo);
      missionAnchor.setExisting(scyElo != null);
      if (scyElo==null){
         logger.warn("failed to load metadata for mission anchor: " + missionAnchor.getEloUri());
      }
   }
   
   @Override
   public void updateElo()
   {
      missionModelElo.updateElo();
   }

   @Override
   public MissionModelElo getMissionModelElo()
   {
      return missionModelElo;
   }

}
