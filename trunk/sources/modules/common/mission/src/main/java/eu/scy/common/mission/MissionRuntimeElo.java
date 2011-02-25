/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.mission;

import java.net.URI;
import java.net.URISyntaxException;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import eu.scy.common.mission.impl.BasicMissionRuntimeEloContent;
import eu.scy.common.mission.impl.BasicMissionRuntimeModel;
import eu.scy.common.mission.impl.jdom.MissionRuntimeEloContentXmlUtils;
import eu.scy.common.scyelo.ContentTypedScyElo;
import eu.scy.common.scyelo.RooloServices;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.scyelo.ScyEloContentCreator;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;

/**
 * 
 * @author SikkenJ
 */
public class MissionRuntimeElo extends ContentTypedScyElo<MissionRuntimeEloContent>
{

	private static class MissionRuntimeEloContentCreator implements
				ScyEloContentCreator<MissionRuntimeEloContent>
	{

		@Override
		public MissionRuntimeEloContent createScyEloContent(ScyElo scyElo)
		{
			String xml = scyElo.getElo().getContent().getXmlString();
			if (xml == null || xml.length() == 0)
			{
				return new BasicMissionRuntimeEloContent();
			}
			try
			{
				return MissionRuntimeEloContentXmlUtils.missionRuntimeFromXml(xml);
			}
			catch (URISyntaxException ex)
			{
				throw new IllegalArgumentException("problems with the xml of the elo, uri: "
							+ scyElo.getUri(), ex);
			}
		}

		@Override
		public void updateEloContent(ContentTypedScyElo<MissionRuntimeEloContent> scyElo)
		{
			scyElo.getElo()
						.getContent()
						.setXmlString(
									MissionRuntimeEloContentXmlUtils.missionRuntimeToXml(scyElo
												.getTypedContent()));
		}
	}

	private static final MissionRuntimeEloContentCreator missionRuntimeEloContentCreator = new MissionRuntimeEloContentCreator();
	private final IMetadataKey userRunningMissionKey;
	private final IMetadataKey missionSpecificationEloKey;

	public MissionRuntimeElo(IELO elo, RooloServices rooloServices)
	{
		super(elo, rooloServices, missionRuntimeEloContentCreator, MissionEloType.MISSION_RUNTIME
					.getType());
		userRunningMissionKey = findMetadataKey(ScyRooloMetadataKeyIds.USER_RUNNING_MISSION);
		missionSpecificationEloKey = findMetadataKey(ScyRooloMetadataKeyIds.MISSION_SPECIFICATION_ELO);
	}

	public static MissionRuntimeElo loadElo(URI uri, RooloServices rooloServices)
	{
		IELO elo = rooloServices.getRepository().retrieveELO(uri);
		if (elo == null)
		{
			return null;
		}
		return new MissionRuntimeElo(elo, rooloServices);
	}

	public static MissionRuntimeElo loadLastVersionElo(URI uri, RooloServices rooloServices)
	{
		IELO elo = rooloServices.getRepository().retrieveELOLastVersion(uri);
		if (elo == null)
		{
			return null;
		}
		return new MissionRuntimeElo(elo, rooloServices);
	}

	public static MissionRuntimeElo createElo(RooloServices rooloServices)
	{
		IELO elo = rooloServices.getELOFactory().createELO();
		elo.getMetadata().getMetadataValueContainer(ScyElo.getTechnicalFormatKey(rooloServices))
					.setValue(MissionEloType.MISSION_RUNTIME.getType());
		MissionRuntimeElo scyElo = new MissionRuntimeElo(elo, rooloServices);
		return scyElo;
	}

	public void setUserRunningMission(String loginName)
	{
		getMetadata().getMetadataValueContainer(userRunningMissionKey).setValue(loginName);
	}

	public String getUserRunningMission()
	{
		return (String) getMetadata().getMetadataValueContainer(userRunningMissionKey).getValue();
	}

	public void setMissionSpecificationElo(URI uri)
	{
		getMetadata().getMetadataValueContainer(missionSpecificationEloKey).setValue(uri);
	}

	public URI getMissionSpecificationElo()
	{
		return (URI) getMetadata().getMetadataValueContainer(missionSpecificationEloKey).getValue();
	}

	public MissionRuntimeModel getMissionRuntimeModel()
	{
		MissionRuntimeEloContent missionRuntime = getTypedContent();
		MissionSpecificationElo missionSpecificationElo = null;
		if (missionRuntime.getMissionSpecificationEloUri() != null)
		{
			missionSpecificationElo = MissionSpecificationElo.loadElo(
						missionRuntime.getMissionSpecificationEloUri(), getRooloServices());
		}
		MissionModelElo missionModelElo = null;
		if (missionRuntime.getMissionMapModelEloUri() != null)
		{
			missionModelElo = MissionModelElo.loadLastVersionElo(
						missionRuntime.getMissionMapModelEloUri(), getRooloServices());
		}
		EloToolConfigsElo eloToolConfigsElo = null;
		if (missionRuntime.getEloToolConfigsEloUri() != null)
		{
			eloToolConfigsElo = EloToolConfigsElo.loadLastVersionElo(
						missionRuntime.getEloToolConfigsEloUri(), getRooloServices());
		}
		TemplateElosElo templateElosElo = null;
		if (missionRuntime.getTemplateElosEloUri() != null)
		{
			templateElosElo = TemplateElosElo.loadLastVersionElo(
						missionRuntime.getTemplateElosEloUri(), getRooloServices());
		}
		RuntimeSettingsElo runtimeSettingsElo = null;
		if (missionRuntime.getRuntimeSettingsEloUri() != null)
		{
			runtimeSettingsElo = RuntimeSettingsElo.loadLastVersionElo(
						missionRuntime.getRuntimeSettingsEloUri(), getRooloServices());
		}
		ColorSchemesElo colorSchemesElo = null;
		if (missionRuntime.getColorSchemesEloUri() != null)
		{
			colorSchemesElo = ColorSchemesElo.loadLastVersionElo(
						missionRuntime.getColorSchemesEloUri(), getRooloServices());
		}
		return new BasicMissionRuntimeModel(this, missionSpecificationElo, getRooloServices(),
					missionModelElo, eloToolConfigsElo, templateElosElo, runtimeSettingsElo,
					colorSchemesElo);
	}

}
