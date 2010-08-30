/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.mission;

import roolo.elo.api.IELO;
import eu.scy.common.mission.impl.BasicEloToolConfigsEloContent;
import eu.scy.common.mission.impl.jdom.EloToolConfigsEloContentXmlUtils;
import eu.scy.common.scyelo.ContentTypedScyElo;
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
      public EloToolConfigsEloContent createScyEloContent(ContentTypedScyElo<EloToolConfigsEloContent> scyElo)
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
      setTechnicalFormat(MissionEloType.ELO_TOOL_CONFIGURATION.getType());
   }
}
