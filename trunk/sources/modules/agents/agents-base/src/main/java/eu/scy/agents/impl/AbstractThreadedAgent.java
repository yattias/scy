package eu.scy.agents.impl;

import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleID;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.TupleSpaceException.TupleSpaceError;

import java.rmi.dgc.VMID;
import java.util.concurrent.locks.ReentrantLock;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IThreadedAgent;

/**
 * A basic implementation of a threaded agent. It provides basic starting and
 * stopping functionality.
 * 
 * @author Florian Schulz, Jan Engler, Stefan Weinbrenner
 * 
 */
public abstract class AbstractThreadedAgent extends AbstractAgent implements
		IThreadedAgent, Callback {

	/**
	 * Statuses of a threaded agent.
	 * 
	 */
	public static enum Status {
		/**
		 * Agent is currently initializing.
		 */
		Initializing,
		/**
		 * Agent is currently running.
		 */
		Running,
		/**
		 * Agent is shutting down.
		 */
		Stopping;

	}

	private TupleID aliveTupleID = null;

	private int commandId;

	private int identifyId;

	protected ReentrantLock lock = new ReentrantLock();

	private Thread myThread;

	/**
	 * The current status of the agent.
	 * 
	 * @see Status
	 */
	protected Status status;

	private boolean killed = false;

	/**
	 * Create a new {@link AbstractThreadedAgent}. Only allowed to call by
	 * subclasses.
	 * 
	 * @param name
	 *            The name of the agent.
	 * @param id
	 *            The id of the agent.
	 */
	protected AbstractThreadedAgent(String name, String id) {
		super(name, id);
		status = Status.Initializing;
	}

	@Override
	public void call(Command command, int seq, Tuple afterTuple,
			Tuple beforeTuple) {
		if (!Command.WRITE.equals(command)) {
			return;
		}
		if (AgentProtocol.COMMAND_LINE.equals(afterTuple.getField(0).getValue()
				.toString())) {
			if (afterTuple.getField(2).getValue().equals(getId().trim())) {
				String message = (String) afterTuple.getField(4).getValue();
				// if (AgentProtocol.MESSAGE_START.equals(message)) {
				// this.start();
				// }
				if (AgentProtocol.MESSAGE_STOP.equals(message)
						&& afterTuple.getField(2).getValue().equals(
								getId().trim())) {
					try {
						// if the message was to stop....
						this.stop();
					} catch (AgentLifecycleException e) {
						e.printStackTrace();
					}
				}
			}

		} else if (AgentProtocol.QUERY.equals(afterTuple.getField(0).getValue()
				.toString())) {
			// Field 1 is (due to conventions) queryId
			final String queryId = afterTuple.getField(1).getValue().toString();
			// Reply on ident-query
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						getTupleSpace().write(getIdentifyTuple(queryId));
					} catch (TupleSpaceException e) {
						e.printStackTrace();
					}
				}
			}).start();
		}
	}

	/**
	 * Really runs the agent. This method is present so programmers can
	 * implement their own behavior of agents whenever they should run. It is
	 * necessary to provide means that the agent is triggered in doRun.
	 * 
	 * @throws TupleSpaceException
	 *             TupleSpace couldn't be accessed.
	 * @throws AgentLifecycleException
	 *             Something went wrong with the life-cycle of the agent.
	 */
	protected abstract void doRun() throws TupleSpaceException,
			AgentLifecycleException;

	/**
	 * Clean up the agent.
	 */
	protected abstract void doStop();

	/**
	 * This method returns a Tuple which identifies the agent with TupleDoc
	 * Entries.<br />
	 * This Tuple has the form:<br /> {@code AgentProtocol.RESPONSE, QueryID : String,
	 * agentID :String, agentName :String, AgentProtocol.MESSAGE_IDENTIFY,
	 * TupleDocEntries : String ...}
	 * 
	 * @param queryId
	 *            The queryId which is inside the Response. Usually taken from
	 *            the identify query.
	 * @return A {@link Tuple} which contains the answer to the identify-query.
	 */
	protected abstract Tuple getIdentifyTuple(String queryId);

	@Override
	public boolean isRunning() {
		return status == Status.Running;
	}

	/**
	 * Is the agent really stopped.
	 * 
	 * @return true if the agent is really stopped.
	 */
	@Override
	public abstract boolean isStopped();

	@SuppressWarnings("deprecation")
	@Override
	public final void kill() throws AgentLifecycleException {
		if (myThread != null) {
			status = Status.Stopping;
			killed = true;
			// we use the dangerous method Thread.stop() intentionally. Inside
			// the ResponseThread the ThreadDeath-Error is handled.
			myThread.stop();
		} else {
			throw new AgentLifecycleException(name
					+ " cannot be killed, is null");
		}
	}

	/**
	 * This method is called when the implemented {@link ThreadedAgent} is
	 * started.
	 */
	public final void run() {
		try {
			// need a lock to run
			lock.lock();
			doRun();
			// free the lock
			lock.unlock();
		} catch (Exception e) {
			if (e instanceof TupleSpaceException
					&& ((TupleSpaceException) e).getError().equals(
							TupleSpaceError.THREAD_KILLED) && this.killed) {
				// Everything OK
				// TODO some kind of output?
			} else {
				e.printStackTrace();
			}
			try {

				if (lock.isHeldByCurrentThread()) {
					lock.unlock();
				}
				if (status != Status.Stopping) {
					stop();

				}

			} catch (AgentLifecycleException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * This method writes an alive {@link Tuple} in the {@link TupleSpace}. If
	 * such a {@link Tuple} has already been written, it is updated,
	 * 
	 * @throws TupleSpaceException
	 *             If something went wrong with the {@link TupleSpace}.
	 */
	protected void sendAliveUpdate() throws TupleSpaceException {
		if (aliveTupleID == null) {
			// write new alive tuple
			aliveTupleID = getTupleSpace()
					.write(
							AgentProtocol.getAliveTuple(getId(), getName(),
									new VMID()));
		} else {
			// update already present alive tuple
			getTupleSpace()
					.update(
							aliveTupleID,
							AgentProtocol.getAliveTuple(getId(), getName(),
									new VMID()));
		}
	}

	@Override
	public final void start() throws AgentLifecycleException {
		// If the agent is in a stopping-phase. It can't be startet then.
		if (Status.Stopping == status) {
			throw new AgentLifecycleException(name
					+ " is in Status.Stopping. Can't be startet here..");
		}
		// if the agent is already started
		if (Status.Running == status) {
			throw new AgentLifecycleException(name + " already running");
		}
		try {
			// register commandEvents & identifyEvents
			// TODO Reverse-structured names
			commandId = getTupleSpace().eventRegister(Command.WRITE,
					AgentProtocol.getCommandTuple(this.id, this.name), this,
					true);
			identifyId = getTupleSpace().eventRegister(Command.WRITE,
					AgentProtocol.getIdentifyTuple(this.id, this.name), this,
					true);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
		status = Status.Running;
		// create & start a new Thread with the new agent as payload
		myThread = new Thread(this, name);
		myThread.start();
	}

	/**
	 * This methods stops the agents, deregisters all callbacks and disconnects
	 * from the {@link TupleSpace}.
	 * 
	 * @throws AgentLifecycleException
	 *             Is thrown if something went wrong during the stopping of the
	 *             agent.
	 */
	public final void stop() throws AgentLifecycleException {
		if (Status.Stopping != status) {
			status = Status.Stopping;
			lock.lock();
			doStop();
			lock.unlock();
			new Thread(new Runnable() {

				@Override
				public void run() {
					tidy();
				}
			}).start();
		} else {
			throw new AgentLifecycleException(name + " already dead");
		}
	}

	/**
	 * Tidy up the agent when it is stopped.
	 */
	public final void tidy() {
		try {
			// command & identify Callbacks are deregistered
			getTupleSpace().eventDeRegister(commandId);
			getTupleSpace().eventDeRegister(identifyId);
			// disconnect from tuplespace
			getTupleSpace().disconnect();
		} catch (TupleSpaceException e) {
			// we do not care about exceptions here
			e.printStackTrace();
		}
	}
}
