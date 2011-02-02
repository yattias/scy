package eu.scy.eloimporter;

import java.io.File;
import java.io.FileFilter;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import roolo.api.IExtensionManager;
import roolo.api.IRepository;
import roolo.elo.JDomBasicELOFactory;
import roolo.elo.api.IContent;
import roolo.elo.api.IELO;
import roolo.elo.api.IELOFactory;
import roolo.elo.api.IMetadataKey;
import roolo.elo.api.IMetadataTypeManager;
import roolo.elo.api.IMetadataValueContainer;
import eu.scy.eloimporter.contentextractors.ContentExtractorFactory;
import eu.scy.eloimporter.contentextractors.IContentExtractor;
import eu.scy.toolbroker.ToolBrokerImpl;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

/**
 * A tool to easily import files as elo. Needs a Roolo-Server instance to be
 * running. The elos are directly saved as to the roolo. In future version the
 * metadata will be adjustable.
 * 
 * @author fschulz
 */
public class ELOImporter {

	private static final String SCY_VERSION = "SCYv0.5";
	private static int counter = 0;
	private IRepository<IELO<IMetadataKey>, IMetadataKey> repository;
	private IExtensionManager extensionManager;
	private IELOFactory<IMetadataKey> eloFactory;
	private IMetadataTypeManager<IMetadataKey> typeManager;
	private Locale[] locales;

	@SuppressWarnings("unchecked")
	public ELOImporter() {
        String username = "";
        String password = "";
		ToolBrokerAPI toolBroker = new ToolBrokerImpl(username, password);
		this.extensionManager = toolBroker.getExtensionManager();
		this.typeManager = toolBroker.getMetaDataTypeManager();
		this.repository = toolBroker.getRepository();

		this.extensionManager.registerExtension("text/xml", "xml");
		this.extensionManager.registerExtension("text/plain", "txt");

		this.eloFactory = new JDomBasicELOFactory(this.typeManager);

		this.fillLocales();
	}

	public IELO<IMetadataKey> importFile(File file) {

		IELO<IMetadataKey> elo = this.createNewElo();

		String mimetype = this.extensionManager.getType(file.toURI());

		this.setMetadata(file, elo, mimetype);
		this.setContent(elo, file, mimetype);

		return elo;
	}

	private void setMetadata(File file, IELO<IMetadataKey> elo, String mimetype) {
		IMetadataValueContainer title = elo.getMetadata()
				.getMetadataValueContainer(
						this.typeManager.getMetadataKey("title"));
		title.setValue(file.getName(), this.getNearestLocale(Locale
				.getDefault()));

		IMetadataValueContainer type = elo.getMetadata()
				.getMetadataValueContainer(
						this.typeManager.getMetadataKey("type"));
		type.setValue(mimetype);

		IMetadataValueContainer sizeContainer = elo.getMetadata()
				.getMetadataValueContainer(
						this.typeManager.getMetadataKey("size"));
		sizeContainer.setValue(file.length());
	}

