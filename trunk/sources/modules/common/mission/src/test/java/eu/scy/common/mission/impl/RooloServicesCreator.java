package eu.scy.common.mission.impl;

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.cms.repository.mock.MockExtensionManager;
import roolo.cms.repository.mock.MockRepository;
import roolo.elo.JDomBasicELOFactory;
import roolo.elo.MockMetadataTypeManager;
import roolo.elo.api.I18nType;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.metadata.CoreRooloMetadataKeyIds;
import roolo.elo.api.metadata.MetadataValueCount;
import roolo.elo.metadata.keys.ContributeMetadataKey;
import roolo.elo.metadata.keys.LongMetadataKey;
import roolo.elo.metadata.keys.RelationMetadataKey;
import roolo.elo.metadata.keys.StringMetadataKey;
import roolo.elo.metadata.keys.UriMetadataKey;
import eu.scy.common.scyelo.RooloServices;
import eu.scy.common.scyelo.ScyRooloMetadataKeyIds;

public class RooloServicesCreator
{
   private static class TestRooloServices implements RooloServices
   {
      private IELOFactory eloFactory;
      private IRepository repository;
      private IMetadataTypeManager metadataTypeManager;
      private IExtensionManager extensionManager;

      @Override
      public IELOFactory getELOFactory()
      {
         return eloFactory;
      }

      @Override
      public IMetadataTypeManager getMetaDataTypeManager()
      {
         return metadataTypeManager;
      }

      @Override
      public IRepository getRepository()
      {
         return repository;
      }

      @Override
      public IExtensionManager getExtensionManager()
      {
         return extensionManager;
      }
   }

   private RooloServicesCreator()
   {

   }

   public static RooloServices createTestRooloServices()
   {
      IMetadataTypeManager metadataTypeManager = new MockMetadataTypeManager();
      IMetadataKey identifierKey = new UriMetadataKey(CoreRooloMetadataKeyIds.IDENTIFIER.getId(),
               "/lom/general/identifier", I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);
      IMetadataKey titleKey = new StringMetadataKey(CoreRooloMetadataKeyIds.TITLE.getId(),
               "/lom/general/title", I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);
      IMetadataKey descriptionKey = new StringMetadataKey(CoreRooloMetadataKeyIds.DESCRIPTION.getId(),
               "/lom/general/description", I18nType.UNIVERSAL, MetadataValueCount.LIST, null);
      IMetadataKey technicalFormatKey = new StringMetadataKey(
               CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId(), "/lom/technical/format",
               I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);
      IMetadataKey isVersionOfKey = new RelationMetadataKey(CoreRooloMetadataKeyIds.IS_VERSION_OF
               .getId(), "/lom/relation/[kind=\"is_version_of\"]", I18nType.UNIVERSAL,
               MetadataValueCount.SINGLE, null);
      IMetadataKey isVersionedByKey = new RelationMetadataKey(
               CoreRooloMetadataKeyIds.IS_VERSIONED_BY.getId(),
               "/lom/relation/[kind=\"is_versioned_by\"]", I18nType.UNIVERSAL,
               MetadataValueCount.SINGLE, null);
      IMetadataKey isForkOfKey = new RelationMetadataKey(
               CoreRooloMetadataKeyIds.IS_FORK_OF.getId(), "/lom/relation/[kind=\"is_fork_of\"]",
               I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);
      IMetadataKey isForkedByKey = new RelationMetadataKey(CoreRooloMetadataKeyIds.IS_FORKED_BY
               .getId(), "/lom/relation/[kind=\"is_forked_by\"]", I18nType.UNIVERSAL,
               MetadataValueCount.LIST, null);
      IMetadataKey versionKey = new LongMetadataKey(CoreRooloMetadataKeyIds.VERSION.getId(),
               "/lom/lifecycle/version", I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);
      IMetadataKey logicalRoleKey = new StringMetadataKey(
               ScyRooloMetadataKeyIds.LOGICAL_TYPE.getId(), "/lom/general/structure",
               I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);
      IMetadataKey functionalRoleKey = new StringMetadataKey(
               ScyRooloMetadataKeyIds.FUNCTIONAL_TYPE.getId(), "/lom/educational/learningResourceType",
               I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);
      IMetadataKey authorKey = new ContributeMetadataKey(
               CoreRooloMetadataKeyIds.AUTHOR.getId(), "/lom/lifecycle/contribute/[kind=\"author\"]",
               I18nType.UNIVERSAL, MetadataValueCount.LIST, null);
      IMetadataKey keywordsKey = new StringMetadataKey(
               ScyRooloMetadataKeyIds.KEYWORDS.getId(), "/lom/general/keyword",
               I18nType.UNIVERSAL, MetadataValueCount.LIST, null);
      IMetadataKey learningActivityKey = new StringMetadataKey(
               ScyRooloMetadataKeyIds.LEARNING_ACTIVITY.getId(), "/lom/educational/context",
               I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);
      IMetadataKey accessKey = new StringMetadataKey(
               ScyRooloMetadataKeyIds.ACCESS.getId(), "/lom/rights/copyrightAndOtherRestrictions",
               I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);
      IMetadataKey missionRuntimeKey = new RelationMetadataKey(
               ScyRooloMetadataKeyIds.MISSION_RUNTIME.getId(),
               "/lom/relation/[kind=\"isbasedon\",type=\"scy/missionruntime\"]", I18nType.UNIVERSAL,
               MetadataValueCount.SINGLE, null);
      IMetadataKey lasKey = new StringMetadataKey(ScyRooloMetadataKeyIds.LAS.getId(),
               "/lom/general/las", I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);
      IMetadataKey iconTypeKey = new roolo.elo.metadata.keys.IconTypeAnnotationKey(ScyRooloMetadataKeyIds.ICON_TYPE.getId(),
               "/lom/annotation/[entity=\"icon\"]", I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);

      metadataTypeManager.registerMetadataKey(identifierKey);
      metadataTypeManager.registerMetadataKey(titleKey);
      metadataTypeManager.registerMetadataKey(descriptionKey);
      metadataTypeManager.registerMetadataKey(technicalFormatKey);
      metadataTypeManager.registerMetadataKey(isVersionOfKey);
      metadataTypeManager.registerMetadataKey(isVersionedByKey);
      metadataTypeManager.registerMetadataKey(isForkOfKey);
      metadataTypeManager.registerMetadataKey(isForkedByKey);
      metadataTypeManager.registerMetadataKey(versionKey);
      metadataTypeManager.registerMetadataKey(logicalRoleKey);
      metadataTypeManager.registerMetadataKey(functionalRoleKey);
      metadataTypeManager.registerMetadataKey(authorKey);
      metadataTypeManager.registerMetadataKey(keywordsKey);
      metadataTypeManager.registerMetadataKey(learningActivityKey);
      metadataTypeManager.registerMetadataKey(accessKey);
      metadataTypeManager.registerMetadataKey(missionRuntimeKey);
      metadataTypeManager.registerMetadataKey(lasKey);
      metadataTypeManager.registerMetadataKey(iconTypeKey);

      MockRepository repository = new MockRepository();
      MockExtensionManager extensionManager = new MockExtensionManager();
      repository.setExtensionManager(extensionManager);
      repository.setMetadataTypeManager(metadataTypeManager);
      repository.setEloFactory(new JDomBasicELOFactory(metadataTypeManager));

      TestRooloServices rooloServices = new TestRooloServices();
      rooloServices.eloFactory = new JDomBasicELOFactory(metadataTypeManager);
      rooloServices.metadataTypeManager = metadataTypeManager;
      rooloServices.repository = repository;
      return rooloServices;
   }
}
