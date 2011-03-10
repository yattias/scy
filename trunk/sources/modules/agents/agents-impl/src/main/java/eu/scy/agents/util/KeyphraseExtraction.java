package eu.scy.agents.util;

import java.io.File;
import java.net.URI;
import java.util.Properties;

import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.identifiers.WikiFeatures;
import de.fhg.iais.kd.tm.obwious.identifiers.WikiOperators;
import de.fhg.iais.kd.tm.obwious.identifiers.WikiParameters;
import de.fhg.iais.kd.tm.obwious.identifiers.WikiValues;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.Operator;
import de.fhg.iais.kd.tm.obwious.operator.feature.atomic.ProvideCSV;
import de.fhg.iais.kd.tm.obwious.operator.meta.Workflow;
import de.fhg.iais.kd.tm.obwious.operator.workflow.ProvideKeyphrasesOnSeparateDocument;
import de.fhg.iais.kd.tm.obwious.type.Container;

public class KeyphraseExtraction {

	public static void main(String[] args) throws Throwable {

		URI corpus = ClassLoader.getSystemResource("mission1_texts/English").toURI();
		URI document = ClassLoader.getSystemResource("mission1_texts/English/new02.txt").toURI();
		
//        URI corpus = ClassLoader.getSystemResource("mission2_texts/English").toURI();
//        URI document = ClassLoader.getSystemResource("mission2_texts/English/acid.rain.txt").toURI();
		
//        URI corpus = ClassLoader.getSystemResource("mission3_texts/English").toURI();
//        URI document = ClassLoader.getSystemResource("mission3_texts/English/wikipedia.Fat.txt").toURI();

      Workflow workflow = new Workflow(), keyphraseWorkflow;      
		File corpusDirectory = (args.length > 0) ? new File(args[0]) : new File(corpus);
		File documentFile = (args.length > 1) ? new File(args[1]) : new File(document);

		if (!documentFile.exists()) {
			documentFile = null;
		}

		WikiFeatures.addFeatures();

		Properties workflowProperties = new Properties();

		workflowProperties.setProperty(WikiParameters.DIRECTORY, corpusDirectory.getAbsolutePath());
		
		/* PROPERTIES ------------------------ */
		
		/** file type for documents (WikiValues.FILE_TYPE_CAS or WikiValues.FILE_TYPE_TXT) */
		workflowProperties.setProperty(WikiParameters.FILE_TYPE, WikiValues.FILE_TYPE_CAS);

		/** minimum length that a token must have */
		workflowProperties.setProperty(WikiParameters.MIN_TOKEN_LENGTH, new Integer(1).toString());
		/** indicator of whether the tokens should be transformed to lower case */
		workflowProperties.setProperty(WikiParameters.TO_LOWER, Boolean.FALSE.toString());

		/** minimum number of tokens that a n-gram must have */
		workflowProperties.setProperty(WikiParameters.MIN_NGRAM_TOKENS, new Integer(2).toString());
		/** maximum number of tokens that a n-gram can have */
		workflowProperties.setProperty(WikiParameters.MAX_NGRAM_TOKENS, new Integer(5).toString());
		/** minimum number of appearances in the document a n-gram must have */
		workflowProperties.setProperty(WikiParameters.MIN_FREQUENCY, new Integer(2).toString());
		/** indicator of whether the tokens of a n-gram might be separated by a delimiter (like . or ,) */
		workflowProperties.setProperty(WikiParameters.IGNORE_DELIMITERS, Boolean.FALSE.toString());
		/** indicator of whether the n-gram might begin or end with a stopword token */
		workflowProperties.setProperty(WikiParameters.IGNORE_STOPWORDS, Boolean.FALSE.toString());

		/** use corpus model instead of document model */
		workflowProperties.setProperty(WikiParameters.USE_BACKGROUND_CORPUS, Boolean.FALSE.toString());
		/** normalize all phraseness values */
		workflowProperties.setProperty(WikiParameters.NORMALIZE_PHRASENESS, Boolean.FALSE.toString());
		/** square phraseness values to avoid negative values */
		workflowProperties.setProperty(WikiParameters.SQUARE_PHRASENESS, Boolean.FALSE.toString());


		/** default value for informativeness, if the n-gram does not appear in the corpus */
		workflowProperties.setProperty(WikiParameters.DEFAULT_VALUE, new Float(0.0f).toString());
		/** normalize all informativeness values */
		workflowProperties.setProperty(WikiParameters.NORMALIZE_INFORMATIVENESS, Boolean.FALSE.toString());
		/** square informativeness values to avoid negative values */
		workflowProperties.setProperty(WikiParameters.SQUARE_INFORMATIVENESS, Boolean.FALSE.toString());


		/** phraseness weight in the final score */
		workflowProperties.setProperty(WikiParameters.PHRASENESS_WEIGHT, new Float(0.5f).toString());
		/** informativeness weight in the final score */
		workflowProperties.setProperty(WikiParameters.INFORMATIVENESS_WEIGHT, new Float(0.5f).toString());
		
		workflowProperties.setProperty(WikiParameters.DOCUMENT_LINK, documentFile.getAbsolutePath());
		keyphraseWorkflow = new ProvideKeyphrasesOnSeparateDocument(workflowProperties);

		workflow.addOperatorSpecification("ProvideKeyphrasesRequirements", keyphraseWorkflow);
		workflow.addOperatorSpecification(WikiOperators.PROVIDE_CSV, ProvideCSV.class);
		workflow.addDefaultOutputLink(ObjectIdentifiers.DOCUMENT);

		workflow.verify();

		Operator operator = workflow.getOperator("KeyphraseExtraction");
		Container output = operator.run();

		Document d = (Document) output.getObject(ObjectIdentifiers.DOCUMENT);
		System.out.println(d.toString());
		
		System.err.println("Done!");
	}

}
