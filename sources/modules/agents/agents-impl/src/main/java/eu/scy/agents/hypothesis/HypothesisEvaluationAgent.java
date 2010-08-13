package eu.scy.agents.hypothesis;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.util.Map;

import org.apache.log4j.Logger;

import eu.scy.actionlogging.Action;
import eu.scy.actionlogging.ActionTupleTransformer;
import eu.scy.actionlogging.api.ContextConstants;
import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class HypothesisEvaluationAgent extends AbstractThreadedAgent implements Callback {

  private static final String TOOL_NAME = "richtext";

  private static final Logger logger = Logger.getLogger(HypothesisEvaluationAgent.class.getName());

  private boolean isStopped;

  public HypothesisEvaluationAgent(Map<String, Object> map) {
    super(HypothesisEvaluationAgent.class.getName(),
          (String) map.get(AgentProtocol.PARAM_AGENT_ID), (String) map.get(AgentProtocol.TS_HOST),
          (Integer) map.get(AgentProtocol.TS_PORT));
    try {
      // commandSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false,
      // AgentProtocol.COMMAND_SPACE_NAME);
      // actionSpace = new TupleSpace(new User(getSimpleName()), host, port, false, false,
      // AgentProtocol.ACTION_SPACE_NAME);
      Callback hypothesisCallback = new HypothesisCallback();
      // TODO tool_started correct
      // TODO add Tool
      getActionSpace().eventRegister(
                                     Command.WRITE,
                                     new Tuple(AgentProtocol.ACTION_SPACE_NAME, String.class,
                                               Long.class, AgentProtocol.ACTION_TOOL_STARTED,
                                               Field.createWildCardField(), TOOL_NAME,
                                               Field.createWildCardField()), hypothesisCallback,
                                     true);
    } catch (TupleSpaceException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected void doRun() throws TupleSpaceException, AgentLifecycleException, InterruptedException {
    while (status == Status.Running) {
      sendAliveUpdate();
      Thread.sleep(5000);
    }
  }

  @Override
  protected void doStop() {
    try {
      getActionSpace().disconnect();
      getCommandSpace().disconnect();
    } catch (TupleSpaceException e) {
      e.printStackTrace();
    }
  }

  @Override
  protected Tuple getIdentifyTuple(String queryId) {
    // TODO
    return null;
  }

  @Override
  public boolean isStopped() {
    return isStopped;
  }

  class HypothesisCallback implements Callback {

    @Override
    public void call(Command cmd, int seqnum, Tuple afterTuple, Tuple beforeTuple) {
      Action a = (Action) ActionTupleTransformer.getActionFromTuple(afterTuple);
      String mission = a.getContext(ContextConstants.mission);
      String session = a.getContext(ContextConstants.session);
      String elouri = a.getContext(ContextConstants.eloURI);
      // TODO Here the logic must be inserted ;-)
      // Wait till saved -> then its easy but maybe we want to evaluate intermediate
      // results....
    }
  }

}
