package eu.scy.agents.impl;

import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleID;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import eu.scy.agents.api.IThreadedAgent;

public abstract class AbstractThreadedAgent extends AbstractAgent implements
		IThreadedAgent, Callback {

	private final class SendAliveUpdates extends TimerTask {
		TupleID tupleId = null;

		@Override
		public void run() {
			try {
				if (tupleId == null) {
					tupleId = getTupleSpace().write(
							AgentProtocol.getAliveTuple(getName(), status));
				} else {
					getTupleSpace().update(tupleId,
							AgentProtocol.getAliveTuple(getName(), status));
				}
			} catch (TupleSpaceException e) {
				// print stacktrace and wait for manager to restart the
				// agent.
				e.printStackTrace();
			}
		}
	}

	public enum Status {
		Suspended, Running, Stopped, Killed;
	}

	private Status status = Status.Stopped;
	private Timer aliveTimer;
	private Tuple triggerParameter;

	protected AbstractThreadedAgent(String name) {
		super(name);
		try {
			getTupleSpace().eventRegister(Command.WRITE,
					AgentProtocol.COMMAND_COMMAND_TEMPLATE, this, true);
			getTupleSpace().eventRegister(Command.WRITE, getTemplateTuple(),
					this, true);
		} catch (TupleSpaceException e) {
			throw new RuntimeException(e);
		}
		aliveTimer = new Timer(getName() + "_AliveTimer");
		aliveTimer.scheduleAtFixedRate(new SendAliveUpdates(), new Date(System
				.currentTimeMillis()), AgentProtocol.ALIVE_INTERVAL);
	}

	// @Override
	public void kill() {
		if (Status.Killed != status) {
			aliveTimer.cancel();
			status = Status.Killed;
			try {
				getTupleSpace().disconnect();
			} catch (TupleSpaceException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void resume() {
		status = Status.Running;
	}

	@Override
	public void start() {
		if (status == Status.Killed) {
			// TODO throw exception that agent was killed?
			return;
		}
		// Thread t = new Thread(this, getName());
		// t.start();
		status = Status.Running;
	}

	@Override
	public boolean isRunning() {
		return status == Status.Running;
	}

	@Override
	public boolean isSuspended() {
		return status == Status.Suspended;
	}

	@Override
	public void stop() {
		status = Status.Stopped;
	}

	@Override
	public void suspend() {
		status = Status.Suspended;
	}

	@Override
	public void run() {
		if (status == Status.Running) {
			doRun(getTriggerParameter());
		}
	}

	private Tuple getTriggerParameter() {
		return triggerParameter;
	}

	protected abstract void doRun(Tuple trigger);

	protected abstract Tuple getTemplateTuple();

	@Override
	public void call(Command command, int seq, Tuple afterTuple,
			Tuple beforeTuple) {
		if (!Command.WRITE.equals(command)) {
			return;
		}
		if (AgentProtocol.COMMAND_LINE
				.equals(afterTuple.getField(0).getValue())) {
			if (afterTuple.getField(1).getValue().equals(getName().trim())) {
				String message = (String) afterTuple.getField(2).getValue();
				if (AgentProtocol.MESSAGE_START.equals(message)) {
					this.start();
				}
				if (AgentProtocol.MESSAGE_STOP.equals(message)) {
					this.stop();
				}
				if (AgentProtocol.MESSAGE_SUSPEND.equals(message)) {
					this.suspend();
				}
				if (AgentProtocol.MESSAGE_RESUME.equals(message)) {
					this.resume();
				}
			}
		}
		if (afterTuple.matches(getTemplateTuple())) {
			setTriggerParameter(afterTuple);
			run();
		}
	}

	private void setTriggerParameter(Tuple afterTuple) {
		triggerParameter = afterTuple;
	}

	@Override
	public boolean isKilled() {
		return status == Status.Killed;
	}
}
