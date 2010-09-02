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
import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.scyelo.ScyEloContentCreator;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 *
 * @author SikkenJ
 */
public class TemplateElosElo extends ContentTypedScyElo<TemplateElosEloContent>
{

   private static class TemplateElosEloContentCreator implements ScyEloContentCreator<TemplateElosEloContent>
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
            throw new IllegalArgumentException("problems with the xml of the elo, uri: " + scyElo.getUri(), ex);
         }
      }

      @Override
      public void updateEloContent(ContentTypedScyElo<TemplateElosEloContent> scyElo)
      {
         scyElo.getElo().getContent().setXmlString(TemplateElosEloContentXmlUtils.templateElosEloContentToXml(scyElo.getTypedContent()));
      }
   }
   private static final TemplateElosEloContentCreator templateElosEloContentCreator = new TemplateElosEloContentCreator();

   public TemplateElosElo(IELO elo, ToolBrokerAPI tbi)
   {
      super(elo, tbi, templateElosEloContentCreator);
      verifyTechnicalFormat(MissionEloType.TEMPLATES_ELOS.getType());
   }

   public static TemplateElosElo loadElo(URI uri, ToolBrokerAPI tbi)
   {
      IELO elo = tbi.getRepository().retrieveELO(uri);
      if (elo == null)
      {
         return null;
      }
      return new TemplateElosElo(elo, tbi);
   }

   public static TemplateElosElo loadLastVersionElo(URI uri, ToolBrokerAPI tbi)
   {
      IELO elo = tbi.getRepository().retrieveELOLastVersion(uri);
      if (elo == null)
      {
         return null;
      }
      return new TemplateElosElo(elo, tbi);
   }

   public static TemplateElosElo createElo(ToolBrokerAPI tbi)
   {
      IELO elo = tbi.getELOFactory().createELO();
      elo.getMetadata().getMetadataValueContainer(ScyElo.getTechnicalFormatKey(tbi)).setValue(
               MissionEloType.TEMPLATES_ELOS.getType());
      TemplateElosElo scyElo = new TemplateElosElo(elo, tbi);
      return scyElo;
   }
}
