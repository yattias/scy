package eu.scy.eloimporter;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.JDomBasicELOFactory;
import roolo.elo.api.I18nType;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import roolo.elo.api.metadata.MetadataValueCount;
import roolo.elo.metadata.keys.StringMetadataKey;
import roolo.elo.metadata.value.validators.LongValidator;
import roolo.elo.metadata.value.validators.StringValidator;
import eu.scy.eloimporter.contentextractors.ContentExtractorFactory;
import eu.scy.eloimporter.contentextractors.IContentExtractor;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

public class ELOImporter {

	private static final String SCY_VERSION = "SCYv0.5";
	private IRepository<IELO<IMetadataKey>, IMetadataKey> repository;
	private IExtensionManager extensionManager;
	private IELOFactory<IMetadataKey> eloFactory;
	private IMetadataTypeManager<IMetadataKey> typeManager;

	@SuppressWarnings("unchecked")
	public ELOImporter() {
		ToolBrokerAPI<IMetadataKey> toolBroker = new ToolBrokerImpl<IMetadataKey>();
		this.extensionManager = toolBroker.getExtensionManager();
		this.typeManager = toolBroker.getMetaDataTypeManager();
		this.repository = toolBroker.getRepository();
		this.eloFactory = new JDomBasicELOFactory(this.typeManager, this.typeManager
				.getMetadataKey("uri"));

		this.extensionManager.registerExtension("text/xml", "xml");
		this.extensionManager.registerExtension("text/plain", "txt");

		this.registerKeys();
	}

	private void registerKeys() {
		// IMetadataKey identifierCatalog = new
		// StringMetadataKey("identifier/catalog",
		// "/metadata/lom/general/identifier/catalog", I18nType.UNIVERSAL,
		// MetadataValueCount.SINGLE, new UriValidator());
		// typeManager.registerMetadataKey(identifierCatalog);
		//        
		// IMetadataKey identifierEntry = new
		// StringMetadataKey("identifier/entry",
		// "/metadata/lom/general/identifier/entry", I18nType.UNIVERSAL,
		// MetadataValueCount.SINGLE, new UriValidator());
		// typeManager.registerMetadataKey(identifierEntry);

		IMetadataKey size = new StringMetadataKey("size", "/lom/technical/size",
				I18nType.UNIVERSAL, MetadataValueCount.SINGLE, new LongValidator());
		if (!this.typeManager.isMetadataKeyRegistered(size)) {
			this.typeManager.registerMetadataKey(size);
		}

		IMetadataKey aggregation = new StringMetadataKey("aggregation",
				"/lom/general/aggregationLevel", I18nType.UNIVERSAL, MetadataValueCount.SINGLE,
				new LongValidator());
		if (!this.typeManager.isMetadataKeyRegistered(aggregation)) {
			this.typeManager.registerMetadataKey(aggregation);
		}

		IMetadataKey contributeRoleSource = new StringMetadataKey("contribute/role/source",
				"/lom/lifecycle/contribute/role/source", I18nType.UNIVERSAL,
				MetadataValueCount.SINGLE, new StringValidator());
		if (!this.typeManager.isMetadataKeyRegistered(contributeRoleSource)) {
			this.typeManager.registerMetadataKey(contributeRoleSource);
		}

		IMetadataKey contributeRoleValue = new StringMetadataKey("contribute/role/value",
				"/lom/lifecycle/contribute/role/value", I18nType.UNIVERSAL,
				MetadataValueCount.SINGLE, new StringValidator());
		if (!this.typeManager.isMetadataKeyRegistered(contributeRoleValue)) {
			this.typeManager.registerMetadataKey(contributeRoleValue);
		}

		IMetadataKey contributeEntity = new StringMetadataKey("contribute/entity",
				"/lom/lifecycle/contribute/entity", I18nType.UNIVERSAL, MetadataValueCount.SINGLE,
				new StringValidator());
		if (!this.typeManager.isMetadataKeyRegistered(contributeEntity)) {
			this.typeManager.registerMetadataKey(contributeEntity);
		}

		IMetadataKey contributeDate = new StringMetadataKey("contribute/date",
				"/lom/lifecycle/contribute/date/datetime", I18nType.UNIVERSAL,
				MetadataValueCount.SINGLE, new LongValidator());
		if (!this.typeManager.isMetadataKeyRegistered(contributeDate)) {
			this.typeManager.registerMetadataKey(contributeDate);
		}

		IMetadataKey learningResourceTypeSource = new StringMetadataKey("learningResource/source",
				"/lom/educational/learningResource/source", I18nType.UNIVERSAL,
				MetadataValueCount.SINGLE, new StringValidator());
		if (!this.typeManager.isMetadataKeyRegistered(learningResourceTypeSource)) {
			this.typeManager.registerMetadataKey(learningResourceTypeSource);
		}

		IMetadataKey learningResourceTypeValue = new StringMetadataKey("learningResource/value",
				"/lom/educational/learningResource/value", I18nType.UNIVERSAL,
				MetadataValueCount.SINGLE, new StringValidator());
		if (!this.typeManager.isMetadataKeyRegistered(learningResourceTypeValue)) {
			this.typeManager.registerMetadataKey(learningResourceTypeValue);
		}

		IMetadataKey copyRightsSource = new StringMetadataKey("copyright/source",
				"/lom/rights/copyrightAndOtherRestrictions/source", I18nType.UNIVERSAL,
				MetadataValueCount.SINGLE, new StringValidator());
		if (!this.typeManager.isMetadataKeyRegistered(copyRightsSource)) {
			this.typeManager.registerMetadataKey(copyRightsSource);
		}

		IMetadataKey copyRightsValue = new StringMetadataKey("copyright/value",
				"/lom/rights/copyrightAndOtherRestrictions/value", I18nType.UNIVERSAL,
				MetadataValueCount.SINGLE, new StringValidator());
		if (!this.typeManager.isMetadataKeyRegistered(copyRightsValue)) {
			this.typeManager.registerMetadataKey(copyRightsValue);
		}

		IMetadataKey description = new StringMetadataKey("description", "/lom/description",
				I18nType.SPECIFIC, MetadataValueCount.SINGLE, new StringValidator());
		if (!this.typeManager.isMetadataKeyRegistered(description)) {
			this.typeManager.registerMetadataKey(description);
		}

		IMetadataKey keyword = new StringMetadataKey("keyword", "/lom/keyword", I18nType.SPECIFIC,
				MetadataValueCount.LIST, new StringValidator());
		if (!this.typeManager.isMetadataKeyRegistered(keyword)) {
			this.typeManager.registerMetadataKey(keyword);
		}
	}

