package eu.scy.common.mission;

import java.net.URI;
import java.net.URISyntaxException;

import eu.scy.common.mission.impl.RuntimeSettingsHelperImpl;
import roolo.elo.api.IELO;
import eu.scy.common.mission.impl.BasicRuntimeSettingsEloContent;
import eu.scy.common.mission.impl.jdom.RuntimeSettingsEloContentXmlUtils;
import eu.scy.common.scyelo.ContentTypedScyElo;
import eu.scy.common.scyelo.RooloServices;
import eu.scy.common.scyelo.ScyElo;
import eu.scy.common.scyelo.ScyEloContentCreator;

public class RuntimeSettingsElo extends ContentTypedScyElo<RuntimeSettingsEloContent> implements PropertyAccessorProvider
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

    public PropertyAccessor getPropertyAccessor() {
        return new RuntimeSettingsHelperImpl(getTypedContent(), this);    
    }

   private static final RuntimeSettingsEloContentCreator runtimeSettingsEloContentCreator = new RuntimeSettingsEloContentCreator();

   public RuntimeSettingsElo(IELO elo, RooloServices rooloServices)
   {
      super(elo, rooloServices, runtimeSettingsEloContentCreator, MissionEloType.RUNTIME_SETTINGS
               .getType());
   }

   public static RuntimeSettingsElo loadElo(URI uri, RooloServices rooloServices)
   {
      IELO elo = rooloServices.getRepository().retrieveELO(uri);
      if (elo == null)
      {
         return null;
      }
      return new RuntimeSettingsElo(elo, rooloServices);
   }

   public static RuntimeSettingsElo loadLastVersionElo(URI uri, RooloServices rooloServices)
   {
      IELO elo = rooloServices.getRepository().retrieveELOLastVersion(uri);
      if (elo == null)
      {
         return null;
      }
      return new RuntimeSettingsElo(elo, rooloServices);
   }

   public static RuntimeSettingsElo createElo(RooloServices rooloServices)
   {
      IELO elo = rooloServices.getELOFactory().createELO();
      elo.getMetadata().getMetadataValueContainer(ScyElo.getTechnicalFormatKey(rooloServices))
               .setValue(MissionEloType.RUNTIME_SETTINGS.getType());
      RuntimeSettingsElo scyElo = new RuntimeSettingsElo(elo, rooloServices);
      return scyElo;
   }

}
