/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.mission.impl;

import java.net.URI;

import eu.scy.common.mission.MissionSpecificationEloContent;

/**
 * 
 * @author SikkenJ
 */
public class BasicMissionSpecificationEloContent implements MissionSpecificationEloContent
{

	private URI missionMapModelEloUri;
	private URI eloToolConfigsEloUri;
	private URI templateElosEloUri;
	private URI runtimeSettingsEloUri;
	private URI agentModelsEloUri;
	private URI pedagogicalPlanSettingsEloUri;
	private URI colorSchemesEloUri;
	private URI missionDescriptionUri;

	@Override
	public String toString()
	{
		return "BasicMissionSpecificationEloContent{" + "missionMapModelEloUri="
					+ missionMapModelEloUri + ",eloToolConfigsEloUri=" + eloToolConfigsEloUri
					+ ",templateElosEloUri=" + templateElosEloUri + ",runtimeSettingsEloUri="
					+ runtimeSettingsEloUri + ",agentModelsEloUri=" + agentModelsEloUri
					+ ",pedagogicalPlanSettingsEloUri=" + pedagogicalPlanSettingsEloUri
					+ ",colorSchemesEloUri=" + colorSchemesEloUri
					+ ",missionDescriptionUri=" + missionDescriptionUri + '}';
	}

	@Override
	public URI getEloToolConfigsEloUri()
	{
		return eloToolConfigsEloUri;
	}

	@Override
	public void setEloToolConfigsEloUri(URI eloToolConfigsEloUri)
	{
		this.eloToolConfigsEloUri = eloToolConfigsEloUri;
	}

	@Override
	public URI getMissionMapModelEloUri()
	{
		return missionMapModelEloUri;
	}

	@Override
	public void setMissionMapModelEloUri(URI missionMapModelEloUri)
	{
		this.missionMapModelEloUri = missionMapModelEloUri;
	}

	@Override
	public URI getTemplateElosEloUri()
	{
		return templateElosEloUri;
	}

	@Override
	public void setTemplateElosEloUri(URI templateElosEloUri)
	{
		this.templateElosEloUri = templateElosEloUri;
	}

	@Override
	public URI getRuntimeSettingsEloUri()
	{
		return runtimeSettingsEloUri;
	}

	@Override
	public void setRuntimeSettingsEloUri(URI runtimeSettingsEloUri)
	{
		this.runtimeSettingsEloUri = runtimeSettingsEloUri;
	}

	@Override
	public URI getAgentModelsEloUri()
	{
		return agentModelsEloUri;
	}

	@Override
	public void setAgentModelsEloUri(URI agentModelsEloUri)
	{
		this.agentModelsEloUri = agentModelsEloUri;
	}

	@Override
	public URI getPedagogicalPlanSettingsEloUri()
	{
		return pedagogicalPlanSettingsEloUri;
	}

	@Override
	public void setPedagogicalPlanSettingsEloUri(URI pedagogicalPlanSettingsEloUri)
	{
		this.pedagogicalPlanSettingsEloUri = pedagogicalPlanSettingsEloUri;
	}

	@Override
	public URI getColorSchemesEloUri()
	{
		return colorSchemesEloUri;
	}

	@Override
	public void setColorSchemesEloUri(URI colorSchemesEloUri)
	{
		this.colorSchemesEloUri = colorSchemesEloUri;
	}

	@Override
	public URI getMissionDescriptionUri()
	{
		return missionDescriptionUri;
	}

	@Override
	public void setMissionDescriptionUri(URI missionDescriptionUri)
	{
		this.missionDescriptionUri = missionDescriptionUri;
	}

}
