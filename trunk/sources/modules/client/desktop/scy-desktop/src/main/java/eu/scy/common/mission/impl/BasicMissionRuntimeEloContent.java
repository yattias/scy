/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.common.mission.impl;

import eu.scy.common.mission.MissionRuntimeEloContent;
import java.net.URI;

/**
 *
 * @author SikkenJ
 */
public class BasicMissionRuntimeEloContent implements MissionRuntimeEloContent {
   private URI missionSpecificationEloUri;
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

   @Override
   public URI getMissionSpecificationEloUri()
   {
      return missionSpecificationEloUri;
   }

   public void setMissionSpecificationEloUri(URI missionSpecificationEloUri)
   {
      this.missionSpecificationEloUri = missionSpecificationEloUri;
   }

}
