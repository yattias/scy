package eu.scy.common.mission;

import java.net.URI;
import java.util.List;

public interface Las
{
   public String getId();
   
   public float getXPos();
   public float getYPos();
   public List<URI> getLoEloUris();
   public List<Las> getNextLasses();
   public List<Las> getPreviousLasses();
   public MissionAnchor getMissionAnchor();
   public List<MissionAnchor> getIntermediateAnchors();
   public List<URI> getOtherEloUris();
   public URI getInstructionUri();
   public boolean isExisting();
   public LasType getLasType();
   public String getToolTip();
   
   public MissionAnchor getSelectedMissionAnchor();
   public void setSelectedMissionAnchor(MissionAnchor missionAnchor);

   public void setNextLasses(List<Las> nextLasses);
   public void setOtherEloUris(List<URI> otherEloUris);
   public void setXPos(float x);
   public void setYPos(float y);
   public void setExisting(boolean existing);
   
}
