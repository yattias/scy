package eu.scy.agents.impl;

import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleID;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.util.Timer;
import java.util.TimerTask;

import eu.scy.agents.api.IThreadedAgent;

public abstract class AbstractThreadedAgent extends AbstractAgent implements
		IThreadedAgent, Callback {

	public enum Status {
		Suspended, Running, Stopped;
	}

	private Status status = Status.Stopped;

	protected AbstractThreadedAgent(String name) {
		super(name);
		try {
			getTupleSpace().eventRegister(Command.WRITE,
					AgentProtocol.COMMAND_TEMPLATE, this, true);
			getTupleSpace().eventRegister(Command.WRITE, getTemplateTuple(),
					this, true);
		} catch (TupleSpaceException e) {
			throw new RuntimeException(e);
		}
		new Timer().schedule(new TimerTask() {
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
		}, AgentProtocol.ALIVE_INTERVAL);
	}

	@Override
	public void resume() {
		status = Status.Running;
	}

	@Override
	public void start() {
		status = Status.Running;
		Thread t = new Thread(this, name);
		t.start();
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
			doRun();
		}
	}

	protected abstract void doRun();

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
			run();
		}
	}
}
