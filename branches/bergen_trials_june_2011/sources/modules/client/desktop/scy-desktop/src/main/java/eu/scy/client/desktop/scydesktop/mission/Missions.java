package eu.scy.client.desktop.scydesktop.mission;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;

public class Missions
{

   private final static Logger logger = Logger.getLogger(Missions.class);
   List<MissionRuntimeElo> missionRuntimeElos;
   List<MissionSpecificationElo> missionSpecificationElos;

   public Missions()
   {
      missionRuntimeElos = new ArrayList<MissionRuntimeElo>();
      missionSpecificationElos = new ArrayList<MissionSpecificationElo>();
   }

   public void removeMissionSpecifications()
   {
      missionSpecificationElos.clear();
   }

   public boolean isEmpty()
   {
      return missionRuntimeElos.isEmpty() && missionSpecificationElos.isEmpty();
   }

   public int size()
   {
      return missionRuntimeElos.size() + missionSpecificationElos.size();
   }

   public MissionRuntimeElo[] getMissionRuntimeElosArray()
   {
      return missionRuntimeElos.toArray(new MissionRuntimeElo[0]);
   }

   public MissionSpecificationElo[] getMissionSpecificationElosArray()
   {
      return missionSpecificationElos.toArray(new MissionSpecificationElo[0]);
   }

   public MissionRuntimeElo findMissionRuntimeElo(String id)
   {
      if (id == null || id.length() == 0)
      {
         return null;
      }
      try
      {
         URI uri = new URI(id);
         return findMissionRuntimeEloByUri(uri);
      }
      catch (URISyntaxException ex)
      {
         logger.debug("id (" + id + ") is not a uri, assuming it is the title, " + ex.getMessage());
      }
      return findMissionRuntimeEloByTitle(id);
   }

   private MissionRuntimeElo findMissionRuntimeEloByTitle(String title)
   {
      for (MissionRuntimeElo missionRuntimeElo : missionRuntimeElos)
      {
         if (title.equalsIgnoreCase(missionRuntimeElo.getTitle()))
         {
            return missionRuntimeElo;
         }
      }
      return null;
   }

   private MissionRuntimeElo findMissionRuntimeEloByUri(URI uri)
   {
      for (MissionRuntimeElo missionRuntimeElo : missionRuntimeElos)
      {
         if (uri.equals(missionRuntimeElo.getUri()))
         {
            return missionRuntimeElo;
         }
      }
      return null;
   }

   public MissionSpecificationElo findMissionSpecificationElo(String id)
   {
      if (id == null || id.length() == 0)
      {
         return null;
      }
      try
      {
         URI uri = new URI(id);
         return findMissionSpecificationEloByUri(uri);
      }
      catch (URISyntaxException ex)
      {
         logger.debug("id (" + id + ") is not a uri, assuming it is the title, " + ex.getMessage());
      }
      return findMissionSpecificationEloByTitle(id);
   }

   private MissionSpecificationElo findMissionSpecificationEloByTitle(String title)
   {
      for (MissionSpecificationElo missionSpecificationElo : missionSpecificationElos)
      {
         if (title.equalsIgnoreCase(missionSpecificationElo.getTitle()))
         {
            return missionSpecificationElo;
         }
      }
      return null;
   }

   private MissionSpecificationElo findMissionSpecificationEloByUri(URI uri)
   {
      for (MissionSpecificationElo missionSpecificationElo : missionSpecificationElos)
      {
         if (uri.equals(missionSpecificationElo.getUri()))
         {
            return missionSpecificationElo;
         }
      }
      return null;
   }
}
