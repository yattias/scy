/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.scy.common.mission;

import eu.scy.common.mission.impl.BasicMissionSpecificationEloContent;
import eu.scy.common.mission.impl.jdom.MissionSpecificationEloContentXmlUtils;
import eu.scy.common.scyelo.ContentTypedScyElo;
import eu.scy.common.scyelo.ScyEloContentCreator;
import java.net.URISyntaxException;
import roolo.elo.api.IELO;
import roolo.elo.api.IMetadataTypeManager;

/**
 *
 * @author SikkenJ
 */
public class MissionSpecificationElo extends ContentTypedScyElo<MissionSpecificationEloContent>
{

   private static class MissionSpecificationEloContentCreator implements ScyEloContentCreator<MissionSpecificationEloContent>
   {

      @Override
      public MissionSpecificationEloContent createScyEloContent(ContentTypedScyElo<MissionSpecificationEloContent> scyElo)
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

   public MissionSpecificationElo(IELO elo, IMetadataTypeManager metadataTypemanager)
   {
      super(elo, metadataTypemanager, missionSpecificationEloContentCreator);
      setTechnicalFormat(MissionEloType.MISSION_SPECIFICATIOM.getType());
   }
}