	private void setDummyMetadata(IELO<IMetadataKey> elo) {
		IMetadataValueContainer title = elo.getMetadata()
				.getMetadataValueContainer(
						this.typeManager.getMetadataKey("title"));
		title.setValue("title", this.getNearestLocale(Locale.getDefault()));

		IMetadataValueContainer descriptionContainer = elo.getMetadata()
				.getMetadataValueContainer(
						this.typeManager.getMetadataKey("description"));
		descriptionContainer.setValue("description", this
				.getNearestLocale(Locale.getDefault()));

		IMetadataValueContainer keywordContainer = elo.getMetadata()
				.getMetadataValueContainer(
						this.typeManager.getMetadataKey("keyword"));
		List<String> keywords = Arrays.asList(new String[] { "keyword1",
				"keyword2", "keyword3", "keyword4" });
		// System.out.println(keywords);
		keywordContainer.setValueList(keywords);
		keywordContainer.setValueList(keywords, this
				.getNearestLocale(Locale.GERMAN));

		IMetadataValueContainer structureSourceContainer = elo
				.getMetadata()
				.getMetadataValueContainer(
						this.typeManager
								.getMetadataKey("logical_representation/source"));
		structureSourceContainer.setValue(SCY_VERSION);

		IMetadataValueContainer structureValueContainer = elo
				.getMetadata()
				.getMetadataValueContainer(
						this.typeManager
								.getMetadataKey("logical_representation/value"));
		structureValueContainer.setValue(StructureValues.graph.toString());

		IMetadataValueContainer aggregationContainer = elo.getMetadata()
				.getMetadataValueContainer(
						this.typeManager.getMetadataKey("aggregation"));
		aggregationContainer.setValue(1);

		IMetadataValueContainer type = elo.getMetadata()
				.getMetadataValueContainer(
						this.typeManager.getMetadataKey("type"));
		type.setValue("text/plain");

		IMetadataValueContainer sizeContainer = elo.getMetadata()
				.getMetadataValueContainer(
						this.typeManager.getMetadataKey("size"));
		sizeContainer.setValue(0);

		IMetadataValueContainer contributeRoleSourceContainer = elo
				.getMetadata().getMetadataValueContainer(
						this.typeManager
								.getMetadataKey("contribute/role/source"));
		contributeRoleSourceContainer.setValue(SCY_VERSION);

		IMetadataValueContainer contributeRoleValueContainer = elo
				.getMetadata().getMetadataValueContainer(
						this.typeManager
								.getMetadataKey("contribute/role/value"));
		contributeRoleValueContainer.setValue(ContributeRoleValues.creator
				.toString());

		IMetadataValueContainer contributeEntityContainer = elo.getMetadata()
				.getMetadataValueContainer(
						this.typeManager.getMetadataKey("contribute/entity"));
		contributeEntityContainer
				.setValue("c23cfac0-9f71-11dd-ad8b-0800200c9a66");

		IMetadataValueContainer contributeDateContainer = elo.getMetadata()
				.getMetadataValueContainer(
						this.typeManager.getMetadataKey("contribute/date"));
		contributeDateContainer.setValue(System.currentTimeMillis());

		IMetadataValueContainer learningResourceSourceContainer = elo
				.getMetadata().getMetadataValueContainer(
						this.typeManager
								.getMetadataKey("functional_role/source"));
		learningResourceSourceContainer.setValue(SCY_VERSION);

		IMetadataValueContainer learningResourceValueContainer = elo
				.getMetadata().getMetadataValueContainer(
						this.typeManager
								.getMetadataKey("functional_role/value"));
		learningResourceValueContainer
				.setValue(LearningResourceTypeValues.report.toString());

		IMetadataValueContainer copyrightSourceContainer = elo.getMetadata()
				.getMetadataValueContainer(
						this.typeManager.getMetadataKey("copyright/source"));
		copyrightSourceContainer.setValue(SCY_VERSION);

		IMetadataValueContainer copyrightValueContainer = elo.getMetadata()
				.getMetadataValueContainer(
						this.typeManager.getMetadataKey("copyright/value"));
		copyrightValueContainer.setValue("public");
	}

	private void setContent(IELO<IMetadataKey> elo, File file, String mimetype) {
		IContentExtractor extractor = ContentExtractorFactory
				.getContentExtractor(mimetype);
		IContent content = extractor.getContent(file);
		elo.setContent(content);
	}

	public void saveElo(IELO<IMetadataKey> elo) {
		this.repository.addELO(elo);
	}

	public IELO<IMetadataKey> createNewElo() {
		IELO<IMetadataKey> elo = this.eloFactory.createELO();
		setDummyMetadata(elo);
		return elo;
	}

	public IMetadataTypeManager<IMetadataKey> getTypeManager() {
		return this.typeManager;
	}

	public static void main(String[] args) throws URISyntaxException {
		ELOImporter importer = new ELOImporter();
		URL url = ELOImporter.class.getResource("/");
		File dir = new File(url.toURI());
		File[] xmlFiles = dir.listFiles(new FileFilter() {

			public boolean accept(File pathname) {
				return pathname.getName().endsWith(".xml");
			}
		});
		// GraphConverterAgent<IELO<IMetadataKey>, IMetadataKey> agent = new
		// GraphConverterAgent<IELO<IMetadataKey>, IMetadataKey>();
		for (final File file : xmlFiles) {
			// System.out.println("elo #" + updateCounter() + " saved");
			IELO<IMetadataKey> elo = importer.importFile(file);
			// agent.processElo(elo);
			importer.saveElo(elo);
		}
		System.exit(0);
	}

	protected static int updateCounter() {
		return counter++;
	}

	private void fillLocales() {
		Locale[] tmp_locales = Locale.getAvailableLocales();
		Set<Locale> locale_set = new HashSet<Locale>();
		for (Locale locale : tmp_locales) {
			if ("".equals(locale.getCountry())) {
				locale_set.add(locale);
			}
		}

		this.locales = new Locale[locale_set.size()];
		int i = 0;
		for (Locale locale : locale_set) {
			this.locales[i] = locale;
			i++;
		}
		Arrays.sort(this.locales, new Comparator<Locale>() {

			public int compare(Locale o1, Locale o2) {
				return o1.toString().compareTo(o2.toString());
			}
		});
	}

	private Locale getNearestLocale(Locale locale) {
		for (int i = 0; i < this.locales.length; i++) {
			if (this.locales[i].getLanguage().equals(locale.getLanguage())) {
				return this.locales[i];
			}
		}
		return locale;
	}
}
