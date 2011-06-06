package eu.scy.agents.keywords.workflow;

import java.util.Properties;

import de.fhg.iais.kd.tm.obwious.identifiers.WikiOperators;
import de.fhg.iais.kd.tm.obwious.identifiers.WikiParameters;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.feature.model.CalculateInformativeness;
import de.fhg.iais.kd.tm.obwious.operator.feature.model.CalculatePhraseness;
import de.fhg.iais.kd.tm.obwious.operator.feature.model.CalculateScore;
import de.fhg.iais.kd.tm.obwious.operator.meta.Workflow;
import de.fhg.iais.kd.tm.obwious.operator.workflow.ProvideRequiredDataOnDocument;
import eu.scy.agents.keywords.workflow.operators.ProvideCorpusModel;

public class ExtractKeyphrasesWorkflow extends Workflow {

  private static final long serialVersionUID = 3762190530929039379L;

  public ExtractKeyphrasesWorkflow() {

    this(new Properties());
  }

  public ExtractKeyphrasesWorkflow(Properties properties) {
    super(properties);

    this.addOperatorSpecification(WikiOperators.PROVIDE_CORPUS_MODEL, ProvideCorpusModel.class);

    /** Provide required data of the document */
    this.addOperatorSpecification(WikiOperators.PROVIDE_REQUIRED_DATA_ON_DOCUMENT,
                                  new ProvideRequiredDataOnDocument(properties));

    /** Calculate phraseness */
    this.addOperatorSpecification(WikiOperators.CALCULATE_PHRASENESS, CalculatePhraseness.class);

    if (properties.containsKey(WikiParameters.USE_BACKGROUND_CORPUS))
      this.setInputParameter(WikiOperators.CALCULATE_PHRASENESS, WikiParameters.DEFAULT_VALUE,
                             Float.parseFloat(properties.getProperty(WikiParameters.DEFAULT_VALUE)));

    if (properties.containsKey(WikiParameters.NORMALIZE_PHRASENESS))
      this.setInputParameter(WikiOperators.CALCULATE_PHRASENESS,
                             WikiParameters.NORMALIZE_PHRASENESS,
                             Boolean.parseBoolean(properties.getProperty(WikiParameters.NORMALIZE_PHRASENESS)));

    if (properties.containsKey(WikiParameters.SQUARE_PHRASENESS))
      this.setInputParameter(WikiOperators.CALCULATE_PHRASENESS,
                             WikiParameters.SQUARE_PHRASENESS,
                             Boolean.parseBoolean(properties.getProperty(WikiParameters.SQUARE_PHRASENESS)));

    /** Calculate informativeness */
    this.addOperatorSpecification(WikiOperators.CALCULATE_INFORMATIVENESS,
                                  CalculateInformativeness.class);

    if (properties.containsKey(WikiParameters.DEFAULT_VALUE))
      this.setInputParameter(WikiOperators.CALCULATE_INFORMATIVENESS, WikiParameters.DEFAULT_VALUE,
                             Float.parseFloat(properties.getProperty(WikiParameters.DEFAULT_VALUE)));

    if (properties.containsKey(WikiParameters.NORMALIZE_INFORMATIVENESS))
      this.setInputParameter(WikiOperators.CALCULATE_INFORMATIVENESS,
                             WikiParameters.NORMALIZE_INFORMATIVENESS,
                             Boolean.parseBoolean(properties.getProperty(WikiParameters.NORMALIZE_INFORMATIVENESS)));

    if (properties.containsKey(WikiParameters.SQUARE_INFORMATIVENESS))
      this.setInputParameter(WikiOperators.CALCULATE_INFORMATIVENESS,
                             WikiParameters.SQUARE_INFORMATIVENESS,
                             Boolean.parseBoolean(properties.getProperty(WikiParameters.SQUARE_INFORMATIVENESS)));

    /** Calculate score */
    this.addOperatorSpecification(WikiOperators.CALCULATE_SCORE, CalculateScore.class);

    if (properties.containsKey(WikiParameters.PHRASENESS_WEIGHT))
      this.setInputParameter(WikiOperators.CALCULATE_SCORE,
                             WikiParameters.PHRASENESS_WEIGHT,
                             Float.parseFloat(properties.getProperty(WikiParameters.PHRASENESS_WEIGHT)));

    if (properties.containsKey(WikiParameters.INFORMATIVENESS_WEIGHT))
      this.setInputParameter(WikiOperators.CALCULATE_SCORE,
                             WikiParameters.INFORMATIVENESS_WEIGHT,
                             Float.parseFloat(properties.getProperty(WikiParameters.INFORMATIVENESS_WEIGHT)));

    // Set output
    this.addDefaultOutputLink(ObjectIdentifiers.CORPUSVIEW);

    addDefaultOutputLink(ObjectIdentifiers.DOCUMENT);
    verify();
  }

}
