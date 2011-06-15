package eu.scy.common.mission;

import java.net.URI;
import java.net.URISyntaxException;

import roolo.elo.api.IELO;
import eu.scy.common.mission.impl.BasicMissionModel;
import eu.scy.common.mission.impl.BasicMissionModelEloContent;
import eu.scy.common.mission.impl.jdom.MissionModelEloContentXmlUtils;
import eu.scy.common.scyelo.ContentTypedScyElo;
import eu.scy.common.scyelo.RooloServices;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.scyelo.ScyEloContentCreator;

public class MissionModelElo extends ContentTypedScyElo<MissionModelEloContent>
{
   private static class MissionModelEloContentCreator implements
            ScyEloContentCreator<MissionModelEloContent>
   {

      @Override
      public MissionModelEloContent createScyEloContent(ScyElo scyElo)
      {
         String xml = scyElo.getElo().getContent().getXmlString();
         if (xml == null || xml.length() == 0)
         {
            return new BasicMissionModelEloContent();
         }
         try
         {
            return MissionModelEloContentXmlUtils.missionModelFromXml(xml);
         }
         catch (URISyntaxException ex)
         {
            throw new IllegalArgumentException("problems with the xml of the elo, uri: "
                     + scyElo.getUri(), ex);
         }
      }

      @Override
      public void updateEloContent(ContentTypedScyElo<MissionModelEloContent> scyElo)
      {
         scyElo.getElo().getContent().setXmlString(
                  MissionModelEloContentXmlUtils.missionModelToXml(scyElo.getTypedContent()));
      }
   }

   private final static MissionModelEloContentCreator missionMapModelEloContentCreator = new MissionModelEloContentCreator();

   public MissionModelElo(IELO elo, RooloServices rooloServices)
   {
      super(elo, rooloServices, missionMapModelEloContentCreator, MissionEloType.MISSION_MAP_MODEL
               .getType());
      setTemplate(true);
   }

   public static MissionModelElo loadElo(URI uri, RooloServices rooloServices)
   {
      IELO elo = rooloServices.getRepository().retrieveELO(uri);
      if (elo == null)
      {
         return null;
      }
      return new MissionModelElo(elo, rooloServices);
   }

   public static MissionModelElo loadLastVersionElo(URI uri, RooloServices rooloServices)
   {
      IELO elo = rooloServices.getRepository().retrieveELOLastVersion(uri);
      if (elo == null)
      {
         return null;
      }
      return new MissionModelElo(elo, rooloServices);
   }

   public static MissionModelElo createElo(RooloServices rooloServices)
   {
      IELO elo = rooloServices.getELOFactory().createELO();
      elo.getMetadata().getMetadataValueContainer(ScyElo.getTechnicalFormatKey(rooloServices))
               .setValue(MissionEloType.MISSION_MAP_MODEL.getType());
      MissionModelElo scyElo = new MissionModelElo(elo, rooloServices);
      return scyElo;
   }

   public MissionModel getMissionModel()
   {
      return new BasicMissionModel(this);
   }

}
