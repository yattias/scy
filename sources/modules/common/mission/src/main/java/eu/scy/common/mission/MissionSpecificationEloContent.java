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
public interface MissionSpecificationEloContent
{

   public URI getMissionMapModelEloUri();

   public void setMissionMapModelEloUri(URI missionMapModelEloUri);

   public URI getEloToolConfigsEloUri();

   public void setEloToolConfigsEloUri(URI eloToolConfigsEloUri);

   public URI getTemplateElosEloUri();

   public void setTemplateElosEloUri(URI templateElosEloUri);
}
