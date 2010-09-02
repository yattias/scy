package eu.scy.common.mission;

import java.net.URI;
import java.net.URISyntaxException;

import roolo.elo.api.IELO;
import eu.scy.common.mission.impl.BasicRuntimeSettingsEloContent;
import eu.scy.common.mission.impl.jdom.RuntimeSettingsEloContentXmlUtils;
import eu.scy.common.scyelo.ContentTypedScyElo;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.scyelo.ScyEloContentCreator;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

public class RuntimeSettingsElo extends ContentTypedScyElo<RuntimeSettingsEloContent>
{

   private static class RuntimeSettingsEloContentCreator implements
            ScyEloContentCreator<RuntimeSettingsEloContent>
   {

      @Override
      public RuntimeSettingsEloContent createScyEloContent(ScyElo scyElo)
      {
         String xml = scyElo.getElo().getContent().getXmlString();
         if (xml == null || xml.length() == 0)
         {
            return new BasicRuntimeSettingsEloContent();
         }
         try
         {
            return RuntimeSettingsEloContentXmlUtils.runtimeSettingsFromXml(xml);
         }
         catch (URISyntaxException e)
         {
            throw new IllegalArgumentException("problems with the xml of the elo, uri: "
                     + scyElo.getUri(), e);
         }
      }

      @Override
      public void updateEloContent(ContentTypedScyElo<RuntimeSettingsEloContent> scyElo)
      {
         scyElo.getElo().getContent().setXmlString(
                  RuntimeSettingsEloContentXmlUtils.runtimeSettingsToXml(scyElo.getTypedContent()));
      }
   }

   private static final RuntimeSettingsEloContentCreator runtimeSettingsEloContentCreator = new RuntimeSettingsEloContentCreator();

   public RuntimeSettingsElo(IELO elo, ToolBrokerAPI tbi)
   {
      super(elo, tbi, runtimeSettingsEloContentCreator);
      verifyTechnicalFormat(MissionEloType.RUNTIME_SETTINGS.getType());
   }

   public static RuntimeSettingsElo loadElo(URI uri, ToolBrokerAPI tbi)
   {
      IELO elo = tbi.getRepository().retrieveELO(uri);
      if (elo == null)
      {
         return null;
      }
      return new RuntimeSettingsElo(elo, tbi);
   }

   public static RuntimeSettingsElo loadLastVersionElo(URI uri, ToolBrokerAPI tbi)
   {
      IELO elo = tbi.getRepository().retrieveELOLastVersion(uri);
      if (elo == null)
      {
         return null;
      }
      return new RuntimeSettingsElo(elo, tbi);
   }

   public static RuntimeSettingsElo createElo(ToolBrokerAPI tbi)
   {
      IELO elo = tbi.getELOFactory().createELO();
      elo.getMetadata().getMetadataValueContainer(ScyElo.getTechnicalFormatKey(tbi)).setValue(
               MissionEloType.RUNTIME_SETTINGS.getType());
      RuntimeSettingsElo scyElo = new RuntimeSettingsElo(elo, tbi);
      return scyElo;
   }

}
