package eu.scy.agents.keywords.workflow;

import java.util.Properties;

import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.meta.Workflow;
import de.fhg.iais.kd.tm.obwious.operator.system.feature.atomic.ProvideTermFrequency;
import de.fhg.iais.kd.tm.obwious.operator.system.feature.modelbased.ProvideTfIdf;
import eu.scy.agents.keywords.workflow.operators.ImprovedTokenize;
import eu.scy.agents.keywords.workflow.operators.RemoveHTMLTags;
import eu.scy.agents.keywords.workflow.operators.RemoveStopWords;
import eu.scy.agents.keywords.workflow.operators.StemTokens;

/**
 * Implements an OBWIOUS operator that
 * <ul>
 * <li><b>Inputs</b></li>
 * <ul>
 * <li>ObjectId(ObjectType): Description.</li>
 * </ul>
 * <li><b>Parameters</b></li>
 * <ul>
 * <li>ParameterId(ParameterType): Description including range, requirement,
 * etc.</li>
 * </ul>
 * <li><b>Outputs</b></li>
 * <ul>
 * <li>ObjectId(ObjectType): Description</li>
 * </ul>
 * </ul>
 * 
 * @author fschulz
 */
public class Preprocessing extends Workflow {

	private static final String REMOVE_HTML_TAGS = "RemoveHTMLTags";

	private static final long serialVersionUID = 1L;

	private static final String PROVIDE_TERM_FREQUENCY = "ProvideTermFrequency";
	private static final String PROVIDE_TOKENS = "ProvideTokens";
	private static final String STEM_TOKENS = "StemTokens";
	private static final String REMOVE_STOPWORDS = "RemoveStopwords";

	public Preprocessing() {
		this(new Properties());
	}

	public Preprocessing(Properties properties) {
		super(properties);

		addOperatorSpecification(REMOVE_HTML_TAGS, RemoveHTMLTags.class);

		addOperatorSpecification(PROVIDE_TOKENS, ImprovedTokenize.class);
		setInputParameter(PROVIDE_TOKENS, "toLower", true);
		setInputParameter(PROVIDE_TOKENS, "removePunctuation", true);
		addOperatorSpecification(REMOVE_STOPWORDS, RemoveStopWords.class);

		addOperatorSpecification(STEM_TOKENS, StemTokens.class);
		setInputParameter(STEM_TOKENS, StemTokens.CREATE_MAPPING, true);
		addOperatorSpecification(PROVIDE_TERM_FREQUENCY,
				ProvideTermFrequency.class);

		addOperatorSpecification(KeywordWorkflowConstants.TFIDF,
				ProvideTfIdf.class);

		addNamespaceLink(KeywordWorkflowConstants.TFIDF,
				KeywordWorkflowConstants.DOCUMENT_FREQUENCY);

		addNamespaceLink(KeywordWorkflowConstants.TFIDF,
				ObjectIdentifiers.MODEL,
				KeywordWorkflowConstants.DOCUMENT_FREQUENCY_MODEL);

		addDefaultOutputLinks();
		verify();
	}

}
