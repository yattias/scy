/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.mission;

import java.net.URI;

/**
 * 
 * @author SikkenJ
 */
public interface MissionRuntimeEloContent
{

	public URI getMissionSpecificationEloUri();

	public void setMissionSpecificationEloUri(URI missionSpecificationEloUri);

	public URI getMissionMapModelEloUri();

	public void setMissionMapModelEloUri(URI missionMapModelEloUri);

	public URI getEloToolConfigsEloUri();

	public void setEloToolConfigsEloUri(URI eloToolConfigsEloUri);

	public URI getTemplateElosEloUri();

	public void setTemplateElosEloUri(URI templateElosEloUri);

	public URI getRuntimeSettingsEloUri();

	public void setRuntimeSettingsEloUri(URI ePortfolioEloUri);

	public URI getEPortfolioEloUri();

	public void setEPortfolioEloUri(URI ePortfolioEloUri);

	public URI getPedagogicalPlanSettingsEloUri();

	public void setPedagogicalPlanSettingsEloUri(URI pedagogicalPlanSettingsEloUri);
}
