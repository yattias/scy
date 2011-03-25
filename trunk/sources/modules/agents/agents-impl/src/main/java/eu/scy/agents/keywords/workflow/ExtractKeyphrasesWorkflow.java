package eu.scy.agents.keywords.workflow;

import java.io.File;
import java.util.Properties;

import org.apache.log4j.Level;

import de.fhg.iais.kd.tm.obwious.identifiers.WikiOperators;
import de.fhg.iais.kd.tm.obwious.identifiers.WikiParameters;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.ParameterIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.feature.model.CalculateInformativeness;
import de.fhg.iais.kd.tm.obwious.operator.feature.model.CalculatePhraseness;
import de.fhg.iais.kd.tm.obwious.operator.feature.model.CalculateScore;
import de.fhg.iais.kd.tm.obwious.operator.io.ProvideCorpusModelFromFile;
import de.fhg.iais.kd.tm.obwious.operator.meta.Workflow;
import de.fhg.iais.kd.tm.obwious.operator.system.io.ImportCorpusFromDirectory;
import de.fhg.iais.kd.tm.obwious.operator.workflow.ProvideRequiredDataOnCorpusView;
import de.fhg.iais.kd.tm.obwious.operator.workflow.ProvideRequiredDataOnDocument;
import de.fhg.iais.kd.tm.obwious.operator.workflow.collection.LoadTextCorpusView;

public class ExtractKeyphrasesWorkflow extends Workflow {

	private static final long serialVersionUID = 3762190530929039379L;

	private static final String IMPORT_CORPUS_FROM_DIRECTORY = "ImportCorpusFromDirectory";

	public ExtractKeyphrasesWorkflow() {

		this(new Properties());
	}

	public ExtractKeyphrasesWorkflow(Properties properties) {
		super(properties);

		loadModel(properties);

		/** Provide required data of the document */
		this.addOperatorSpecification(
				WikiOperators.PROVIDE_REQUIRED_DATA_ON_DOCUMENT,
				new ProvideRequiredDataOnDocument(properties));

		/** Calculate phraseness */
		this.addOperatorSpecification(WikiOperators.CALCULATE_PHRASENESS,
				CalculatePhraseness.class);

		if (properties.containsKey(WikiParameters.USE_BACKGROUND_CORPUS))
			this.setInputParameter(WikiOperators.CALCULATE_PHRASENESS,
					WikiParameters.DEFAULT_VALUE, Float.parseFloat(properties
							.getProperty(WikiParameters.DEFAULT_VALUE)));

		if (properties.containsKey(WikiParameters.NORMALIZE_PHRASENESS))
			this.setInputParameter(WikiOperators.CALCULATE_PHRASENESS,
					WikiParameters.NORMALIZE_PHRASENESS,
					Boolean.parseBoolean(properties
							.getProperty(WikiParameters.NORMALIZE_PHRASENESS)));

		if (properties.containsKey(WikiParameters.SQUARE_PHRASENESS))
			this.setInputParameter(WikiOperators.CALCULATE_PHRASENESS,
					WikiParameters.SQUARE_PHRASENESS,
					Boolean.parseBoolean(properties
							.getProperty(WikiParameters.SQUARE_PHRASENESS)));

		/** Calculate informativeness */
		this.addOperatorSpecification(WikiOperators.CALCULATE_INFORMATIVENESS,
				CalculateInformativeness.class);

		if (properties.containsKey(WikiParameters.DEFAULT_VALUE))
			this.setInputParameter(WikiOperators.CALCULATE_INFORMATIVENESS,
					WikiParameters.DEFAULT_VALUE, Float.parseFloat(properties
							.getProperty(WikiParameters.DEFAULT_VALUE)));

		if (properties.containsKey(WikiParameters.NORMALIZE_INFORMATIVENESS))
			this.setInputParameter(
					WikiOperators.CALCULATE_INFORMATIVENESS,
					WikiParameters.NORMALIZE_INFORMATIVENESS,
					Boolean.parseBoolean(properties
							.getProperty(WikiParameters.NORMALIZE_INFORMATIVENESS)));

		if (properties.containsKey(WikiParameters.SQUARE_INFORMATIVENESS))
			this.setInputParameter(
					WikiOperators.CALCULATE_INFORMATIVENESS,
					WikiParameters.SQUARE_INFORMATIVENESS,
					Boolean.parseBoolean(properties
							.getProperty(WikiParameters.SQUARE_INFORMATIVENESS)));

		/** Calculate score */
		this.addOperatorSpecification(WikiOperators.CALCULATE_SCORE,
				CalculateScore.class);

		if (properties.containsKey(WikiParameters.PHRASENESS_WEIGHT))
			this.setInputParameter(WikiOperators.CALCULATE_SCORE,
					WikiParameters.PHRASENESS_WEIGHT,
					Float.parseFloat(properties
							.getProperty(WikiParameters.PHRASENESS_WEIGHT)));

		if (properties.containsKey(WikiParameters.INFORMATIVENESS_WEIGHT))
			this.setInputParameter(
					WikiOperators.CALCULATE_SCORE,
					WikiParameters.INFORMATIVENESS_WEIGHT,
					Float.parseFloat(properties
							.getProperty(WikiParameters.INFORMATIVENESS_WEIGHT)));

		// Set output
		this.addDefaultOutputLink(ObjectIdentifiers.CORPUSVIEW);

		addDefaultOutputLink(ObjectIdentifiers.DOCUMENT);
		verify();
	}

