package eu.scy.common.mission;

public interface MissionManagement
{
   public MissionRuntimeModel createMissionRuntimeModelElos(String userName);

   public MissionRuntimeModel getMissionRuntimeModelElosOnSpecifiaction(String userName);

   public MissionRuntimeModel getMissionRuntimeModel(String userName);

}
