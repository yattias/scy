package eu.scy.agents.keywords.workflow.operators;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import de.fhg.iais.kd.tm.obwious.JavaClasses;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;
import de.fhg.iais.kd.tm.obwious.operator.DocumentOperatorSpecification;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.ParameterIdentifiers;
import de.fhg.iais.kd.tm.obwious.type.Container;
import de.fhg.iais.kd.tm.obwious.util.Assert;

/**
 * Removes stopwords from the tokenlist of this document. The stopwords are read from a file and matched against every
 * token. Tokens that are contained in the stopword list are removed from the token feature.
 * <ul>
 * <li><b>Inputs</b></li>
 * <ul>
 * <li>Document(Document): The input document.</li>
 * </ul>
 * <li><b>Parameters</b></li>
 * <ul>
 * <li>delimiter(String): The delimiter that separates the stopwords in the file (default ,).</li>
 * <li>fileName(String): The file that contains the stopwords in one word per line format or separated by
 * <code>delimiter</code>.</li>
 * </ul>
 * <li><b>Outputs</b></li>
 * <ul>
 * <li>Document(Document): The enriched document.</li>
 * </ul>
 * </ul>
 * 
 * @author Florian Schulz
 */
public class RemoveStopWords extends DocumentOperatorSpecification {

	private static final long serialVersionUID = 1L;

	public RemoveStopWords() {
		super();
		this.addParameterType(ParameterIdentifiers.FILENAME, JavaClasses.STRING, false, null);// ClassLoader.getSystemResource(
		// "english_stopWords.txt").toString());
		this.addParameterType("delimiter", JavaClasses.STRING, false, ",");
	}

	@Override
	protected Container run(Container inputParameters) {
		Container output = new Container(getOutputSignature());
		Document document = (Document) inputParameters.getObject(ObjectIdentifiers.DOCUMENT);
		String path = (String) inputParameters.get(ParameterIdentifiers.FILENAME);
		String delimiter = (String) inputParameters.get("delimiter");

		Scanner scanner = null;
		InputStream inStream = null;
		if (path == null) {
			inStream = getClass().getResourceAsStream("english_stopWords.txt");
			if (inStream == null) {
				logger.warn("no stopwords removed");
				output.setObject(ObjectIdentifiers.DOCUMENT, document);
				return output;
			}
		} else {
			try {
				inStream = new FileInputStream(path);
			} catch (FileNotFoundException e) {
				logger.warn("no stopwords removed");
				output.setObject(ObjectIdentifiers.DOCUMENT, document);
				return output;
			}
		}
		scanner = new Scanner(inStream).useDelimiter(delimiter);

		if (document.hasFeature(Features.TOKENS)) {
			List<String> tokens = document.getFeature(Features.TOKENS);
			List<String> stopWords = new ArrayList<String>();

			File stopWordFile = new File(path);

			Assert.isTrue(stopWordFile.exists());

			while (scanner.hasNext()) {
				stopWords.add(scanner.next().trim());
			}

			tokens.removeAll(stopWords);

			document.setFeature(Features.TOKENS, tokens);
		}

		output.setObject(ObjectIdentifiers.DOCUMENT, document);

		return output;
	}
}
