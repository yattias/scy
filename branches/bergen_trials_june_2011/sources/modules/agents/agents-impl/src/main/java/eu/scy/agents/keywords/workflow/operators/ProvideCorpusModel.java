package eu.scy.agents.keywords.workflow.operators;

import de.fhg.iais.kd.tm.obwious.JavaClasses;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.CarrierClasses;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Corpus;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.CorpusView;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.MemoryCorpus;
import de.fhg.iais.kd.tm.obwious.identifiers.WikiFeatures;
import de.fhg.iais.kd.tm.obwious.model.CorpusModel;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.OperatorSpecification;
import de.fhg.iais.kd.tm.obwious.type.Container;
import de.fhg.iais.kd.tm.obwious.util.Assert;
import eu.scy.agents.keywords.workflow.KeywordWorkflowConstants;

public class ProvideCorpusModel extends OperatorSpecification {

  private static final long serialVersionUID = 1L;

  public ProvideCorpusModel() {
    super();
    addInputType(KeywordWorkflowConstants.KEYPHRASE_MODEL, CorpusModel.class);
    addOutputType(ObjectIdentifiers.CORPUSVIEW, CarrierClasses.CORPUSVIEW);
  }

  @Override
  protected Container run(Container inputParameters) {

    Corpus corpus = new MemoryCorpus();
    CorpusView corpusView = new CorpusView(corpus, false);
    
    Container output = new Container(this.getOutputSignature());

    // Define feature
    if (!Features.getInstance().isFeature(WikiFeatures.TOKEN_FREQUENCIES)) {
      Features.getInstance().addFeature(WikiFeatures.TOKEN_FREQUENCIES, JavaClasses.MAP);
    } else {
      Assert.areSame(Features.getInstance().getType(WikiFeatures.TOKEN_FREQUENCIES),
                     JavaClasses.MAP);
    }
    if (!Features.getInstance().isFeature(WikiFeatures.NGRAM_FREQUENCIES)) {
      Features.getInstance().addFeature(WikiFeatures.NGRAM_FREQUENCIES, JavaClasses.MAP);
    } else {
      Assert.areSame(Features.getInstance().getType(WikiFeatures.NGRAM_FREQUENCIES),
                     JavaClasses.MAP);
    }
    if (!Features.getInstance().isFeature(WikiFeatures.TOKEN_MODEL)) {
      Features.getInstance().addFeature(WikiFeatures.TOKEN_MODEL, JavaClasses.MAP);
    } else {
      Assert.areSame(Features.getInstance().getType(WikiFeatures.TOKEN_MODEL), JavaClasses.MAP);
    }
    if (!Features.getInstance().isFeature(WikiFeatures.NGRAM_MODEL)) {
      Features.getInstance().addFeature(WikiFeatures.NGRAM_MODEL, JavaClasses.MAP);
    } else {
      Assert.areSame(Features.getInstance().getType(WikiFeatures.NGRAM_MODEL), JavaClasses.MAP);
    }

    // Compute feature
    CorpusModel corpusModel = (CorpusModel) inputParameters.getObject(KeywordWorkflowConstants.KEYPHRASE_MODEL);

    corpusView.setFeature(WikiFeatures.TOKEN_FREQUENCIES, corpusModel.getTokenFrequencies());
    corpusView.setFeature(WikiFeatures.TOKEN_MODEL, corpusModel.getTokenModel());

    corpusView.setFeature(WikiFeatures.NGRAM_FREQUENCIES, corpusModel.getNgramFrequencies());
    corpusView.setFeature(WikiFeatures.NGRAM_MODEL, corpusModel.getNgramModel());

    output.setObject(ObjectIdentifiers.CORPUSVIEW, corpusView);

    System.err.println("prepared corpus model");

    return output;
  }
}
