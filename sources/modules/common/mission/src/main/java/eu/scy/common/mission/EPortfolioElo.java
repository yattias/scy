package eu.scy.common.mission;

import java.net.URI;
import java.net.URISyntaxException;

import roolo.elo.api.IELO;
import eu.scy.common.mission.impl.BasicEPortfolioEloContent;
import eu.scy.common.mission.impl.jdom.EPortfolioEloContentXmlUtils;
import eu.scy.common.scyelo.ContentTypedScyElo;
import eu.scy.common.scyelo.RooloServices;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.scyelo.ScyEloContentCreator;

public class EPortfolioElo extends ContentTypedScyElo<EPortfolioEloContent>
{

   private static class EPortfoloEloContentCreator implements
            ScyEloContentCreator<EPortfolioEloContent>
   {

      @Override
      public EPortfolioEloContent createScyEloContent(ScyElo scyElo)
      {
         String xml = scyElo.getElo().getContent().getXmlString();
         if (xml == null || xml.length() == 0)
         {
            return new BasicEPortfolioEloContent();
         }
         try
         {
            return EPortfolioEloContentXmlUtils.ePortfolioEloContentFromXml(xml);
         }
         catch (URISyntaxException ex)
         {
            throw new IllegalArgumentException("problems with the xml of the elo, uri: "
                     + scyElo.getUri(), ex);
         }
      }

      @Override
      public void updateEloContent(ContentTypedScyElo<EPortfolioEloContent> scyElo)
      {
         scyElo.getElo().getContent().setXmlString(
         			EPortfolioEloContentXmlUtils.ePortfolioEloContentToXml(scyElo
                           .getTypedContent()));
      }
   }

   private static final EPortfoloEloContentCreator ePortfoloEloContentCreator = new EPortfoloEloContentCreator();

   public EPortfolioElo(IELO elo, RooloServices rooloServices)
   {
      super(elo, rooloServices, ePortfoloEloContentCreator, MissionEloType.EPORTFOLIO
               .getType());
      setTemplate(true);
   }

   public static EPortfolioElo loadElo(URI uri, RooloServices rooloServices)
   {
      IELO elo = rooloServices.getRepository().retrieveELO(uri);
      if (elo == null)
      {
         return null;
      }
      return new EPortfolioElo(elo, rooloServices);
   }

   public static EPortfolioElo loadLastVersionElo(URI uri, RooloServices rooloServices)
   {
      IELO elo = rooloServices.getRepository().retrieveELOLastVersion(uri);
      if (elo == null)
      {
         return null;
      }
      return new EPortfolioElo(elo, rooloServices);
   }

   public static EPortfolioElo createElo(RooloServices rooloServices)
   {
      IELO elo = rooloServices.getELOFactory().createELO();
      elo.getMetadata().getMetadataValueContainer(ScyElo.getTechnicalFormatKey(rooloServices))
               .setValue(MissionEloType.EPORTFOLIO.getType());
      EPortfolioElo scyElo = new EPortfolioElo(elo, rooloServices);
      return scyElo;
   }
}
