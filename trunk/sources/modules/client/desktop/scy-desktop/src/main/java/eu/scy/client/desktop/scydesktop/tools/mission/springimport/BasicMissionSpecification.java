/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.client.desktop.scydesktop.tools.mission.springimport;

import java.net.URI;

/**
 *
 * @author SikkenJ
 */
public class BasicMissionSpecification implements MissionSpecification
{

   private URI missionMapModelEloUri;
   private URI eloToolConfigsEloUri;

   @Override
   public URI getEloToolConfigsEloUri()
   {
      return eloToolConfigsEloUri;
   }

   public void setEloToolConfigsEloUri(URI eloToolConfigsEloUri)
   {
      this.eloToolConfigsEloUri = eloToolConfigsEloUri;
   }

   @Override
   public URI getMissionMapModelEloUri()
   {
      return missionMapModelEloUri;
   }

   public void setMissionMapModelEloUri(URI missionMapModelEloUri)
   {
      this.missionMapModelEloUri = missionMapModelEloUri;
   }
}
