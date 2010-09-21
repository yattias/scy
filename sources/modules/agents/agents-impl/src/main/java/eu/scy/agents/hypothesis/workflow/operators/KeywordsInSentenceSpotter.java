package eu.scy.agents.hypothesis.workflow.operators;

import java.util.List;

import de.fhg.iais.kd.tm.obwious.JavaClasses;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.OperatorSpecification;
import de.fhg.iais.kd.tm.obwious.type.Container;

/**
 * Takes the feature specified in parameter baseText (default:text), which is a
 * single string and generates a feature tokens, which is represented as a list.
 * The delimiters are regular expressions which have to be applied in a special
 * order
 * 
 * @author Jörg Kindermann
 */
public class KeywordsInSentenceSpotter extends OperatorSpecification {

	/**
	 * Should documents really be updated. true, false
	 */
	public static final String UPDATE = "update";
	public static final String HISTOGRAM = "histogram";

	/**
	 * Construct new Operator that spots keywords in sentences and computes a
	 * keyword-in-sentence ratio.
	 */
	public KeywordsInSentenceSpotter() {
		super();
		this.addParameterType(UPDATE, JavaClasses.BOOLEAN, false, Boolean.TRUE);
		this.addInputType(ObjectIdentifiers.DOCUMENT, Document.class);
		this.addOutputType(HISTOGRAM, List.class);
	}

	@Override
	public Container run(Container inputParameters) {

		Document document = (Document) inputParameters
				.getObject(ObjectIdentifiers.DOCUMENT);
		// logger.info("Tokenizing document to sentence tokens" +
		// document.getId());

		List sentences = (List) document.getFeature(Features.SENTENCES);
		Boolean update = (Boolean) inputParameters.getObject(UPDATE);

		Container output = new Container(getOutputSignature());
		// analyze document....
		output.setObject(ObjectIdentifiers.DOCUMENT, document);

		return output;
	}

	private String[] tokenize(String text) {
		String regex = "\\s+";
		String[] sent = text.split(regex);
		return sent;
	}
}
