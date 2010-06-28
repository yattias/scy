package eu.scy.agents.keywords.workflow.operators;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import de.fhg.iais.kd.tm.obwious.JavaClasses;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;
import de.fhg.iais.kd.tm.obwious.operator.DocumentOperatorSpecification;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.type.Container;
import de.fhg.iais.kd.tm.obwious.util.Assert;

/**
 * Takes the feature specified in parameter baseText (default:text), which is a single string and
 * generates a feature tokens, which is represented as a list. The delimiters are the standard Java
 * delimiters " \n\t\r\f", but can also be specified via a parameter.
 * 
 * @author Florian Schulz
 */
public class ImprovedTokenize extends DocumentOperatorSpecification {

	/**
	 * Should documents really be updated. true, false
	 */
	public static final String UPDATE = "update";
	/**
	 * Remove Numbers from texts. true, false
	 */
	public static final String REMOVE_NUMBERS = "removeNumbers";
	/**
	 * Text source. Feature name
	 */
	public static final String BASE_TEXT = "baseText";

	/**
	 * Delimiter by which to split. \n\t\r\f:
	 */
	public static final String DELIMITER = "delimiter";
	/**
	 * convert tokens to lower case. true, false
	 */
	public static final String TO_LOWER = "toLower";
	/**
	 * remove punctuation as is .,:;!?""... True, false.
	 */
	public static final String REMOVE_PUNCTUATION = "removePunctuation";
	/**
	 * Yeah, whatever.
	 */
	public static final long serialVersionUID = 1L;

	/**
	 * Construct new Operator that tokenizes Text.
	 */
	public ImprovedTokenize() {
		super();
		this.addParameterType(BASE_TEXT, JavaClasses.STRING, false, Features.TEXT);
		this.addParameterType(DELIMITER, JavaClasses.STRING, false, " \n\t\r\f:");
		this.addParameterType(TO_LOWER, JavaClasses.BOOLEAN, false, Boolean.FALSE);
		this.addParameterType(REMOVE_PUNCTUATION, JavaClasses.BOOLEAN, false, Boolean.TRUE);
		this.addParameterType(REMOVE_NUMBERS, JavaClasses.BOOLEAN, false, Boolean.TRUE);
		this.addParameterType(UPDATE, JavaClasses.BOOLEAN, false, Boolean.FALSE);
	}

	@Override
	public Container run(Container inputParameters) {

		Document document = (Document) inputParameters.getObject(ObjectIdentifiers.DOCUMENT);
		// logger.info("Tokenizing document " + document.getId());

		String baseText = (String) inputParameters.getObject(BASE_TEXT);
		String delimiters = (String) inputParameters.getObject(DELIMITER);
		Boolean removePunctuation = (Boolean) inputParameters.getObject(REMOVE_PUNCTUATION);
		Boolean removeNumbers = (Boolean) inputParameters.getObject(REMOVE_NUMBERS);
		Boolean toLower = (Boolean) inputParameters.getObject(TO_LOWER);
		Boolean update = (Boolean) inputParameters.getObject(UPDATE);

		Container output = new Container(getOutputSignature());

		// Define feature
		if (!Features.getInstance().isFeature(Features.TOKENS)) {
			Features.getInstance().addFeature(Features.TOKENS, JavaClasses.LIST);
		} else {
			Assert.areSame(Features.getInstance().getType(Features.TOKENS), JavaClasses.LIST);
		}

		// Compute feature
		if (!document.hasFeature(Features.TOKENS) || update) {

			// Required features
			Assert.isTrue(Features.getInstance().isFeature(baseText));
			Assert.isTrue(document.hasFeature(baseText));

			String text = (String) document.getFeature(baseText);

			List<String> tokens = new ArrayList<String>();

			StringTokenizer tokenizer = new StringTokenizer(text, delimiters);
			String token;
			while (tokenizer.hasMoreTokens()) {
				token = tokenizer.nextToken();
				if (removePunctuation) {
					token = token.replaceAll("[\\(\\)«»,;\\.:\\?\\!\\-\\*=+%/@`�'���\"]", "");
				}
				if (token.length() < 2) {
					continue;
				}
				if ("".equals(token.trim())) {
					continue;
				}
				if (removeNumbers) {
					if (token.matches("[0-9]*")) {
						continue;
					}
					// boolean containsDigit = false;
					// for (int i = 0; i < token.length(); i++) {
					// if (Character.isDigit(token.charAt(i))) {
					// containsDigit = true;
					// break;
					// }
					// }
					// if (containsDigit) {
					// continue;
					// }
				}
				if (toLower) {
					tokens.add(token.toLowerCase().trim());
				} else {
					tokens.add(token.trim());
				}
			}

			document.setFeature(Features.TOKENS, tokens);
		}

		output.setObject(ObjectIdentifiers.DOCUMENT, document);

		return output;
	}
}
