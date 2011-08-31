package eu.scy.common.mission.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.log4j.Logger;

import roolo.elo.api.IMetadata;

import eu.scy.common.mission.Las;
import eu.scy.common.mission.MissionAnchor;
import eu.scy.common.mission.MissionModel;
import eu.scy.common.mission.MissionModelElo;
import eu.scy.common.mission.MissionModelEloContent;
import eu.scy.common.scyelo.RooloServices;
import eu.scy.common.scyelo.ScyElo;

public class BasicMissionModel implements MissionModel
{
	private final static Logger logger = Logger.getLogger(BasicMissionModel.class);

	private MissionModelElo missionModelElo;
	private MissionModelEloContent missionModelEloContent;

	public BasicMissionModel(MissionModelElo missionModelElo)
	{
		super();
		this.missionModelElo = missionModelElo;
		missionModelEloContent = missionModelElo.getTypedContent();
	}

	@Override
	public List<Las> getLasses()
	{
		return missionModelEloContent.getLasses();
	}

	@Override
	public List<URI> getLoEloUris()
	{
		return missionModelEloContent.getLoEloUris();
	}

	@Override
	public Las getSelectedLas()
	{
		return missionModelEloContent.getSelectedLas();
	}

	@Override
	public void setSelectedLas(Las selectedLas)
	{
		missionModelEloContent.setSelectedLas(selectedLas);
	}

	@Override
	public URI getMissionMapBackgroundImageUri()
	{
		return missionModelEloContent.getMissionMapBackgroundImageUri();
	}

   @Override
   public String getMissionMapButtonIconType()
   {
      return missionModelEloContent.getMissionMapButtonIconType();
   }

   @Override
   public URI getMissionMapInstructionUri()
   {
      return missionModelEloContent.getMissionMapInstructionUri();
   }

	@Override
	public List<URI> getEloUris(boolean allElos)
	{
		Set<URI> allEloUris = new HashSet<URI>();
		if (allElos)
		{
			addAllUris(allEloUris, missionModelEloContent.getLoEloUris());
		}
		for (Las las : missionModelEloContent.getLasses())
		{
			if (allElos)
			{
				addAllUris(allEloUris, las.getLoEloUris());
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

	private void addMissionAnchorEloUris(Set<URI> allEloUris, MissionAnchor missionAnchor,
				boolean allElos)
	{
		if (missionAnchor.getEloUri() != null)
		{
			allEloUris.add(missionAnchor.getEloUri());
			if (allElos)
			{
				addAllUris(allEloUris, missionAnchor.getLoEloUris());
			}
		}
	}

	@Override
	public void loadMetadata(RooloServices rooloServices)
	{
		List<URI> uris = getAllUris();
		List<IMetadata> metadatas = rooloServices.getRepository().retrieveMetadatas(uris);
		// we are walking in the same sequence though the lasses
		int i = 0;
		for (Las las : missionModelEloContent.getLasses())
		{
			loadMetadata(las.getMissionAnchor(), metadatas.get(i++), rooloServices);
			las.setExisting(las.getMissionAnchor().isExisting());
			for (MissionAnchor intermediateAnchor : las.getIntermediateAnchors())
			{
				loadMetadata(intermediateAnchor, metadatas.get(i++), rooloServices);
			}
		}
	}

	private List<URI> getAllUris()
	{
		List<URI> uris = new ArrayList<URI>();
		for (Las las : missionModelEloContent.getLasses())
		{
			uris.add(las.getMissionAnchor().getEloUri());
			for (MissionAnchor intermediateAnchor : las.getIntermediateAnchors())
			{
				uris.add(intermediateAnchor.getEloUri());
			}
		}
		return uris;
	}

	private void loadMetadata(MissionAnchor missionAnchor, IMetadata metadata,
				RooloServices rooloServices)
	{
		if (metadata != null)
		{
			ScyElo scyElo = new ScyElo(metadata, rooloServices);
			missionAnchor.setScyElo(scyElo);
			missionAnchor.setExisting(true);
		}
		else
		{
			missionAnchor.setExisting(false);
			logger.warn("failed to load metadata for mission anchor: " + missionAnchor.getEloUri());
		}
	}

	private void loadMetadata(MissionAnchor missionAnchor, RooloServices rooloServices)
	{
		ScyElo scyElo = ScyElo.loadMetadata(missionAnchor.getEloUri(), rooloServices);
		missionAnchor.setScyElo(scyElo);
		missionAnchor.setExisting(scyElo != null);
		if (scyElo == null)
		{
			logger.warn("failed to load metadata for mission anchor: " + missionAnchor.getEloUri());
		}
	}

	@Override
	public void updateElo()
	{
		missionModelElo.updateElo();
	}

	@Override
	public MissionModelElo getMissionModelElo()
	{
		return missionModelElo;
	}

   @Override
   public String getWindowStatesXml(String lasId)
   {
      return missionModelEloContent.getWindowStatesXml(lasId);
   }

   @Override
   public void setWindowStatesXml(String lasId, String xml)
   {
      missionModelEloContent.setWindowStatesXml(lasId, xml);
   }

   @Override
   public Collection<String> getWindowStatesXmlIds()
   {
      return missionModelEloContent.getWindowStatesXmlIds();
   }

   @Override
   public List<MissionAnchor> getMissionAnchors()
   {
      return missionModelEloContent.getMissionAnchors();
   }

   @Override
   public MissionAnchor getMissionAnchor(String id)
   {
      return missionModelEloContent.getMissionAnchor(id);
   }

}