	public IELO<IMetadataKey> importFile(File file) {
		IELO<IMetadataKey> elo = this.eloFactory.createELO();

		IMetadataValueContainer title = elo.getMetadata().getMetadataValueContainer(
				this.typeManager.getMetadataKey("title"));
		title.setValue(file.getName());

		IMetadataValueContainer descriptionContainer = elo.getMetadata().getMetadataValueContainer(
				this.typeManager.getMetadataKey("description"));
		descriptionContainer.setValue("description");

		IMetadataValueContainer keywordContainer = elo.getMetadata().getMetadataValueContainer(
				this.typeManager.getMetadataKey("keyword"));
		List<String> keywords = Arrays.asList(new String[] { "keyword1", "keyword2", "keyword3",
				"keyword4" });
		System.out.println(keywords);
		keywordContainer.setValueList(keywords);
		keywordContainer.setValueList(keywords, Locale.GERMAN);

		IMetadataValueContainer aggregationContainer = elo.getMetadata().getMetadataValueContainer(
				this.typeManager.getMetadataKey("aggregation"));
		aggregationContainer.setValue(1);

		IMetadataValueContainer type = elo.getMetadata().getMetadataValueContainer(
				this.typeManager.getMetadataKey("type"));
		String extension = file.getName().substring(file.getName().lastIndexOf('.'),
				file.getName().length());
		String mimetype = this.extensionManager.getType(extension);
		type.setValue(mimetype);

		IMetadataValueContainer sizeContainer = elo.getMetadata().getMetadataValueContainer(
				this.typeManager.getMetadataKey("size"));
		sizeContainer.setValue(file.length());

		IMetadataValueContainer contributeRoleSourceContainer = elo.getMetadata()
				.getMetadataValueContainer(
						this.typeManager.getMetadataKey("contribute/role/source"));
		contributeRoleSourceContainer.setValue(SCY_VERSION);

		IMetadataValueContainer contributeRoleValueContainer = elo
				.getMetadata()
				.getMetadataValueContainer(this.typeManager.getMetadataKey("contribute/role/value"));
		contributeRoleValueContainer.setValue(ContributeRoleValues.creator.toString());

		IMetadataValueContainer contributeEntityContainer = elo.getMetadata()
				.getMetadataValueContainer(this.typeManager.getMetadataKey("contribute/entity"));
		contributeEntityContainer.setValue("c23cfac0-9f71-11dd-ad8b-0800200c9a66");

		IMetadataValueContainer contributeDateContainer = elo.getMetadata()
				.getMetadataValueContainer(this.typeManager.getMetadataKey("contribute/date"));
		contributeDateContainer.setValue(System.currentTimeMillis());

		IMetadataValueContainer learningResourceSourceContainer = elo.getMetadata()
				.getMetadataValueContainer(
						this.typeManager.getMetadataKey("learningResource/source"));
		learningResourceSourceContainer.setValue(SCY_VERSION);

		IMetadataValueContainer learningResourceValueContainer = elo.getMetadata()
				.getMetadataValueContainer(
						this.typeManager.getMetadataKey("learningResource/value"));
		learningResourceValueContainer.setValue(LearningResourceType.narrativetext.toString());

		IMetadataValueContainer copyrightSourceContainer = elo.getMetadata()
				.getMetadataValueContainer(this.typeManager.getMetadataKey("copyright/source"));
		copyrightSourceContainer.setValue(SCY_VERSION);

		IMetadataValueContainer copyrightValueContainer = elo.getMetadata()
				.getMetadataValueContainer(this.typeManager.getMetadataKey("copyright/value"));
		copyrightValueContainer.setValue("public");

		this.setContent(elo, file, mimetype);

		return elo;
	}

	private void setContent(IELO<IMetadataKey> elo, File file, String mimetype) {
		IContentExtractor extractor = ContentExtractorFactory.getContentExtractor(mimetype);
		IContent content = extractor.getContent(file);
		elo.setContent(content);
	}

	public void saveElo(IELO<IMetadataKey> elo) {
		this.repository.addELO(elo);
	}

	public static void main(String[] args) throws URISyntaxException {
		ELOImporter importer = new ELOImporter();
		URL url = ELOImporter.class.getResource("/100-Carolina-Quispe-b-3.xml");
		File file = new File(url.toURI());
		IELO<IMetadataKey> elo = importer.importFile(file);
		importer.saveElo(elo);
		System.exit(0);
	}
}
