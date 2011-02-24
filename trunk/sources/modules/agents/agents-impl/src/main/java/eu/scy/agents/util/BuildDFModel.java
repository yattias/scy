package eu.scy.agents.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Properties;

//import joptsimple.OptionParser;
//import joptsimple.OptionSet;
//import joptsimple.OptionSpec;

import antlr.Utils;
import de.fhg.iais.kd.tm.obwious.operator.ObjectIdentifiers;
import de.fhg.iais.kd.tm.obwious.operator.Operator;
import de.fhg.iais.kd.tm.obwious.operator.meta.DocumentMappingLoop;
import de.fhg.iais.kd.tm.obwious.operator.meta.Workflow;
import de.fhg.iais.kd.tm.obwious.operator.system.model.TrainDocumentFrequencyModel;
import de.fhg.iais.kd.tm.obwious.type.Container;
import eu.scy.agents.keywords.workflow.Preprocessing;

/**
 * A workflow to build a topic model.
 * 
 * @author Florian Schulz
 * 
 */
public class BuildDFModel extends Workflow {
  private static final long serialVersionUID = 1L;

  private static final String PREPROCESSING_LOOP = "PreprocessingLoop";
  private static final String CALCULATE_DOCUMENT_FREQUENCY = "CalculateDF";
  private static final String PREPROCESSING = "Preprocessing";
  private static final String CREATE_COLLECTION = "CreateCollection";
  private static String parserType;
  private static int maxDocuments = -1; // not used; to use it, add option to OptionParser processing

  /**
   * Create a new workflow to build a document frequency model.
   * @throws Exception 
   * 
   */
  public BuildDFModel() throws Exception {
    super();

    if (parserType.equalsIgnoreCase("oneDocPerSentence")) {
      addOperatorSpecification(CREATE_COLLECTION, OneDocPerLineReader.class);
    } else if (parserType.equalsIgnoreCase("oneDocPerFile")){
      addOperatorSpecification(CREATE_COLLECTION, OneDocPerFileReader.class);
    } else {
      throw new Exception("BuildTopicModel - unknown parser type: " + parserType);
    }
		
    addNamespaceLink(CREATE_COLLECTION, TMParameters.INPUT);
    addNamespaceLink(CREATE_COLLECTION, TMParameters.MAX_DOCUMENTS);
    setInputParameter(CREATE_COLLECTION, TMParameters.RANDOM, false);

    DocumentMappingLoop preprocessingLoop = new DocumentMappingLoop();
    addOperatorSpecification(PREPROCESSING_LOOP, preprocessingLoop);
    Properties props = new Properties();
    props.put(TMParameters.REMOVE_HTML_TAGS, "RemoveHTMLTags");
    props.put(TMParameters.PROVIDE_TOKENS, "ProvideTokens");
    props.put(TMParameters.STEM_TOKENS, "StemTokens");
    props.put(TMParameters.REMOVE_STOPWORDS, "RemoveStopwords");

    Preprocessing preprocessor = new Preprocessing(props);
    preprocessingLoop.addOperatorSpecification(PREPROCESSING, preprocessor);
    addLink(PREPROCESSING_LOOP, ObjectIdentifiers.ITERABLE, CREATE_COLLECTION, ObjectIdentifiers.CORPUSVIEW);
		

    addOperatorSpecification(CALCULATE_DOCUMENT_FREQUENCY, TrainDocumentFrequencyModel.class);
    addLink(CALCULATE_DOCUMENT_FREQUENCY, ObjectIdentifiers.CORPUSVIEW, CREATE_COLLECTION,
            ObjectIdentifiers.CORPUSVIEW);

    addDefaultOutputLinks();

    verify();
  }

  /**
   * main.
   * 
   * @param args
   *            arguments
   * @throws Exception 
   */
  @SuppressWarnings("unchecked")
    public static void main(String[] args) throws Exception {

    File outputDir = new File("output/models/df.out");
    String input = "data/CO2background";
    
//    OptionParser optionParser = new OptionParser();
//    OptionSpec<String> inputArgument = optionParser.accepts("i", "InputDirectory").withRequiredArg().ofType(
//                                                                                                            String.class);
//    OptionSpec<String> outputArgument = optionParser.accepts("o", "OutputDirectory").withRequiredArg().ofType(
//                                                                                                              String.class);
//    OptionSpec<String> pt = optionParser.accepts("p", "ParserType").withRequiredArg().ofType(
//                                                                                                              String.class);
//    OptionSet options = optionParser.parse(args);          

//    if (options.has(inputArgument)) {
//      input = options.valueOf(inputArgument);
//    } else {
//      optionParser.printHelpOn(new PrintStream(System.out, true));
//      System.exit(-1);
//    }
//    if (options.has(outputArgument)) {
//      outputDir = new File(options.valueOf(outputArgument));
//    } else {
//      outputDir = new File("output/models/df.out");
//    }
//    if (options.has(pt)) {
//      parserType = options.valueOf(pt);
//    } else {
//      optionParser.printHelpOn(new PrintStream(System.out, true));
//      System.exit(-1)  ;
//    }



    if (!outputDir.exists()) {
      outputDir.mkdirs();
    }

    Operator op = new BuildDFModel().getOperator("Main");
    //		System.out.println(op.toXml());

    op.setInputParameter(TMParameters.INPUT, input);
    op.setInputParameter(TMParameters.DIRECTORY, input);
    op.setInputParameter(TMParameters.OUTPUT_PATH, outputDir.getAbsolutePath());
    op.setInputParameter(TMParameters.PARSER_TYPE, parserType);
    op.setInputParameter(TMParameters.MAX_DOCUMENTS, maxDocuments);
    System.out.println(op.toXml());
    Container output = op.run();

    ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(new File(outputDir, "model.dat")));
    out.writeObject(output);
    out.close();


    logger.info("Finished all");
    System.exit(0);
  }

}
