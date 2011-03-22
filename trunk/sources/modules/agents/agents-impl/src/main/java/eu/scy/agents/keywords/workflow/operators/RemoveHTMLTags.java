package eu.scy.agents.keywords.workflow.operators;

import java.util.Properties;

import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;
import de.fhg.iais.kd.tm.obwious.identifiers.TMParameters;
import de.fhg.iais.kd.tm.obwious.operator.DocumentOperatorSpecification;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.type.Container;

/**
 * Implements an OBWIOUS operator that
 * <ul>
 * <li><b>Inputs</b></li>
 * <ul>
 * <li>ObjectId(ObjectType): Description.</li>
 * </ul>
 * <li><b>Parameters</b></li>
 * <ul>
 * <li>ParameterId(ParameterType): Description including range, requirement, etc.</li>
 * </ul>
 * <li><b>Outputs</b></li>
 * <ul>
 * <li>ObjectId(ObjectType): Description</li>
 * </ul>
 * </ul>
 * 
 * @author fschulz
 */
public class RemoveHTMLTags extends DocumentOperatorSpecification {

	private static final long serialVersionUID = 1L;

	public RemoveHTMLTags() {
		this(new Properties());
	}

	public RemoveHTMLTags(Properties properties) {
		super(properties);
	}

	@Override
	protected Container run(Container inputParameters) {
		Container output = new Container(getOutputSignature());
		Document document = (Document) inputParameters.get(ObjectIdentifiers.DOCUMENT);

		if (document.hasFeature(Features.TEXT)) {

			String text = document.getFeature(Features.TEXT);
			String tagPattern = "<("+ TMParameters.WORD_PATTERN +")*?\\s*?("+ TMParameters.WORD_PATTERN +")*?>";
			String noHTMLString = text.replaceAll(tagPattern, " ");
			String noWhitespaces = noHTMLString.replaceAll("\\s+", " ");

			document.setFeature(Features.TEXT, noWhitespaces);
		}
		output.put(ObjectIdentifiers.DOCUMENT, document);
		return output;
	}
}
