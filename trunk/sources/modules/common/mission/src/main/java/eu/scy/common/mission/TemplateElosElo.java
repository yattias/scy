/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.mission;

import java.net.URI;
import java.net.URISyntaxException;

import roolo.elo.api.IELO;
import eu.scy.common.mission.impl.BasicTemplateElosEloContent;
import eu.scy.common.mission.impl.jdom.TemplateElosEloContentXmlUtils;
import eu.scy.common.scyelo.ContentTypedScyElo;
import eu.scy.common.scyelo.RooloServices;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.scyelo.ScyEloContentCreator;

/**
 * 
 * @author SikkenJ
 */
public class TemplateElosElo extends ContentTypedScyElo<TemplateElosEloContent>
{

   private static class TemplateElosEloContentCreator implements
            ScyEloContentCreator<TemplateElosEloContent>
   {

      @Override
      public TemplateElosEloContent createScyEloContent(ScyElo scyElo)
      {
         String xml = scyElo.getElo().getContent().getXmlString();
         if (xml == null || xml.length() == 0)
         {
            return new BasicTemplateElosEloContent();
         }
         try
         {
            return TemplateElosEloContentXmlUtils.templateElosEloContentFromXml(xml);
         }
         catch (URISyntaxException ex)
         {
            throw new IllegalArgumentException("problems with the xml of the elo, uri: "
                     + scyElo.getUri(), ex);
         }
      }

      @Override
      public void updateEloContent(ContentTypedScyElo<TemplateElosEloContent> scyElo)
      {
         scyElo.getElo().getContent().setXmlString(
                  TemplateElosEloContentXmlUtils.templateElosEloContentToXml(scyElo
                           .getTypedContent()));
      }
   }

   private static final TemplateElosEloContentCreator templateElosEloContentCreator = new TemplateElosEloContentCreator();

   public TemplateElosElo(IELO elo, RooloServices rooloServices)
   {
      super(elo, rooloServices, templateElosEloContentCreator, MissionEloType.TEMPLATES_ELOS
               .getType());
      setTemplate(true);
   }

   public static TemplateElosElo loadElo(URI uri, RooloServices rooloServices)
   {
      IELO elo = rooloServices.getRepository().retrieveELO(uri);
      if (elo == null)
      {
         return null;
      }
      return new TemplateElosElo(elo, rooloServices);
   }

   public static TemplateElosElo loadLastVersionElo(URI uri, RooloServices rooloServices)
   {
      IELO elo = rooloServices.getRepository().retrieveELOLastVersion(uri);
      if (elo == null)
      {
         return null;
      }
      return new TemplateElosElo(elo, rooloServices);
   }

   public static TemplateElosElo createElo(RooloServices rooloServices)
   {
      IELO elo = rooloServices.getELOFactory().createELO();
      elo.getMetadata().getMetadataValueContainer(ScyElo.getTechnicalFormatKey(rooloServices))
               .setValue(MissionEloType.TEMPLATES_ELOS.getType());
      TemplateElosElo scyElo = new TemplateElosElo(elo, rooloServices);
      return scyElo;
   }
}
