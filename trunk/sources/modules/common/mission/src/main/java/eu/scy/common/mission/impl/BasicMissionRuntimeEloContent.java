/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.mission.impl;

import java.net.URI;

import eu.scy.common.mission.MissionRuntimeEloContent;

/**
 * 
 * @author SikkenJ
 */
public class BasicMissionRuntimeEloContent implements MissionRuntimeEloContent
{

	private URI missionSpecificationEloUri;
	private URI missionMapModelEloUri;
	private URI eloToolConfigsEloUri;
	private URI templateElosEloUri;
	private URI runtimeSettingsEloUri;
	private URI ePortfolioEloUri;
	private URI pedagogicalPlanSettingsEloUri;
	private URI colorSchemesEloUri;

	@Override
	public String toString()
	{
		return "BasicMissionRuntimeEloContent{" + "missionSpecificationEloUri="
					+ missionSpecificationEloUri + ",missionMapModelEloUri=" + missionMapModelEloUri
					+ ",eloToolConfigsEloUri=" + eloToolConfigsEloUri + ",templateElosEloUri="
					+ templateElosEloUri + ",runtimeSettingsEloUri=" + runtimeSettingsEloUri
					+ ",ePortfolioEloUri=" + ePortfolioEloUri + ",pedagogicalPlanSettingsEloUri="
					+ pedagogicalPlanSettingsEloUri + ",colorSchemesEloUri="
					+ colorSchemesEloUri + '}';
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
	public URI getMissionSpecificationEloUri()
	{
		return missionSpecificationEloUri;
	}

	@Override
	public void setMissionSpecificationEloUri(URI missionSpecificationEloUri)
	{
		this.missionSpecificationEloUri = missionSpecificationEloUri;
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
	public URI getEPortfolioEloUri()
	{
		return ePortfolioEloUri;
	}

	@Override
	public void setEPortfolioEloUri(URI ePortfolioEloUri)
	{
		this.ePortfolioEloUri = ePortfolioEloUri;
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
}
