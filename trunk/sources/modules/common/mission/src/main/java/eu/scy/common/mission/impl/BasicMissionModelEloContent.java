package eu.scy.common.mission.impl;

import eu.scy.common.mission.ArchivedElo;
import eu.scy.common.mission.MissionAnchor;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import eu.scy.common.mission.Las;
import eu.scy.common.mission.MissionModelEloContent;
import eu.scy.common.mission.UriScyElo;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

public class BasicMissionModelEloContent implements MissionModelEloContent
{

   private List<UriScyElo> loEloUris = new ArrayList<UriScyElo>();
   private List<Las> lasses = new ArrayList<Las>();
   private Las selectedLas;
   private URI missionMapBackgroundImageUri;
   private URI missionMapInstructionUri;
   private String missionMapButtonIconType;
   private Map<String, String> desktopStatesXml = new HashMap<String, String>();
   private LinkedList<ArchivedElo> archivedElos = new LinkedList<ArchivedElo>();

   @Override
   public List<UriScyElo> getLoEloUris()
   {
      return loEloUris;
   }

   @Override
   public List<Las> getLasses()
   {
      return lasses;
   }

   @Override
   public Las getSelectedLas()
   {
      return selectedLas;
   }

   @Override
   public void setSelectedLas(Las selectedLas)
   {
      this.selectedLas = selectedLas;
   }

   public void setLoEloUris(List<UriScyElo> loEloUris)
   {
      assert loEloUris != null;
      this.loEloUris = loEloUris;
   }

   public void setLasses(List<Las> lasses)
   {
      assert lasses != null;
      this.lasses = lasses;
   }

   @Override
   public URI getMissionMapBackgroundImageUri()
   {
      return missionMapBackgroundImageUri;
   }

   public void setMissionMapBackgroundImageUri(URI missionMapBackgroundImageUri)
   {
      this.missionMapBackgroundImageUri = missionMapBackgroundImageUri;
   }

   @Override
   public String getMissionMapButtonIconType()
   {
      return missionMapButtonIconType;
   }

   public void setMissionMapButtonIconType(String missionMapButtonIconType)
   {
      this.missionMapButtonIconType = missionMapButtonIconType;
   }

   @Override
   public URI getMissionMapInstructionUri()
   {
      return missionMapInstructionUri;
   }

   public void setMissionMapInstructionUri(URI missionMapInstructionUri)
   {
      this.missionMapInstructionUri = missionMapInstructionUri;
   }

   @Override
   public String getWindowStatesXml(String lasId)
   {
      return desktopStatesXml.get(lasId);
   }

   @Override
   public void setWindowStatesXml(String lasId, String xml)
   {
      desktopStatesXml.put(lasId, xml);
   }

   @Override
   public Collection<String> getWindowStatesXmlIds()
   {
      return desktopStatesXml.keySet();
   }

   @Override
   public List<MissionAnchor> getMissionAnchors()
   {
      List<MissionAnchor> missionAnchors = new ArrayList<MissionAnchor>();
      for (Las las : getLasses())
      {
         missionAnchors.add(las.getMissionAnchor());
         missionAnchors.addAll(las.getIntermediateAnchors());
      }
      return missionAnchors;
   }

   @Override
   public MissionAnchor getMissionAnchor(String id)
   {
      List<MissionAnchor> missionAnchors = getMissionAnchors();
      for (MissionAnchor missionAnchor : missionAnchors)
      {
         if (missionAnchor.getId() != null && missionAnchor.getId().equals(id))
         {
            return missionAnchor;
         }
      }
      return null;
   }

   @Override
   public MissionAnchor getMissionAnchor(URI eloUri)
   {
      List<MissionAnchor> missionAnchors = getMissionAnchors();
      for (MissionAnchor missionAnchor : missionAnchors)
      {
         if (missionAnchor.getEloUri() != null && missionAnchor.getEloUri().equals(eloUri))
         {
            return missionAnchor;
         }
      }
      return null;
   }

   @Override
   public List<ArchivedElo> getArchivedElos()
   {
      return archivedElos;
   }

   @Override
   public void addArchivedElo(ArchivedElo archivedElo)
   {
      removeArchivedElo(archivedElo);
      archivedElos.addFirst(archivedElo);
   }

   @Override
   public void removeArchivedElo(ArchivedElo archivedElo)
   {
      archivedElos.remove(archivedElo);
   }

   public void setArchivedElos(List<ArchivedElo> archivedElos)
   {
      this.archivedElos.clear();
      this.archivedElos.addAll(archivedElos);
   }

   @Override
   public List<URI> getEloUris(boolean allElos)
   {
      Set<URI> allEloUris = new HashSet<URI>();
      if (allElos)
      {
         addAllUriScyElos(allEloUris, getLoEloUris());
      }
      for (Las las : getLasses())
      {
         if (allElos)
         {
            addAllUriScyElos(allEloUris, las.getLoEloUris());
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

   private void addAllUriScyElos(Set<URI> allEloUris, List<UriScyElo> eloUris)
   {
      if (eloUris != null)
      {
         for (UriScyElo uriScyElo : getLoEloUris())
         {
            allEloUris.add(uriScyElo.getUri());
         }
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
            addAllUriScyElos(allEloUris, missionAnchor.getLoEloUris());
         }
      }
   }
}
