/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.mission;

import java.net.URI;
import java.net.URISyntaxException;

import roolo.elo.api.IELO;
import eu.scy.common.mission.impl.BasicMissionSpecificationEloContent;
import eu.scy.common.mission.impl.jdom.MissionSpecificationEloContentXmlUtils;
import eu.scy.common.scyelo.ContentTypedScyElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.scyelo.ScyEloContentCreator;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 *
 * @author SikkenJ
 */
public class MissionSpecificationElo extends ContentTypedScyElo<MissionSpecificationEloContent>
{

   private static class MissionSpecificationEloContentCreator implements ScyEloContentCreator<MissionSpecificationEloContent>
   {

      @Override
      public MissionSpecificationEloContent createScyEloContent(ScyElo scyElo)
      {
         String xml = scyElo.getElo().getContent().getXmlString();
         if (xml==null || xml.length()==0){
            return new BasicMissionSpecificationEloContent();
         }
         try
         {
            return MissionSpecificationEloContentXmlUtils.missionSpecificationFromXml(xml);
         }
         catch (URISyntaxException ex)
         {
            throw new IllegalArgumentException("problems with the xml of the elo, uri: " + scyElo.getUri(), ex);
         }
      }

      @Override
      public void updateEloContent(ContentTypedScyElo<MissionSpecificationEloContent> scyElo)
      {
         scyElo.getElo().getContent().setXmlString(MissionSpecificationEloContentXmlUtils.missionSpecificationToXml(scyElo.getTypedContent()));
      }
   }
   private final static MissionSpecificationEloContentCreator missionSpecificationEloContentCreator = new MissionSpecificationEloContentCreator();

   public MissionSpecificationElo(IELO elo, ToolBrokerAPI tbi)
   {
      super(elo, tbi, missionSpecificationEloContentCreator);
      verifyTechnicalFormat(MissionEloType.MISSION_SPECIFICATIOM.getType());
   }

   public static MissionSpecificationElo loadElo(URI uri, ToolBrokerAPI tbi)
   {
      IELO elo = tbi.getRepository().retrieveELO(uri);
      if (elo == null)
      {
         return null;
      }
      return new MissionSpecificationElo(elo, tbi);
   }

   public static MissionSpecificationElo loadLastVersionElo(URI uri, ToolBrokerAPI tbi)
   {
      IELO elo = tbi.getRepository().retrieveELOLastVersion(uri);
      if (elo == null)
      {
         return null;
      }
      return new MissionSpecificationElo(elo, tbi);
   }

   public static MissionSpecificationElo createElo(ToolBrokerAPI tbi)
   {
      IELO elo = tbi.getELOFactory().createELO();
      elo.getMetadata().getMetadataValueContainer(ScyElo.getTechnicalFormatKey(tbi)).setValue(
               MissionEloType.MISSION_SPECIFICATIOM.getType());
      MissionSpecificationElo scyElo = new MissionSpecificationElo(elo, tbi);
      return scyElo;
   }
}
