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

   @Override
   public String toString()
   {
      return "BasicMissionSpecificationEloContent{" + "missionMapModelEloUri="
               + missionMapModelEloUri + ",eloToolConfigsEloUri=" + eloToolConfigsEloUri
               + ",templateElosEloUri=" + templateElosEloUri + ",runtimeSettingsEloUri="
               + runtimeSettingsEloUri + '}';
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
}
