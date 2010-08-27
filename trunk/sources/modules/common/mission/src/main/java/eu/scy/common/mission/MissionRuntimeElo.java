/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.mission;

import java.net.URISyntaxException;

import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import eu.scy.common.mission.impl.BasicMissionRuntimeEloContent;
import eu.scy.common.mission.impl.jdom.MissionRuntimeEloContentXmlUtils;
import eu.scy.common.scyelo.ContentTypedScyElo;
import eu.scy.common.scyelo.ScyEloContentCreator;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;

/**
 *
 * @author SikkenJ
 */
public class MissionRuntimeElo extends ContentTypedScyElo<MissionRuntimeEloContent>
{

   private static class MissionRuntimeEloContentCreator implements ScyEloContentCreator<MissionRuntimeEloContent>
   {

      @Override
      public MissionRuntimeEloContent createScyEloContent(ContentTypedScyElo<MissionRuntimeEloContent> scyElo)
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

   public MissionRuntimeElo(IELO elo, IMetadataTypeManager metadataTypemanager)
   {
      super(elo, metadataTypemanager, missionRuntimeEloContentCreator);
      setTechnicalFormat(MissionEloType.MISSION_RUNTIME.getType());
      missionRunningKey = findMetadataKey(ScyRooloMetadataKeyIds.MISSION_RUNNING);
   }

   public void setMissionRunning(String userName){
      getMetatadata().getMetadataValueContainer(missionRunningKey).setValue(userName);
   }

   public String getMissionRunning(){
      return (String) getMetatadata().getMetadataValueContainer(missionRunningKey).getValue();
   }
}
