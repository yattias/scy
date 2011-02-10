package eu.scy.common.mission.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import eu.scy.common.mission.Las;
import eu.scy.common.mission.LasType;
import eu.scy.common.mission.MissionAnchor;

public class BasicLas implements Las
{
   private String Id;
   private float xPos;
   private float yPos;
   private List<URI> loEloUris = new ArrayList<URI>();
   private List<Las> nextLasses = new ArrayList<Las>();
   private List<Las> previousLasses = new ArrayList<Las>();
   private MissionAnchor missionAnchor;
   private List<MissionAnchor> intermediateAnchors = new ArrayList<MissionAnchor>();
   private List<URI> otherEloUris = new ArrayList<URI>();
   private URI instructionUri;
   private boolean existing;
   private LasType lasType;
   private String toolTip;
   private String title;
   
   private MissionAnchor selectedMissionAnchor;

   @Override
   public String getId()
   {
      return Id;
   }

   public void setId(String id)
   {
      Id = id;
   }

   public float getXPos()
   {
      return xPos;
   }

   public void setXPos(float xPos)
   {
      this.xPos = xPos;
   }

   public float getYPos()
   {
      return yPos;
   }

   public void setYPos(float yPos)
   {
      this.yPos = yPos;
   }

   public List<URI> getLoEloUris()
   {
      return loEloUris;
   }

   public void setLoEloUris(List<URI> loEloUris)
   {
      assert loEloUris!=null;
      this.loEloUris = loEloUris;
   }

   public List<Las> getNextLasses()
   {
      return nextLasses;
   }

   public void setNextLasses(List<Las> nextLasses)
   {
      assert nextLasses!=null;
      this.nextLasses = nextLasses;
   }

   public List<Las> getPreviousLasses()
   {
      return previousLasses;
   }

   public void setPreviousLasses(List<Las> previousLasses)
   {
      assert previousLasses!=null;
      this.previousLasses = previousLasses;
   }

   public MissionAnchor getMissionAnchor()
   {
      return missionAnchor;
   }

   public void setMissionAnchor(MissionAnchor missionAnchor)
   {
      this.missionAnchor = missionAnchor;
   }

   public List<MissionAnchor> getIntermediateAnchors()
   {
      return intermediateAnchors;
   }

   public void setIntermediateAnchors(List<MissionAnchor> intermediateAnchors)
   {
      assert intermediateAnchors!=null;
      this.intermediateAnchors = intermediateAnchors;
   }

   public List<URI> getOtherEloUris()
   {
      return otherEloUris;
   }

   public void setOtherEloUris(List<URI> otherEloUris)
   {
      assert otherEloUris!=null;
      this.otherEloUris = otherEloUris;
   }

   public URI getInstructionUri()
   {
      return instructionUri;
   }

   public void setInstructionUri(URI instructionUri)
   {
      this.instructionUri = instructionUri;
   }

   public boolean isExisting()
   {
      return existing;
   }

   public void setExisting(boolean existing)
   {
      this.existing = existing;
   }

   public LasType getLasType()
   {
      return lasType;
   }

   public void setLasType(LasType lasType)
   {
      this.lasType = lasType;
   }

   public String getToolTip()
   {
      return toolTip;
   }

   public void setToolTip(String toolTip)
   {
      this.toolTip = toolTip;
   }

   public String getTitle()
	{
		return title;
	}

	public void setTitle(String title)
	{
		this.title = title;
	}

	public MissionAnchor getSelectedMissionAnchor()
   {
      return selectedMissionAnchor;
   }

   public void setSelectedMissionAnchor(MissionAnchor selectedMissionAnchor)
   {
      this.selectedMissionAnchor = selectedMissionAnchor;
   }
   
}
