package eu.scy.common.mission.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import eu.scy.common.mission.Las;
import eu.scy.common.mission.MissionModelEloContent;

public class BasicMissionModelEloContent implements MissionModelEloContent
{
   private List<URI> loEloUris = new ArrayList<URI>();
   private List<Las> lasses = new ArrayList<Las>();
   private Las selectedLas;
   private URI missionMapBackgroundImageUri;
   private URI missionMapInstructionUri;
   private String missionMapButtonIconType;

   @Override
   public List<URI> getLoEloUris()
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

   public void setLoEloUris(List<URI> loEloUris)
   {
      assert loEloUris!=null;
      this.loEloUris = loEloUris;
   }

   public void setLasses(List<Las> lasses)
   {
      assert lasses!=null;
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

}