	private void loadModel(Properties properties) {
		/** Load previously calculated model */
		String directoryName = (String) properties
				.get(WikiParameters.DIRECTORY);
		File directory = new File(directoryName);
		if (!(directory.exists())) {
			directory = new File(ClassLoader.getSystemResource(directoryName)
					.getFile());
		}
		if (!directory.exists()) {
			logger.log(Level.FATAL, String.format(
					"The given directory '%s' does not exist in package.",
					directoryName));
			throw new RuntimeException();
		}
		if (!directory.isDirectory()) {
			logger.log(Level.FATAL, String.format(
					"The given file '%s' is not a directory.", directoryName));
			throw new RuntimeException();
		}
		directoryName = directory.getAbsolutePath();
		File modelFile = new File(directoryName + "/.model/corpus_model");

		if (modelFile.exists() && modelFile.canRead() && modelFile.isFile()) {
			/** Load corpus */
			this.addOperatorSpecification(IMPORT_CORPUS_FROM_DIRECTORY,
					ImportCorpusFromDirectory.class);
			this.setInputParameter(IMPORT_CORPUS_FROM_DIRECTORY,
					ParameterIdentifiers.DIRECTORY,
					properties.getProperty(WikiParameters.DIRECTORY));

			this.addOperatorSpecification(
					WikiOperators.PROVIDE_CORPUS_MODEL_FROM_FILE,
					ProvideCorpusModelFromFile.class);
			this.setInputParameter(
					WikiOperators.PROVIDE_CORPUS_MODEL_FROM_FILE,
					WikiParameters.MODEL_FILE, modelFile.getAbsolutePath());
		} else {
			/** Load corpus */
			this.propagateProperty(WikiOperators.LOAD_TEXT_COPRUS_VIEW,
					WikiParameters.DIRECTORY);
			this.propagateProperty(WikiOperators.LOAD_TEXT_COPRUS_VIEW,
					WikiParameters.FILE_TYPE);
			this.addOperatorSpecification(WikiOperators.LOAD_TEXT_COPRUS_VIEW,
					LoadTextCorpusView.class);
			/** Provide required data of the corpus */
			this.addOperatorSpecification(
					WikiOperators.PROVIDE_REQUIRED_DATA_ON_COPRUS_VIEW,
					new ProvideRequiredDataOnCorpusView(properties));
		}
	}
}
