package eu.scy.common.mission.impl;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import roolo.search.IQuery;
import roolo.search.ISearchResult;
import eu.scy.common.mission.ColorSchemesElo;
import eu.scy.common.mission.EloToolConfig;
import eu.scy.common.mission.EloToolConfigsElo;
import eu.scy.common.mission.EloToolConfigsEloContent;
import eu.scy.common.mission.Las;
import eu.scy.common.mission.MissionAnchor;
import eu.scy.common.mission.MissionEloType;
import eu.scy.common.mission.MissionManagement;
import eu.scy.common.mission.MissionModel;
import eu.scy.common.mission.MissionModelElo;
import eu.scy.common.mission.MissionModelEloContent;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionRuntimeModel;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.mission.MissionSpecificationEloContent;
import eu.scy.common.mission.RuntimeSettingsElo;
import eu.scy.common.mission.TemplateElosElo;
import eu.scy.common.scyelo.QueryFactory;
import eu.scy.common.scyelo.RooloServices;
import eu.scy.common.scyelo.ScyElo;

public class BasicMissionManagement implements MissionManagement
{
	private final static Logger logger = Logger.getLogger(BasicMissionManagement.class);

	private final MissionSpecificationElo missionSpecificationElo;
	private final RooloServices rooloServices;

	public BasicMissionManagement(MissionSpecificationElo missionSpecificationElo,
				RooloServices rooloServices)
	{
		super();
		this.missionSpecificationElo = missionSpecificationElo;
		this.rooloServices = rooloServices;
	}

	@Override
	public MissionRuntimeModel createMissionRuntimeModelElos(String userName)
	{
		return createMissionRuntimeModelElos(userName, false);
	}

	@Override
	public MissionRuntimeModel getMissionRuntimeModelElosOnSpecifiaction(String userName)
	{
		return createMissionRuntimeModelElos(userName, true);
	}

