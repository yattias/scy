package eu.scy.agents.authoring;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.impl.AbstractRequestAgent;
import eu.scy.agents.impl.AgentProtocol;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.rmi.dgc.VMID;
import java.util.Map;

public class NotifyUserAgent extends AbstractRequestAgent implements Callback {

	public static final String SEND_NOTIFICATION = "send_notification";
	public static final String NAME = NotifyUserAgent.class.getName();

	private int listenerId;

	public NotifyUserAgent(Map<String, Object> params) {
		super(NAME, (String) params.get(AgentProtocol.PARAM_AGENT_ID));
		if (params.containsKey(AgentProtocol.TS_HOST)) {
			host = (String) params.get(AgentProtocol.TS_HOST);
		}
		if (params.containsKey(AgentProtocol.TS_PORT)) {
			port = (Integer) params.get(AgentProtocol.TS_PORT);
		}

		try {
			listenerId = getCommandSpace().eventRegister(Command.WRITE,
					getTemplateTuple(), this, true);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	/*
	 * ("send_notification":String, <From>:String, <To>:String, <Mission>:String
	 * <Message>:String)
	 */
	private Tuple getTemplateTuple() {
		return new Tuple(SEND_NOTIFICATION, String.class, String.class,
				String.class, String.class);
	}

	@Override
	protected void doRun() throws TupleSpaceException, AgentLifecycleException,
			InterruptedException {
		while (status == Status.Running) {
			sendAliveUpdate();
			Thread.sleep(AgentProtocol.ALIVE_INTERVAL / 3);
		}
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

	@Override
	public void call(Command command, int seq, Tuple afterTuple,
			Tuple beforeTuple) {
		if (this.listenerId != seq) {
			logger.debug("Callback passed to Superclass.");
			super.call(command, seq, afterTuple, beforeTuple);
			return;
		} else {
			String from = (String) afterTuple.getField(1).getValue();
			String to = (String) afterTuple.getField(2).getValue();
			String mission = (String) afterTuple.getField(3).getValue();
			String message = (String) afterTuple.getField(4).getValue();

			sendNotification(from, to, mission, message);
		}
	}

	private void sendNotification(String from, String to, String mission,
			String message) {
        Tuple notificationTuple = new Tuple();
		notificationTuple.add(AgentProtocol.NOTIFICATION);
		notificationTuple.add(new VMID().toString());
		notificationTuple.add(to);
		notificationTuple.add("no specific elo");
		notificationTuple.add(NAME);
		notificationTuple.add(mission);
		notificationTuple.add("n/a");
		notificationTuple.add("text=" + message.toString());
		notificationTuple.add("title=Generic Message");
		notificationTuple.add("type=message_dialog_show");
		notificationTuple.add("modal=false");
		notificationTuple.add("dialogType=OK_DIALOG");

		try {
			if (this.getCommandSpace().isConnected()) {
				this.getCommandSpace().write(notificationTuple);
			}
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}
}
