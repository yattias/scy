/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.mission;

import java.net.URI;

import roolo.elo.api.IELO;
import eu.scy.common.mission.impl.BasicEloToolConfigsEloContent;
import eu.scy.common.mission.impl.jdom.EloToolConfigsEloContentXmlUtils;
import eu.scy.common.scyelo.ContentTypedScyElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.scyelo.ScyEloContentCreator;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 *
 * @author SikkenJ
 */
public class EloToolConfigsElo extends ContentTypedScyElo<EloToolConfigsEloContent>
{

   private static class EloToolConfigsEloContentCreator implements ScyEloContentCreator<EloToolConfigsEloContent>
   {

      @Override
      public EloToolConfigsEloContent createScyEloContent(ScyElo scyElo)
      {
         String xml = scyElo.getElo().getContent().getXmlString();
         if (xml==null || xml.length()==0){
            return new BasicEloToolConfigsEloContent();
         }
         return EloToolConfigsEloContentXmlUtils.eloToolConfigsFromXml(xml);
      }

      @Override
      public void updateEloContent(ContentTypedScyElo<EloToolConfigsEloContent> scyElo)
      {
         scyElo.getElo().getContent().setXmlString(EloToolConfigsEloContentXmlUtils.eloToolConfigsToXml(scyElo.getTypedContent()));
      }
   }
   private static final EloToolConfigsEloContentCreator eloToolConfigsEloContentCreator = new EloToolConfigsEloContentCreator();

   public EloToolConfigsElo(IELO elo, ToolBrokerAPI tbi)
   {
      super(elo, tbi, eloToolConfigsEloContentCreator);
      verifyTechnicalFormat(MissionEloType.ELO_TOOL_CONFIGURATION.getType());
   }

   public static EloToolConfigsElo loadElo(URI uri, ToolBrokerAPI tbi)
   {
      IELO elo = tbi.getRepository().retrieveELO(uri);
      if (elo == null)
      {
         return null;
      }
      return new EloToolConfigsElo(elo, tbi);
   }

   public static EloToolConfigsElo loadLastVersionElo(URI uri, ToolBrokerAPI tbi)
   {
      IELO elo = tbi.getRepository().retrieveELOLastVersion(uri);
      if (elo == null)
      {
         return null;
      }
      return new EloToolConfigsElo(elo, tbi);
   }

   public static EloToolConfigsElo createElo(ToolBrokerAPI tbi)
   {
      IELO elo = tbi.getELOFactory().createELO();
      elo.getMetadata().getMetadataValueContainer(ScyElo.getTechnicalFormatKey(tbi)).setValue(
               MissionEloType.ELO_TOOL_CONFIGURATION.getType());
      EloToolConfigsElo scyElo = new EloToolConfigsElo(elo, tbi);
      return scyElo;
   }
}