	/**
	 * creates the mission runtime elos, if they do not exists.
	 * 
	 * if runSpecificationElos is true, the mission runtime elos are not created, but the mission
	 * specification elos are used. This is used for authoring, so that the anchor elos itself can be
	 * seen and edited.
	 * 
	 * @param userName
	 * @param runSpecificationElos
	 * @return
	 */
	private MissionRuntimeModel createMissionRuntimeModelElos(String userName,
				boolean runSpecificationElos)
	{
		MissionRuntimeModel missionRuntimeModel = getMissionRuntimeModel(userName);
		if (missionRuntimeModel == null)
		{
			// it does not exists, so create it
			final MissionSpecificationEloContent missionSpecification = missionSpecificationElo
						.getTypedContent();
			MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.createElo(rooloServices);
			missionRuntimeElo.setTitle(missionSpecificationElo.getTitle());
			missionRuntimeElo.setDescription(missionSpecificationElo.getDescription());
			missionRuntimeElo.setMissionSpecificationEloUri(missionSpecificationElo.getUri());
			missionRuntimeElo.setUserRunningMission(userName);
			missionRuntimeElo.setAuthor(userName);
			if (!runSpecificationElos)
			{
				missionRuntimeElo.saveAsNewElo();
			}
			// create the elo tool configs
			EloToolConfigsElo eloToolConfigsElo = EloToolConfigsElo.loadElo(
						missionSpecification.getEloToolConfigsEloUri(), rooloServices);
			eloToolConfigsElo.setAuthor(userName);
			if (!runSpecificationElos)
			{
				eloToolConfigsElo.saveAsForkedElo();
			}
			// create the personal mission map model
			MissionModelElo personalMissionMapModelElo;
			if (!runSpecificationElos)
			{
				personalMissionMapModelElo = createPersonalMissionMapModelElo(userName,
							missionRuntimeElo.getUri(), missionSpecificationElo.getUri(),
							eloToolConfigsElo);
			}
			else
			{
				// misuse the specification mission map model ELO as personal mission map model ELO
				personalMissionMapModelElo = MissionModelElo.loadElo(
							missionSpecification.getMissionMapModelEloUri(), rooloServices);
			}
			// create the template elos elo
			TemplateElosElo templateElosElo = TemplateElosElo.loadElo(
						missionSpecification.getTemplateElosEloUri(), rooloServices);
			templateElosElo.setAuthor(userName);
			if (!runSpecificationElos)
			{
				templateElosElo.saveAsForkedElo();
			}
			// create an empty runtime settings elo
			RuntimeSettingsElo runtimeSettingsElo = RuntimeSettingsElo.loadElo(
						missionSpecification.getRuntimeSettingsEloUri(), rooloServices);
			if (runtimeSettingsElo != null)
			{
				runtimeSettingsElo.setTypeContent(new BasicRuntimeSettingsEloContent());
				if (!runSpecificationElos)
				{
					runtimeSettingsElo.saveAsForkedElo();
				}
			}
			else
			{
				runtimeSettingsElo = RuntimeSettingsElo.createElo(rooloServices);
				runtimeSettingsElo.setTitle(missionSpecificationElo.getTitle());
				if (!runSpecificationElos)
				{
					runtimeSettingsElo.saveAsNewElo();
				}
			}
			runtimeSettingsElo.setAuthor(userName);
			ColorSchemesElo colorSchemesElo = null;
			if (missionSpecification.getColorSchemesEloUri() != null)
			{
				if (!runSpecificationElos)
				{
					colorSchemesElo = ColorSchemesElo.loadElo(
								missionSpecification.getColorSchemesEloUri(), rooloServices);
					colorSchemesElo.setAuthor(userName);
					colorSchemesElo.saveAsForkedElo();
				}
			}

			missionRuntimeElo.setMissionSpecificationElo(missionSpecificationElo.getUri());
			missionRuntimeElo.getTypedContent().setMissionSpecificationEloUri(
						missionSpecificationElo.getUri());
			missionRuntimeElo.getTypedContent().setMissionMapModelEloUri(
						personalMissionMapModelElo.getUri());
			missionRuntimeElo.getTypedContent().setEloToolConfigsEloUri(eloToolConfigsElo.getUri());
			missionRuntimeElo.getTypedContent().setTemplateElosEloUri(templateElosElo.getUri());
			missionRuntimeElo.getTypedContent().setRuntimeSettingsEloUri(runtimeSettingsElo.getUri());
			if (colorSchemesElo != null)
			{
				missionRuntimeElo.getTypedContent().setColorSchemesEloUri(colorSchemesElo.getUri());
			}
			if (!runSpecificationElos)
			{
				ScyElo ePortfolioElo = ScyElo.createElo(MissionEloType.EPORTFOLIO.getType(),
							rooloServices);
				ePortfolioElo.setTitle(missionSpecificationElo.getTitle());
				ePortfolioElo.addAuthor(userName);
				ePortfolioElo.saveAsNewElo();
				missionRuntimeElo.getTypedContent().setEPortfolioEloUri(ePortfolioElo.getUri());
			}
			if (!runSpecificationElos)
			{
				ScyElo pedagogicalPlanSettings = ScyElo.createElo(
							MissionEloType.PADAGOGICAL_PLAN_SETTINGS.getType(), rooloServices);
				pedagogicalPlanSettings.setTitle(missionSpecificationElo.getTitle());
				pedagogicalPlanSettings.addAuthor(userName);
				pedagogicalPlanSettings.saveAsNewElo();
				missionRuntimeElo.getTypedContent().setPedagogicalPlanSettingsEloUri(
							pedagogicalPlanSettings.getUri());
			}
			if (!runSpecificationElos)
			{
				missionRuntimeElo.updateElo();
			}
			missionRuntimeModel = new BasicMissionRuntimeModel(missionRuntimeElo,
						missionSpecificationElo, rooloServices, personalMissionMapModelElo,
						eloToolConfigsElo, templateElosElo, runtimeSettingsElo, colorSchemesElo);
		}
		return missionRuntimeModel;
	}

	private MissionModelElo createPersonalMissionMapModelElo(String userName,
				URI missionRuntimeEloUri, URI missionSpecificationEloUri,
				EloToolConfigsElo eloToolConfigsElo)
	{
		final MissionSpecificationEloContent missionSpecification = missionSpecificationElo
					.getTypedContent();
		MissionModelElo missionModelElo = MissionModelElo.loadElo(
					missionSpecification.getMissionMapModelEloUri(), rooloServices);
		final MissionModel missionModel = missionModelElo.getMissionModel();
		missionModel.loadMetadata(rooloServices);
		makePersonalMissionModel(missionModel, userName, missionRuntimeEloUri,
					missionSpecificationEloUri, eloToolConfigsElo.getTypedContent());
		missionModelElo.setAuthor(userName);
		missionModelElo.saveAsForkedElo();
		return missionModelElo;
	}

