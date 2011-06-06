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
import eu.scy.agents.util.TMParameters;


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


	private static final long serialVersionUID = 1L;


	public Preprocessing() {	  
		this(new Properties());
	}

  public Preprocessing(Properties properties) {
    super(properties);
    if (properties.isEmpty()) { // set default properties
      properties.put(TMParameters.REMOVE_HTML_TAGS, "RemoveHTMLTags");
      properties.put(TMParameters.PROVIDE_TERM_FREQUENCY, "ProvideTermFrequency");
      properties.put(TMParameters.PROVIDE_TOKENS, "ProvideTokens");
      properties.put(TMParameters.STEM_TOKENS, "StemTokens");
      properties.put(TMParameters.REMOVE_STOPWORDS, "RemoveStopwords");
    }
    if (properties.containsKey(TMParameters.REMOVE_HTML_TAGS)) {
      addOperatorSpecification(TMParameters.REMOVE_HTML_TAGS, RemoveHTMLTags.class);
    }

    if (properties.containsKey(TMParameters.PROVIDE_TOKENS)) {
      addOperatorSpecification(TMParameters.PROVIDE_TOKENS, ImprovedTokenize.class);
      setInputParameter(TMParameters.PROVIDE_TOKENS, "toLower", true);
      setInputParameter(TMParameters.PROVIDE_TOKENS, "removePunctuation", true);
    }
    if (properties.containsKey(TMParameters.REMOVE_STOPWORDS)) {
      addOperatorSpecification(TMParameters.REMOVE_STOPWORDS, RemoveStopWords.class);
    }
    if (properties.containsKey(TMParameters.STEM_TOKENS)) {
      addOperatorSpecification(TMParameters.STEM_TOKENS, StemTokens.class);
      setInputParameter(TMParameters.STEM_TOKENS, StemTokens.CREATE_MAPPING, true);
    }
    if (properties.containsKey(TMParameters.PROVIDE_TERM_FREQUENCY)) {
      addOperatorSpecification(TMParameters.PROVIDE_TERM_FREQUENCY, ProvideTermFrequency.class);

      addOperatorSpecification(KeywordWorkflowConstants.TFIDF, ProvideTfIdf.class);

      addNamespaceLink(KeywordWorkflowConstants.TFIDF, KeywordWorkflowConstants.DOCUMENT_FREQUENCY);

      addNamespaceLink(KeywordWorkflowConstants.TFIDF, ObjectIdentifiers.MODEL,
                       KeywordWorkflowConstants.DOCUMENT_FREQUENCY_MODEL);
    }
    addDefaultOutputLinks();
    verify();
  }

}
