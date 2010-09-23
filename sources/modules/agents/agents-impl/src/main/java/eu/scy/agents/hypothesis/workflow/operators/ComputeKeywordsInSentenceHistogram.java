package eu.scy.agents.hypothesis.workflow.operators;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import util.Utilities;

import de.fhg.iais.kd.tm.obwious.JavaClasses;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Document;
import de.fhg.iais.kd.tm.obwious.base.featurecarrier.Features;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.OperatorSpecification;
import de.fhg.iais.kd.tm.obwious.type.Container;
import eu.scy.agents.keywords.workflow.KeywordWorkflowConstants;

/**
 * computes a histogram of number of keywords per sentence:
 * 
 * 
 * @author Jörg Kindermann
 */
public class ComputeKeywordsInSentenceHistogram extends OperatorSpecification {

  /**
   * Should documents really be updated. true, false
   */
  public static final String UPDATE = "update";

  public static final String HISTOGRAM = "histogram";

  /**
   * Construct new Operator that spots keywords in sentences and computes a keyword-in-sentence
   * ratio.
   */
  public ComputeKeywordsInSentenceHistogram() {
    super();
    this.addParameterType(UPDATE, JavaClasses.BOOLEAN, false, Boolean.TRUE);
    this.addInputType(ObjectIdentifiers.DOCUMENT, Document.class);
    this.addOutputType(HISTOGRAM, HashMap.class);
  }

  @Override
  public Container run(Container inputParameters) {

    Document document = (Document) inputParameters.getObject(ObjectIdentifiers.DOCUMENT);
    if (!Features.getInstance().isFeature(HISTOGRAM)) {
      Features.getInstance().addFeature(HISTOGRAM, Map.class);
    }
    ArrayList<String> sentences = (ArrayList<String>) document.getFeature(Features.SENTENCES);
    ArrayList<String> keywords = (ArrayList<String>) document.getFeature(Features.WORDS);
    Boolean update = (Boolean) inputParameters.getObject(UPDATE);

    Iterator<String> it = sentences.iterator();
    HashMap<Integer, Integer> histogram = new HashMap<Integer, Integer>();
    while (it.hasNext()) {
      String sentString = (String) it.next();
      String[] sentence = Utilities.tokenize(sentString);
      int count = 0;
      for (int i = 0; i < sentence.length; i++) {
        String term = sentence[i];
        if (keywords.contains(term)) {
          count += 1;
        }
      }
      if (histogram.containsKey(count)) {
        histogram.put(count, histogram.get(count) + 1);
      } else {
        histogram.put(count, 1);
      }
    }
    document.setFeature(HISTOGRAM, histogram);
    Container output = new Container(getOutputSignature());
    output.setObject(ObjectIdentifiers.DOCUMENT, document);

    return output;
  }

}
