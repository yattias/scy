package eu.scy.agents.topics;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectOutputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.topics.ParallelTopicModel;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import eu.scy.agents.impl.PersistentStorage;
import eu.scy.agents.util.Preprocessor;

public class TopicModelTool {

  private static final String TOPIC_MODEL_NAME_SUFFIX = "_tm";

  private String topicModelName;

  private ParallelTopicModel topicModel;

  public Preprocessor preprocessor;

  public TopicModelTool() {
    preprocessor = new Preprocessor();
  }

  public void createTopicModel(Reader[] files, String language, String mission, int numberOfTopics) {

    topicModelName = createTopicModelName(language, mission);

    String[] documents = readDocuments(files);

    String[][] tokenizedDocuments = preprocessDocuments(documents, language);

    InstanceList instanceList = createInstanceList(tokenizedDocuments);

    learnTopicModel(numberOfTopics, instanceList);
  }

  private String[][] preprocessDocuments(String[] documents, String language) {
    String[][] tokenizedDocuments = new String[documents.length][];
    for (int i = 0; i < documents.length; i++) {
      String doc = documents[i];
      String[] newTokens = preprocessor.preprocessDocument(doc);
      tokenizedDocuments[i] = preprocessor.removeStopwords(newTokens, language);
    }
    return tokenizedDocuments;
  }

private void learnTopicModel(int numberOfTopics, InstanceList instanceList) {
    topicModel = new ParallelTopicModel(numberOfTopics);
    topicModel.addInstances(instanceList);
    topicModel.setNumIterations(2000);
    topicModel.setBurninPeriod(1500);
    topicModel.setTopicDisplay(0, 0);
    try {
      topicModel.estimate();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  private InstanceList createInstanceList(String[][] documents) {
    Pipe pipe = new TokenSequence2FeatureSequence();
    InstanceList instanceList = new InstanceList(pipe);

    for (int i = 0; i < documents.length; i++) {
      Instance instance = preprocessor.createInstanceFromTokens("" + i, documents[i]);
      instanceList.addThruPipe(instance);
    }
    return instanceList;
  }

  private String[] readDocuments(Reader[] files) {
    String[] documents = new String[files.length];
    for (int i = 0; i < files.length; i++) {
      try {
        documents[i] = convertInputStreamToString(files[i]);
      } catch (IOException e) {
        e.printStackTrace();
        documents[i] = null;
      }
    }
    return documents;
  }

  public ParallelTopicModel getTopicModel() {
    return topicModel;
  }

  public String getName() {
    return topicModelName;
  }

  private String createTopicModelName(String language, String mission) {
    return mission + "_" + language + TOPIC_MODEL_NAME_SUFFIX;
  }

  private static String convertInputStreamToString(Reader r) throws IOException {
    if (r != null) {
      StringBuilder sb = new StringBuilder();
      String line;

      try {
        BufferedReader reader = new BufferedReader(r);
        while ((line = reader.readLine()) != null) {
          sb.append(line).append("\n");
        }
      } finally {
        r.close();
      }
      return sb.toString();
    } else {
      return "";
    }
  }

  public void upload(String host, int port, String key, ParallelTopicModel model)
      throws IOException {
    PersistentStorage storage = new PersistentStorage(host, port);
    ByteArrayOutputStream bytesOut = new ByteArrayOutputStream();
    ObjectOutputStream objectOut = new ObjectOutputStream(bytesOut);
    objectOut.writeObject(model);
    storage.put(key, bytesOut.toByteArray());
  }

  public static void main(String[] args) throws IOException {
//    InputStream in = TopicModelTool.class.getResourceAsStream("/mission1_texts/English/content.lst");
//    InputStream in = TopicModelTool.class.getResourceAsStream("/mission2_texts/English/content.lst");
    InputStream in = TopicModelTool.class.getResourceAsStream("/mission3_texts/English/content.lst");
    // .getResourceAsStream("/mission1_texts/Estonian/content.lst");
    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
    String line = "";
    List<Reader> inputStreams = new ArrayList<Reader>();
    while ((line = reader.readLine()) != null) {
      inputStreams.add(new InputStreamReader(
//                                             TopicModelTool.class.getResourceAsStream("/mission1_texts/English/"
//                                                                                      + line.trim())));
//      TopicModelTool.class.getResourceAsStream("/mission2_texts/English/"
//                                               + line.trim())));
      TopicModelTool.class.getResourceAsStream("/mission3_texts/English/"
                                               + line.trim())));
      // .getResourceAsStream("/mission1_texts/Estonian/"
      // + line.trim())));
    }

    TopicModelTool topicModelTool = new TopicModelTool();
//    topicModelTool.createTopicModel(inputStreams.toArray(new Reader[0]), "en", "co2", 15);
//    topicModelTool.createTopicModel(inputStreams.toArray(new Reader[0]), "en", "eco", 15);
    topicModelTool.createTopicModel(inputStreams.toArray(new Reader[0]), "en", "pizza", 15);

    // topicModelTool.upload("scy.collide.info", 2525, topicModelTool
    // .getName(), topicModelTool.getTopicModel());
    File file = new File("src/test/resources/models", topicModelTool.getName());
    // System.out.println(file.getAbsolutePath());
    topicModelTool.getTopicModel().write(file);

//    BufferedWriter writer = new BufferedWriter(new FileWriter("topWords.en.co2.15.txt"));
//    BufferedWriter writer = new BufferedWriter(new FileWriter("topWords.en.eco.15.txt"));
    BufferedWriter writer = new BufferedWriter(new FileWriter("topWords.en.pizza.15.txt"));
    Object[][] topWords = topicModelTool.getTopicModel().getTopWords(1000);
    for (Object[] topWordsTopic : topWords) {
      for (Object topWord : topWordsTopic) {
        writer.write(topWord.toString());
        writer.write(" ");
      }
      writer.newLine();
    }
    writer.close();
  }
}
