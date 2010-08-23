/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.mission;

import eu.scy.common.mission.impl.jdom.MissionRuntimeEloContentXmlUtils;
import eu.scy.common.scyelo.ContentTypedScyElo;
import eu.scy.common.scyelo.ScyEloContentCreator;
import java.net.URISyntaxException;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataTypeManager;

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
         try
         {
            return MissionRuntimeEloContentXmlUtils.missionRuntimeFromXml(scyElo.getElo().getContent().getXmlString());
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

   public MissionRuntimeElo(IELO elo, IMetadataTypeManager metadataTypemanager)
   {
      super(elo, metadataTypemanager, missionRuntimeEloContentCreator);
   }
}
