/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.mission;

import java.net.URISyntaxException;

import roolo.elo.api.IELO;
import eu.scy.common.mission.impl.BasicTemplateElosEloContent;
import eu.scy.common.mission.impl.jdom.TemplateElosEloContentXmlUtils;
import eu.scy.common.scyelo.ContentTypedScyElo;
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
      public TemplateElosEloContent createScyEloContent(ContentTypedScyElo<TemplateElosEloContent> scyElo)
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
      setTechnicalFormat(MissionEloType.TEMPLATES_ELOS.getType());
   }
}
