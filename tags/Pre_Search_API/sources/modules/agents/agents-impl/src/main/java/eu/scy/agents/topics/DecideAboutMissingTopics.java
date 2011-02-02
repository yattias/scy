package eu.scy.agents.topics;

import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractDecisionAgent;
import eu.scy.agents.impl.AgentProtocol;

/**
 * Activated whenever topics for an ELO have to be determined, i.e. by a tuple like
 * ("topicDetector":String, <ELOUri>:String)
 * 
 * @author Florian Schulz
 */
public class DecideAboutMissingTopics extends AbstractDecisionAgent {

  private static final double MINIMUM_TOPIC_PROBABILITY = 0.5;

  protected DecideAboutMissingTopics(Map<String, Object> params) {
    super(DecideAboutMissingTopics.class.getName(),
          (String) params.get(AgentProtocol.PARAM_AGENT_ID));
  }

  @Override
  protected void doRun() throws TupleSpaceException, AgentLifecycleException {
    while (status == Status.Running) {
      Tuple tuple = getCommandSpace().waitToRead(getTemplateTuple(),
                                                 AgentProtocol.COMMAND_EXPIRATION);
      if (tuple != null) {
        Tuple topicTuple = getCommandSpace().waitToRead(new Tuple(TopicAgents.TOPIC_DETECTOR,
                                                                  String.class,
                                                                  Field.createWildCardField()),
                                                        10 * 1000);
        if (topicTuple != null) {
          try {
            HashMap<Integer, Double> topicScoresMap = getTopicScores(topicTuple);

            Set<Integer> importantTopics = getImportantTopics();
            Set<Integer> missingTopics = determineMissingTopics(importantTopics, topicScoresMap);

            // TODO get information about level of scaffold
            if (!missingTopics.isEmpty()) {
              sendMissingTopicsMessage(missingTopics);
            }
          } catch (IOException e) {
            e.printStackTrace();
          } catch (ClassNotFoundException e) {
            e.printStackTrace();
          }
        }
      }
      sendAliveUpdate();
    }
  }

  @SuppressWarnings("unchecked")
  private HashMap<Integer, Double> getTopicScores(Tuple topicTuple) throws IOException,
      ClassNotFoundException {
    ObjectInputStream bytesIn = new ObjectInputStream(
                                                      new ByteArrayInputStream(
                                                                               (byte[]) topicTuple.getField(2).getValue()));
    HashMap<Integer, Double> topicScoresMap = (HashMap<Integer, Double>) bytesIn.readObject();
    return topicScoresMap;
  }

  private void sendMissingTopicsMessage(Set<Integer> missingTopics) {
    Tuple response = new Tuple(TopicAgents.TOPIC_DETECTOR, "missing_topics");
    for (int missingTopic : missingTopics) {
      response.add(missingTopic);
    }
    try {
      getCommandSpace().write(response);
    } catch (TupleSpaceException e) {
      e.printStackTrace();
    }
  }

  private Set<Integer> determineMissingTopics(Set<Integer> importantTopics,
                                              HashMap<Integer, Double> topicScoresMap) {
    HashSet<Integer> missingTopics = new HashSet<Integer>();
    for (int topicId : importantTopics) {
      double topicProbability = topicScoresMap.get(topicId);
      if (topicProbability < MINIMUM_TOPIC_PROBABILITY) {
        missingTopics.add(topicId);
      }
    }
    return missingTopics;
  }

  private Set<Integer> getImportantTopics() {
    return Collections.emptySet();
  }

  private Tuple getTemplateTuple() {
    return new Tuple(TopicAgents.TOPIC_DETECTOR, String.class);
  }

  @Override
  protected void doStop() {
    status = Status.Stopping;
  }

  @Override
  protected Tuple getIdentifyTuple(String queryId) {
    return null;
  }

  @Override
  public boolean isStopped() {
    return status == Status.Stopping;
  }

}
