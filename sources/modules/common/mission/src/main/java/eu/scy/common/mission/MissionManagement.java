package eu.scy.common.mission;

import java.net.URI;
import java.util.List;

public interface MissionManagement
{
   public MissionRuntimeModel createMissionRuntimeModelElos(String userName);

   public MissionRuntimeModel getMissionRuntimeModelElosOnSpecifiaction(String userName);

   public MissionRuntimeModel getMissionRuntimeModel(String userName);
   
   public List<MissionRuntimeModel> getAllMissionRuntimeModels();

   public URI getAnchorEloUriForElo(URI eloUri);
}
