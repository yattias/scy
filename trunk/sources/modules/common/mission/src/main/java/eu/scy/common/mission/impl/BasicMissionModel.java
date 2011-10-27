package eu.scy.common.mission.impl;

import eu.scy.common.mission.ArchivedElo;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import roolo.elo.api.IMetadata;

import eu.scy.common.mission.Las;
import eu.scy.common.mission.MissionAnchor;
import eu.scy.common.mission.MissionModel;
import eu.scy.common.mission.MissionModelElo;
import eu.scy.common.mission.MissionModelEloContent;
import eu.scy.common.mission.UriScyElo;
import eu.scy.common.scyelo.MultiScyEloLoader;
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
   public List<UriScyElo> getLoEloUris()
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
   public URI getMissionMapBackgroundImageUri()
   {
      return missionModelEloContent.getMissionMapBackgroundImageUri();
   }

   @Override
   public String getMissionMapButtonIconType()
   {
      return missionModelEloContent.getMissionMapButtonIconType();
   }

   @Override
   public URI getMissionMapInstructionUri()
   {
      return missionModelEloContent.getMissionMapInstructionUri();
   }

   @Override
   public List<URI> getEloUris(boolean allElos)
   {
      return missionModelEloContent.getEloUris(allElos);
   }

   @Override
   public void loadMetadata(RooloServices rooloServices)
   {
      MultiScyEloLoader multiScyEloLoader = new MultiScyEloLoader(getAllUris(), true, rooloServices);
      for (Las las : missionModelEloContent.getLasses())
      {
         loadMetadata(las, multiScyEloLoader);
      }
      loadMetadata(getLoEloUris(), multiScyEloLoader);
   }

   private List<URI> getAllUris()
   {
      List<URI> uris = new ArrayList<URI>();

      for (Las las : missionModelEloContent.getLasses())
      {
         addLasUris(las, uris);
      }
      uris.addAll(UriScyElo.getUris(getLoEloUris()));
      return uris;
   }

   private void addLasUris(Las las, List<URI> uris)
   {
      addMissionAnchorUris(las.getMissionAnchor(), uris);
      for (MissionAnchor intermediateAnchor : las.getIntermediateAnchors())
      {
         addMissionAnchorUris(intermediateAnchor, uris);
      }
      uris.addAll(UriScyElo.getUris(las.getLoEloUris()));
      uris.addAll(UriScyElo.getUris(las.getOtherEloUris()));
   }

   private void addMissionAnchorUris(MissionAnchor missionAnchor, List<URI> uris)
   {
      uris.add(missionAnchor.getEloUri());
      uris.addAll(UriScyElo.getUris(missionAnchor.getLoEloUris()));
   }

   private void loadMetadata(Las las, MultiScyEloLoader multiScyEloLoader)
   {
      loadMetadata(las.getMissionAnchor(), multiScyEloLoader);
      las.setExisting(las.getMissionAnchor().isExisting());
      for (MissionAnchor intermediateAnchor : las.getIntermediateAnchors())
      {
         loadMetadata(intermediateAnchor, multiScyEloLoader);
      }
      loadMetadata(las.getLoEloUris(), multiScyEloLoader);
      loadMetadata(las.getOtherEloUris(), multiScyEloLoader);
   }

   private void loadMetadata(MissionAnchor missionAnchor, MultiScyEloLoader multiScyEloLoader)
   {
      final ScyElo scyElo = multiScyEloLoader.getScyElo(missionAnchor.getEloUri());
      if (scyElo != null)
      {
         missionAnchor.setScyElo(scyElo);
         missionAnchor.setExisting(true);
         loadMetadata(missionAnchor.getLoEloUris(), multiScyEloLoader);
      }
      else
      {
         missionAnchor.setExisting(false);
         logger.warn("failed to load metadata for mission anchor: " + missionAnchor.getEloUri());
      }
   }

   private void loadMetadata(List<UriScyElo> uriScyElos, MultiScyEloLoader multiScyEloLoader)
   {
      for (UriScyElo uriScyElo : uriScyElos)
      {
         final ScyElo scyElo = multiScyEloLoader.getScyElo(uriScyElo.getUri());
         if (scyElo != null)
         {
            uriScyElo.setScyElo(scyElo);
         }
         else
         {
            logger.warn("failed to load metadata for elo: " + uriScyElo.getUri());
         }
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

   @Override
   public String getWindowStatesXml(String lasId)
   {
      return missionModelEloContent.getWindowStatesXml(lasId);
   }

   @Override
   public void setWindowStatesXml(String lasId, String xml)
   {
      missionModelEloContent.setWindowStatesXml(lasId, xml);
   }

   @Override
   public Collection<String> getWindowStatesXmlIds()
   {
      return missionModelEloContent.getWindowStatesXmlIds();
   }

   @Override
   public List<MissionAnchor> getMissionAnchors()
   {
      return missionModelEloContent.getMissionAnchors();
   }

   @Override
   public MissionAnchor getMissionAnchor(String id)
   {
      return missionModelEloContent.getMissionAnchor(id);
   }

   @Override
   public MissionAnchor getMissionAnchor(URI eloUri)
   {
      return missionModelEloContent.getMissionAnchor(eloUri);
   }

   @Override
   public void addArchivedElo(ArchivedElo archivedElo)
   {
      missionModelEloContent.addArchivedElo(archivedElo);
   }

   @Override
   public List<ArchivedElo> getArchivedElos()
   {
      return missionModelEloContent.getArchivedElos();
   }

   @Override
   public void removeArchivedElo(ArchivedElo archivedElo)
   {
      missionModelEloContent.removeArchivedElo(archivedElo);
   }
}
