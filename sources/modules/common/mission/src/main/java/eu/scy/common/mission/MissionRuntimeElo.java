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
import eu.scy.common.mission.impl.jdom.MissionRuntimeEloContentXmlUtils;
import eu.scy.common.scyelo.ContentTypedScyElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.scyelo.ScyEloContentCreator;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 *
 * @author SikkenJ
 */
public class MissionRuntimeElo extends ContentTypedScyElo<MissionRuntimeEloContent>
{

   private static class MissionRuntimeEloContentCreator implements ScyEloContentCreator<MissionRuntimeEloContent>
   {

      @Override
      public MissionRuntimeEloContent createScyEloContent(ScyElo scyElo)
      {
         String xml = scyElo.getElo().getContent().getXmlString();
         if (xml==null || xml.length()==0){
            return new BasicMissionRuntimeEloContent();
         }
         try
         {
            return MissionRuntimeEloContentXmlUtils.missionRuntimeFromXml(xml);
         }
         catch (URISyntaxException ex)
         {
            throw new IllegalArgumentException("problems with the xml of the elo, uri: " + scyElo.getUri(), ex);
         }
      }

      @Override
      public void updateEloContent(ContentTypedScyElo<MissionRuntimeEloContent> scyElo)
      {
         scyElo.getElo().getContent().setXmlString(MissionRuntimeEloContentXmlUtils.missionRuntimeToXml(scyElo.getTypedContent()));
      }
   }
   private static final MissionRuntimeEloContentCreator missionRuntimeEloContentCreator = new MissionRuntimeEloContentCreator();
   private final IMetadataKey missionRunningKey;
   private final IMetadataKey missionSpecificationEloKey;

   public MissionRuntimeElo(IELO elo, ToolBrokerAPI tbi)
   {
      super(elo, tbi, missionRuntimeEloContentCreator);
      verifyTechnicalFormat(MissionEloType.MISSION_RUNTIME.getType());
      missionRunningKey = findMetadataKey(ScyRooloMetadataKeyIds.MISSION_RUNNING);
      missionSpecificationEloKey = findMetadataKey(ScyRooloMetadataKeyIds.MISSION_SPECIFICATION_ELO);
   }

   public static MissionRuntimeElo loadElo(URI uri, ToolBrokerAPI tbi)
   {
      IELO elo = tbi.getRepository().retrieveELO(uri);
      if (elo == null)
      {
         return null;
      }
      return new MissionRuntimeElo(elo, tbi);
   }

   public static MissionRuntimeElo loadLastVersionElo(URI uri, ToolBrokerAPI tbi)
   {
      IELO elo = tbi.getRepository().retrieveELOLastVersion(uri);
      if (elo == null)
      {
         return null;
      }
      return new MissionRuntimeElo(elo, tbi);
   }

   public static MissionRuntimeElo createElo(ToolBrokerAPI tbi)
   {
      IELO elo = tbi.getELOFactory().createELO();
      elo.getMetadata().getMetadataValueContainer(ScyElo.getTechnicalFormatKey(tbi)).setValue(
               MissionEloType.MISSION_RUNTIME.getType());
      MissionRuntimeElo scyElo = new MissionRuntimeElo(elo, tbi);
      return scyElo;
   }

   public void setMissionRunning(String userName){
      getMetadata().getMetadataValueContainer(missionRunningKey).setValue(userName);
   }

   public String getMissionRunning(){
      return (String) getMetadata().getMetadataValueContainer(missionRunningKey).getValue();
   }
   
   public void setMissionSpecificationElo(URI uri){
      getMetadata().getMetadataValueContainer(missionSpecificationEloKey).setValue(uri);
   }

   public URI getMissionSpecificationElo(){
      return (URI) getMetadata().getMetadataValueContainer(missionSpecificationEloKey).getValue();
   }
}
