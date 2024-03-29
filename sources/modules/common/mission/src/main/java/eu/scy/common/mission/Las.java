package eu.scy.common.mission;

import java.net.URI;
import java.util.List;

public interface Las
{
   public String getId();
   
   public float getXPos();
   public float getYPos();
   public List<UriScyElo> getLoEloUris();
   public List<Las> getNextLasses();
   public List<Las> getPreviousLasses();
   public MissionAnchor getMissionAnchor();
   public List<MissionAnchor> getIntermediateAnchors();
   public List<UriScyElo> getOtherEloUris();
   public URI getInstructionUri();
   public boolean isExisting();
   public LasType getLasType();
   public String getToolTip();
   public String getTitle();
   public MissionAnchor getInitialMissionAnchorToOpen();
   
   public MissionAnchor getSelectedMissionAnchor();
   public void setSelectedMissionAnchor(MissionAnchor missionAnchor);

   public void setNextLasses(List<Las> nextLasses);
   public void setLoEloUris(List<UriScyElo> loEloUris);
   public void setOtherEloUris(List<UriScyElo> otherEloUris);
   public void setXPos(float x);
   public void setYPos(float y);
   public void setExisting(boolean existing);
   public void setTitle(String title);
   
}
