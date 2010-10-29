package eu.scy.common.mission;

import java.net.URI;
import java.util.List;

public interface MissionModel extends MissionModelEloContent
{
   public List<URI> getEloUris(boolean allElos);
   
   public void updateElo();
}
