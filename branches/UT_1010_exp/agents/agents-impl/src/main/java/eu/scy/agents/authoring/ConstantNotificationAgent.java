package eu.scy.agents.authoring;

import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.Map;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;

public class ConstantNotificationAgent extends AbstractThreadedAgent {

	private static final String NAME = ConstantNotificationAgent.class
			.getName();

	protected ConstantNotificationAgent(Map<String, Object> params) {
		super(NAME, (String) params.get(AgentProtocol.PARAM_AGENT_ID));
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			port = (Integer) params.get(AgentProtocol.TS_PORT);
		}
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException,
			InterruptedException {
		while (status == Status.Running) {
			sendAliveUpdate();
			Thread.sleep(AgentProtocol.ALIVE_INTERVAL / 3);
			sendNotification();
		}
	}

	private void sendNotification() throws TupleSpaceException {
		Tuple notificationTuple = new Tuple();
		//TODO send a notification
		getCommandSpace().write(notificationTuple);
	}

	@Override
	protected void doStop() throws AgentLifecycleException {
		status = Status.Stopping;
	}

	@Override
	protected Tuple getIdentifyTuple(String queryId) {
		return null;
	}

	@Override
	protected Tuple getListParameterTuple(String queryId) {
		return super.getListParameterTuple(queryId);
	}

	@Override
	public boolean isStopped() {
		return status == Status.Stopping;
	}

}
