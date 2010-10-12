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
import roolo.elo.metadata.keys.LongMetadataKey;
import roolo.elo.metadata.keys.RelationMetadataKey;
import roolo.elo.metadata.keys.StringMetadataKey;
import roolo.elo.metadata.keys.UriMetadataKey;
import eu.scy.common.scyelo.RooloServices;

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
               "/lom/general/title", I18nType.UNIVERSAL, MetadataValueCount.LIST, null);
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

      metadataTypeManager.registerMetadataKey(identifierKey);
      metadataTypeManager.registerMetadataKey(titleKey);
      metadataTypeManager.registerMetadataKey(descriptionKey);
      metadataTypeManager.registerMetadataKey(technicalFormatKey);
      metadataTypeManager.registerMetadataKey(isVersionOfKey);
      metadataTypeManager.registerMetadataKey(isVersionedByKey);
      metadataTypeManager.registerMetadataKey(isForkOfKey);
      metadataTypeManager.registerMetadataKey(isForkedByKey);
      metadataTypeManager.registerMetadataKey(versionKey);

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
