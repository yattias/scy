package eu.scy.client.desktop.scydesktop.mission;

import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionSpecificationElo;
import java.util.ArrayList;
import java.util.List;

public class Missions
{

   List<MissionRuntimeElo> missionRuntimeElos;
   List<MissionSpecificationElo> missionSpecificationElos;

   public Missions()
   {
      missionRuntimeElos = new ArrayList<MissionRuntimeElo>();
      missionSpecificationElos = new ArrayList<MissionSpecificationElo>();
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

   public MissionRuntimeElo findMissionRuntimeEloByTitle(String title)
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

   public MissionSpecificationElo findMissionSpecificationEloByTitle(String title)
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
}
