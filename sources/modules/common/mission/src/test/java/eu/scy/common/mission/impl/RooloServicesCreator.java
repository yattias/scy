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
import roolo.elo.metadata.keys.BinaryKey;
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
					"/lom/general/title", I18nType.SPECIFIC, MetadataValueCount.SINGLE, null);
		IMetadataKey descriptionKey = new StringMetadataKey(
					CoreRooloMetadataKeyIds.DESCRIPTION.getId(), "/lom/general/description",
					I18nType.SPECIFIC, MetadataValueCount.SINGLE, null);
		IMetadataKey dateCreatedKey = new LongMetadataKey(
					CoreRooloMetadataKeyIds.DATE_CREATED.getId(),
					"/lom/technical/customElements/dateCreated", I18nType.UNIVERSAL,
					MetadataValueCount.SINGLE, null);
		IMetadataKey dateLastModifiedKey = new LongMetadataKey(
					CoreRooloMetadataKeyIds.DATE_LAST_MODIFIED.getId(),
					"/lom/technical/customElements/dateLastModified", I18nType.UNIVERSAL,
					MetadataValueCount.SINGLE, null);
		IMetadataKey dateFirstUserSaveKey = new LongMetadataKey(
					ScyRooloMetadataKeyIds.DATE_FIRST_USER_SAVE.getId(),
					"/lom/technical/customElements/dateFirstUserSave", I18nType.UNIVERSAL,
					MetadataValueCount.SINGLE, null);
		IMetadataKey technicalFormatKey = new StringMetadataKey(
					CoreRooloMetadataKeyIds.TECHNICAL_FORMAT.getId(), "/lom/technical/format",
					I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);
		IMetadataKey isVersionOfKey = new RelationMetadataKey(
					CoreRooloMetadataKeyIds.IS_VERSION_OF.getId(),
					"/lom/relation/[kind=\"is_version_of\"]", I18nType.UNIVERSAL,
					MetadataValueCount.SINGLE, null);
		IMetadataKey isVersionedByKey = new RelationMetadataKey(
					CoreRooloMetadataKeyIds.IS_VERSIONED_BY.getId(),
					"/lom/relation/[kind=\"is_versioned_by\"]", I18nType.UNIVERSAL,
					MetadataValueCount.SINGLE, null);
		IMetadataKey isForkOfKey = new RelationMetadataKey(
					CoreRooloMetadataKeyIds.IS_FORK_OF.getId(), "/lom/relation/[kind=\"is_fork_of\"]",
					I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);
		IMetadataKey isForkedByKey = new RelationMetadataKey(
					CoreRooloMetadataKeyIds.IS_FORKED_BY.getId(),
					"/lom/relation/[kind=\"is_forked_by\"]", I18nType.UNIVERSAL,
					MetadataValueCount.LIST, null);
		IMetadataKey versionKey = new LongMetadataKey(CoreRooloMetadataKeyIds.VERSION.getId(),
					"/lom/lifecycle/version", I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);
		IMetadataKey logicalRoleKey = new StringMetadataKey(
					ScyRooloMetadataKeyIds.LOGICAL_TYPE.getId(), "/lom/general/structure",
					I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);
		IMetadataKey functionalRoleKey = new StringMetadataKey(
					ScyRooloMetadataKeyIds.FUNCTIONAL_TYPE.getId(),
					"/lom/educational/learningResourceType", I18nType.UNIVERSAL,
					MetadataValueCount.SINGLE, null);
		IMetadataKey authorKey = new ContributeMetadataKey(CoreRooloMetadataKeyIds.AUTHOR.getId(),
					"/lom/lifecycle/contribute/[kind=\"author\"]", I18nType.UNIVERSAL,
					MetadataValueCount.LIST, null);
		IMetadataKey creatorKey = new ContributeMetadataKey(ScyRooloMetadataKeyIds.CREATOR.getId(),
					"/lom/lifecycle/contribute/[kind=\"initiator\"]", I18nType.UNIVERSAL,
					MetadataValueCount.SINGLE, null);
		IMetadataKey keywordsKey = new StringMetadataKey(CoreRooloMetadataKeyIds.KEYWORDS.getId(),
					"/keywords", I18nType.UNIVERSAL, MetadataValueCount.LIST, null);
		IMetadataKey socialTagsKey = new StringMetadataKey(
					CoreRooloMetadataKeyIds.SOCIAL_TAGS.getId(), "/socialTags", I18nType.UNIVERSAL,
					MetadataValueCount.LIST, null);
		IMetadataKey learningActivityKey = new StringMetadataKey(
					ScyRooloMetadataKeyIds.LEARNING_ACTIVITY.getId(), "/lom/educational/context",
					I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);
		IMetadataKey accessKey = new StringMetadataKey(ScyRooloMetadataKeyIds.ACCESS.getId(),
					"/lom/rights/copyrightAndOtherRestrictions", I18nType.UNIVERSAL,
					MetadataValueCount.SINGLE, null);
		IMetadataKey missionRuntimeKey = new RelationMetadataKey(
					ScyRooloMetadataKeyIds.MISSION_RUNTIME.getId(),
					"/lom/relation/[kind=\"isbasedon\",type=\"scy/missionruntime\"]",
					I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);
		IMetadataKey missionRunningKey = new StringMetadataKey(
					ScyRooloMetadataKeyIds.MISSION_RUNNING.getId(), "/lom/general/missionRunning",
					I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);
		IMetadataKey lasKey = new StringMetadataKey(ScyRooloMetadataKeyIds.LAS.getId(),
					"/lom/general/las", I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);
		IMetadataKey iconTypeKey = new roolo.elo.metadata.keys.IconTypeAnnotationKey(
					ScyRooloMetadataKeyIds.ICON_TYPE.getId(), "/lom/annotation/[entity=\"icon\"]",
					I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);
		IMetadataKey colorSchemeIdKey = new roolo.elo.metadata.keys.ColorSchemeIdAnnotationKey(
					ScyRooloMetadataKeyIds.COLOR_SCHEME_ID.getId(), "/lom/annotation/[entity=\"colorId\"]",
					I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);
		IMetadataKey thumbnailKey = new BinaryKey(CoreRooloMetadataKeyIds.THUMBNAIL.getId(),
					"/thumbnail", I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);
		IMetadataKey assignmentUriKey = new UriMetadataKey(
					ScyRooloMetadataKeyIds.ASSIGNMENT_URI.getId(), "/lom/resource/assignmentUri",
					I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);
		IMetadataKey resourcesUriKey = new UriMetadataKey(
					ScyRooloMetadataKeyIds.RESOURCES_URI.getId(), "/lom/resource/resourcesUri",
					I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);
		IMetadataKey obligatoryInPortfolioKey = new StringMetadataKey(
					ScyRooloMetadataKeyIds.OBLIGATORY_IN_PORTFOLIO.getId(),
					"/lom/educational/obligatoryInPortfolio", I18nType.UNIVERSAL,
					MetadataValueCount.SINGLE, null);
		IMetadataKey feedbackOnKey = new RelationMetadataKey(
					ScyRooloMetadataKeyIds.FEEDBACK_ON.getId(),
					"/lom/relation/[kind=\"feedbackOn\"]",
					I18nType.UNIVERSAL, MetadataValueCount.SINGLE, null);

		metadataTypeManager.registerMetadataKey(identifierKey);
		metadataTypeManager.registerMetadataKey(titleKey);
		metadataTypeManager.registerMetadataKey(descriptionKey);
		metadataTypeManager.registerMetadataKey(technicalFormatKey);
		metadataTypeManager.registerMetadataKey(dateCreatedKey);
		metadataTypeManager.registerMetadataKey(dateLastModifiedKey);
		metadataTypeManager.registerMetadataKey(dateFirstUserSaveKey);
		metadataTypeManager.registerMetadataKey(isVersionOfKey);
		metadataTypeManager.registerMetadataKey(isVersionedByKey);
		metadataTypeManager.registerMetadataKey(isForkOfKey);
		metadataTypeManager.registerMetadataKey(isForkedByKey);
		metadataTypeManager.registerMetadataKey(versionKey);
		metadataTypeManager.registerMetadataKey(logicalRoleKey);
		metadataTypeManager.registerMetadataKey(functionalRoleKey);
		metadataTypeManager.registerMetadataKey(authorKey);
		metadataTypeManager.registerMetadataKey(creatorKey);
		metadataTypeManager.registerMetadataKey(keywordsKey);
		metadataTypeManager.registerMetadataKey(socialTagsKey);
		metadataTypeManager.registerMetadataKey(learningActivityKey);
		metadataTypeManager.registerMetadataKey(accessKey);
		metadataTypeManager.registerMetadataKey(missionRuntimeKey);
		metadataTypeManager.registerMetadataKey(missionRunningKey);
		metadataTypeManager.registerMetadataKey(lasKey);
		metadataTypeManager.registerMetadataKey(iconTypeKey);
		metadataTypeManager.registerMetadataKey(colorSchemeIdKey);
		metadataTypeManager.registerMetadataKey(thumbnailKey);
		metadataTypeManager.registerMetadataKey(assignmentUriKey);
		metadataTypeManager.registerMetadataKey(resourcesUriKey);
		metadataTypeManager.registerMetadataKey(obligatoryInPortfolioKey);
		metadataTypeManager.registerMetadataKey(feedbackOnKey);

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