	private void makePersonalMissionModel(MissionModelEloContent missionModel, String userName,
				URI missionRuntimeEloUri, URI missionSpecificationEloUri,
				EloToolConfigsEloContent eloToolConfigs)
	{
		for (Las las : missionModel.getLasses())
		{
			if (las.getMissionAnchor().isExisting())
			{
				las.setTitle(las.getMissionAnchor().getScyElo().getTitle());
			}
			makePersonalMissionAnchor(las.getMissionAnchor(), userName, missionRuntimeEloUri,
						missionSpecificationEloUri, eloToolConfigs);
			for (MissionAnchor anchor : las.getIntermediateAnchors())
			{
				makePersonalMissionAnchor(anchor, userName, missionRuntimeEloUri,
							missionSpecificationEloUri, eloToolConfigs);
			}
		}
	}

	private boolean isEmpty(String string)
	{
		return string == null || string.length() == 0;
	}

	private void makePersonalMissionAnchor(MissionAnchor missionAnchor, String userName,
				URI missionRuntimeEloUri, URI missionSpecificationEloUri,
				EloToolConfigsEloContent eloToolConfigs)
	{
		if (missionAnchor.isExisting())
		{
			EloToolConfig eloConfig = eloToolConfigs.getEloToolConfig(missionAnchor.getScyElo()
						.getTechnicalFormat());
			if (!eloConfig.isContentStatic())
			{
				missionAnchor.getScyElo().setLasId(missionAnchor.getLas().getId());
				if (!isEmpty(missionAnchor.getIconType()))
				{
					missionAnchor.getScyElo().setIconType(missionAnchor.getIconType());
				}
				if (missionAnchor.getColorSchemeId()!=null)
				{
					missionAnchor.getScyElo().setColorSchemeId(missionAnchor.getColorSchemeId());
				}
				if (missionAnchor.getAssignmentUri() != null)
				{
					missionAnchor.getScyElo().setAssignmentUri(missionAnchor.getAssignmentUri());
				}
				if (missionAnchor.getResourcesUri() != null)
				{
					missionAnchor.getScyElo().setResourcesUri(missionAnchor.getResourcesUri());
				}
				missionAnchor.getScyElo().setAuthor(userName);
				missionAnchor.getScyElo().setMissionRuntimeEloUri(missionRuntimeEloUri);
				missionAnchor.getScyElo().setMissionSpecificationEloUri(missionSpecificationEloUri);
				missionAnchor.getScyElo().setDateFirstUserSave(null);
				missionAnchor.getScyElo().setCreator(null);
				missionAnchor.getScyElo().saveAsForkedElo();
				missionAnchor.setEloUri(missionAnchor.getScyElo().getUri());
			}
		}
		else
		{
			logger.error("failed to find existing anchor elo, uri: " + missionAnchor.getEloUri());
		}
	}

	@Override
	public MissionRuntimeModel getMissionRuntimeModel(String userName)
	{
		IQuery myMissionRuntimeQuery = QueryFactory.createMissionRuntimeQuery(
					MissionEloType.MISSION_RUNTIME.getType(), userName);
		List<ISearchResult> missionRuntimeResults = rooloServices.getRepository().search(
					myMissionRuntimeQuery);
		if (missionRuntimeResults != null)
		{
			for (ISearchResult missionRuntimeResult : missionRuntimeResults)
			{
				MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadElo(
							missionRuntimeResult.getUri(), rooloServices);
				if (missionSpecificationElo.getUri().equals(
							missionRuntimeElo.getTypedContent().getMissionSpecificationEloUri()))
				{
					return new BasicMissionRuntimeModel(missionRuntimeElo, missionSpecificationElo,
								rooloServices);
				}
			}
		}
		return null;
	}

	@Override
	public List<MissionRuntimeModel> getAllMissionRuntimeModels()
	{
		IQuery allMissionRuntimeQuery = QueryFactory.createAllMissionRuntimesQuery(
					MissionEloType.MISSION_RUNTIME.getType(), missionSpecificationElo.getUri());
		List<ISearchResult> missionRuntimeResults = rooloServices.getRepository().search(
					allMissionRuntimeQuery);
		List<MissionRuntimeModel> allMissionRuntimeModels = new ArrayList<MissionRuntimeModel>();
		for (ISearchResult searchResult : missionRuntimeResults)
		{
			MissionRuntimeElo missionRuntimeElo = MissionRuntimeElo.loadElo(searchResult.getUri(),
						rooloServices);
			allMissionRuntimeModels.add(new BasicMissionRuntimeModel(missionRuntimeElo,
						missionSpecificationElo, rooloServices));
		}
		return allMissionRuntimeModels;
	}

}
