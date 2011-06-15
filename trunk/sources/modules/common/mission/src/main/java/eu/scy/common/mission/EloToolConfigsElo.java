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
import eu.scy.common.scyelo.RooloServices;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.scyelo.ScyEloContentCreator;

/**
 * 
 * @author SikkenJ
 */
public class EloToolConfigsElo extends ContentTypedScyElo<EloToolConfigsEloContent>
{

   private static class EloToolConfigsEloContentCreator implements
            ScyEloContentCreator<EloToolConfigsEloContent>
   {

      @Override
      public EloToolConfigsEloContent createScyEloContent(ScyElo scyElo)
      {
         String xml = scyElo.getElo().getContent().getXmlString();
         if (xml == null || xml.length() == 0)
         {
            return new BasicEloToolConfigsEloContent();
         }
         return EloToolConfigsEloContentXmlUtils.eloToolConfigsFromXml(xml);
      }

      @Override
      public void updateEloContent(ContentTypedScyElo<EloToolConfigsEloContent> scyElo)
      {
         scyElo.getElo().getContent().setXmlString(
                  EloToolConfigsEloContentXmlUtils.eloToolConfigsToXml(scyElo.getTypedContent()));
      }
   }

   private static final EloToolConfigsEloContentCreator eloToolConfigsEloContentCreator = new EloToolConfigsEloContentCreator();

   public EloToolConfigsElo(IELO elo, RooloServices rooloServices)
   {
      super(elo, rooloServices, eloToolConfigsEloContentCreator,
               MissionEloType.ELO_TOOL_CONFIGURATION.getType());
      setTemplate(true);
   }

   public static EloToolConfigsElo loadElo(URI uri, RooloServices rooloServices)
   {
      IELO elo = rooloServices.getRepository().retrieveELO(uri);
      if (elo == null)
      {
         return null;
      }
      return new EloToolConfigsElo(elo, rooloServices);
   }

   public static EloToolConfigsElo loadLastVersionElo(URI uri, RooloServices rooloServices)
   {
      IELO elo = rooloServices.getRepository().retrieveELOLastVersion(uri);
      if (elo == null)
      {
         return null;
      }
      return new EloToolConfigsElo(elo, rooloServices);
   }

   public static EloToolConfigsElo createElo(RooloServices rooloServices)
   {
      IELO elo = rooloServices.getELOFactory().createELO();
      elo.getMetadata().getMetadataValueContainer(ScyElo.getTechnicalFormatKey(rooloServices))
               .setValue(MissionEloType.ELO_TOOL_CONFIGURATION.getType());
      EloToolConfigsElo scyElo = new EloToolConfigsElo(elo, rooloServices);
      return scyElo;
   }
}
