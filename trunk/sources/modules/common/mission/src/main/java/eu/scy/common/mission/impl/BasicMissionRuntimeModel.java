package eu.scy.common.mission.impl;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashSet;
import java.util.Set;

import eu.scy.common.mission.ColorSchemesElo;
import eu.scy.common.mission.EloToolConfigsElo;
import eu.scy.common.mission.MissionModel;
import eu.scy.common.mission.MissionModelElo;
import eu.scy.common.mission.MissionRuntimeElo;
import eu.scy.common.mission.MissionRuntimeEloContent;
import eu.scy.common.mission.MissionRuntimeModel;
import eu.scy.common.mission.MissionSpecificationElo;
import eu.scy.common.mission.RuntimeSettingsElo;
import eu.scy.common.mission.RuntimeSettingsManager;
import eu.scy.common.mission.TemplateElosElo;
import eu.scy.common.scyelo.RooloServices;
import eu.scy.common.scyelo.ScyElo;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class BasicMissionRuntimeModel implements MissionRuntimeModel
{

   private final static Logger logger = Logger.getLogger(BasicMissionRuntimeModel.class);
   private final MissionRuntimeElo missionRuntimeElo;
   private final RooloServices rooloServices;
   private final MissionModelElo missionModelElo;
   private final EloToolConfigsElo eloToolConfigsElo;
   private final TemplateElosElo templateElosElo;
   private final RuntimeSettingsElo runtimeSettingsElo;
   private final ColorSchemesElo colorSchemesElo;
   private final MissionSpecificationElo missionSpecificationElo;
   private Set<URI> specificationContentEloUris;
   private Map<URI, URI> specificationEloUriMap;
   private URI nullUri;

   public BasicMissionRuntimeModel(MissionRuntimeElo missionRuntimeElo,
      MissionSpecificationElo missionSpecificationElo, RooloServices rooloServices)
   {
      super();
      this.missionRuntimeElo = missionRuntimeElo;
      this.missionSpecificationElo = missionSpecificationElo;
      this.rooloServices = rooloServices;
      final MissionRuntimeEloContent missionRuntimeEloContent = missionRuntimeElo.getTypedContent();
      missionModelElo = MissionModelElo.loadLastVersionElo(
         missionRuntimeEloContent.getMissionMapModelEloUri(), rooloServices);
      eloToolConfigsElo = EloToolConfigsElo.loadLastVersionElo(
         missionRuntimeEloContent.getEloToolConfigsEloUri(), rooloServices);
      templateElosElo = TemplateElosElo.loadLastVersionElo(
         missionRuntimeEloContent.getTemplateElosEloUri(), rooloServices);
      runtimeSettingsElo = RuntimeSettingsElo.loadLastVersionElo(
         missionRuntimeEloContent.getRuntimeSettingsEloUri(), rooloServices);
      colorSchemesElo = ColorSchemesElo.loadLastVersionElo(
         missionRuntimeEloContent.getColorSchemesEloUri(), rooloServices);
   }

   public BasicMissionRuntimeModel(MissionRuntimeElo missionRuntimeElo,
      MissionSpecificationElo missionSpecificationElo, RooloServices rooloServices,
      MissionModelElo missionModelElo, EloToolConfigsElo eloToolConfigsElo,
      TemplateElosElo templateElosElo, RuntimeSettingsElo runtimeSettingsElo,
      ColorSchemesElo colorSchemesElo)
   {
      super();
      this.missionRuntimeElo = missionRuntimeElo;
      this.missionSpecificationElo = missionSpecificationElo;
      this.rooloServices = rooloServices;
      this.missionModelElo = missionModelElo;
      this.eloToolConfigsElo = eloToolConfigsElo;
      this.templateElosElo = templateElosElo;
      this.runtimeSettingsElo = runtimeSettingsElo;
      this.colorSchemesElo = colorSchemesElo;
   }

   @Override
   public String toString()
   {
      StringBuilder builder = new StringBuilder();
      builder.append(this.getClass().getName());
      builder.append("{");
      appendEloUri(builder, missionRuntimeElo, "missionRuntimeElo");
      appendEloUri(builder, missionModelElo, ",missionModelElo");
      appendEloUri(builder, eloToolConfigsElo, ",eloToolConfigsElo");
      appendEloUri(builder, templateElosElo, ",templateElosElo");
      appendEloUri(builder, runtimeSettingsElo, ",runtimeSettingsElo");
      appendEloUri(builder, colorSchemesElo, ",colorSchemesElo");
      builder.append("}");
      return builder.toString();
   }

   private void appendEloUri(StringBuilder builder, ScyElo scyElo, String label)
   {
      builder.append(label);
      if (scyElo == null)
      {
         builder.append("=null");
      }
      else
      {
         builder.append(".uri=");
         builder.append(scyElo.getUri());
      }
   }

   @Override
   public EloToolConfigsElo getEloToolConfigsElo()
   {
      return eloToolConfigsElo;
   }

   @Override
   public MissionModelElo getMissionModelElo()
   {
      return missionModelElo;
   }

   @Override
   public MissionRuntimeElo getMissionRuntimeElo()
   {
      return missionRuntimeElo;
   }

   @Override
   public RuntimeSettingsElo getRuntimeSettingsElo()
   {
      return runtimeSettingsElo;
   }

   @Override
   public TemplateElosElo getTemplateElosElo()
   {
      return templateElosElo;
   }

   @Override
   public MissionModel getMissionModel()
   {
      if (missionModelElo == null)
      {
         return null;
      }
      return missionModelElo.getMissionModel();
   }

   @Override
   public RuntimeSettingsManager getRuntimeSettingsManager() throws URISyntaxException
   {
      Set<URI> specificationMissionMapModelEloUriSet = new HashSet<URI>();
      RuntimeSettingsElo specificationRuntimeSettingsElo = null;
      if (missionSpecificationElo != null)
      {
         if (missionSpecificationElo.getTypedContent().getMissionMapModelEloUri() != null)
         {
            MissionModelElo specificationMissionModelElo = MissionModelElo.loadElo(
               missionSpecificationElo.getTypedContent().getMissionMapModelEloUri(),
               rooloServices);
            if (specificationMissionModelElo != null)
            {
               specificationMissionMapModelEloUriSet.addAll(specificationMissionModelElo.getMissionModel().getEloUris(true));
            }
         }

         if (missionSpecificationElo.getTypedContent().getRuntimeSettingsEloUri() != null)
         {
            specificationRuntimeSettingsElo = RuntimeSettingsElo.loadElo(missionSpecificationElo.getTypedContent().getRuntimeSettingsEloUri(), rooloServices);
         }
      }

      return new MissionRuntimeSettingsManager(specificationRuntimeSettingsElo, runtimeSettingsElo,
         specificationMissionMapModelEloUriSet, rooloServices);
   }

   @Override
   public ColorSchemesElo getColorSchemesElo()
   {
      return colorSchemesElo;
   }

   @Override
   public URI getAnchorEloUriForElo(URI eloUri)
   {
      if (specificationContentEloUris == null)
      {
         specificationContentEloUris = new HashSet<URI>(missionModelElo.getMissionModel().getEloUris(false));
         specificationEloUriMap = new ConcurrentHashMap<URI, URI>();
         try
         {
            nullUri = new URI("");
         }
         catch (URISyntaxException ex)
         {
            logger.error("failed to create nullUri", ex);
         }
      }
      return getSpecificationEloUri(eloUri);
   }

   private URI getSpecificationEloUri(URI eloUri)
   {
      if (eloUri == null)
      {
         return null;
      }
      if (specificationEloUriMap.containsKey(eloUri))
      {
         URI uri = specificationEloUriMap.get(eloUri);
         if (nullUri.equals(uri))
         {
            return null;
         }
         return uri;
      }
      URI specificationEloUri = findSpecificationEloUri(eloUri);
      specificationEloUriMap.put(eloUri, specificationEloUri == null ? nullUri : specificationEloUri);
      return specificationEloUri;
   }

   private URI findSpecificationEloUri(URI eloUri)
   {
      if (eloUri == null)
      {
         return null;
      }
      ScyElo scyElo = ScyElo.loadMetadata(eloUri, rooloServices);
      if (scyElo == null)
      {
         return null;
      }
      if (specificationContentEloUris.contains(scyElo.getUri()))
      {
         return scyElo.getUri();
      }
      if (!eloUri.equals(scyElo.getUriFirstVersion()))
      {
         scyElo = ScyElo.loadMetadata(scyElo.getUriFirstVersion(), rooloServices);
      }
      return findSpecificationEloUri(scyElo.getIsForkedOfEloUri());
   }
}
