package eu.scy.agents.impl;

import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleID;
import info.collide.sqlspaces.commons.TupleSpaceException;

import java.rmi.dgc.VMID;

import eu.scy.agents.api.AgentLifecycleException;
import eu.scy.agents.api.IThreadedAgent;

public abstract class AbstractThreadedAgent extends AbstractAgent implements IThreadedAgent, Callback {

    public static enum Status {
        Initializing,
        Running,
        Stopping;
    }

    protected Status status;

    private int commandId;

    public TupleID aliveTupleID = null;

    protected AbstractThreadedAgent(String name, String id) {
        super(name, id);
        status = Status.Initializing;
    }

    @Override
    public void stop() throws AgentLifecycleException {
        if (Status.Stopping != status) {
            status = Status.Stopping;
            doStop();
            try {
                getTupleSpace().eventDeRegister(commandId);
                getTupleSpace().disconnect();
            } catch (TupleSpaceException e) {
                // we do not care about exceptions here
                e.printStackTrace();
            }
        } else {
            throw new AgentLifecycleException(name + " already dead");
        }
    }

    @Override
    public final void start() throws AgentLifecycleException, AgentLifecycleException {
        if (Status.Stopping == status) {
            throw new AgentLifecycleException(name + " already dead");
        }
        if (Status.Running == status) {
            throw new AgentLifecycleException(name + " already running");
        }
        try {
            commandId = getTupleSpace().eventRegister(Command.WRITE, AgentProtocol.COMMAND_COMMAND_TEMPLATE, this, true);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        status = Status.Running;
        new Thread(this, name).start();
    }

    public final void run() {
        try {
            doRun();
        } catch (Exception e) {
            try {
                stop();
            } catch (AgentLifecycleException e1) {
                e1.printStackTrace();
            }
        }
    }

    protected abstract void doRun() throws TupleSpaceException, AgentLifecycleException;

    /**
     * Clean up the agent.
     */
    protected abstract void doStop();

    /**
     * Is the agent really stopped.
     */
    public abstract boolean isStopped();

    @Override
    public boolean isRunning() {
        return status == Status.Running;
    }

    @Override
    public void call(Command command, int seq, Tuple afterTuple, Tuple beforeTuple) {
        if (!Command.WRITE.equals(command)) {
            return;
        }
        if (AgentProtocol.COMMAND_LINE.equals(afterTuple.getField(0).getValue())) {
            if (afterTuple.getField(3).getValue().equals(getName().trim())) {
                String message = (String) afterTuple.getField(4).getValue();
                // if (AgentProtocol.MESSAGE_START.equals(message)) {
                // this.start();
                // }
                if (AgentProtocol.MESSAGE_STOP.equals(message)) {
                    try {
                        this.stop();
                    } catch (AgentLifecycleException e) {
                        e.printStackTrace();
                    }
                } else if (AgentProtocol.MESSAGE_IDENTIFY.equals(message)) {
                    try {
                        getTupleSpace().write(getIdentifyTuple());
                    } catch (TupleSpaceException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    protected void sendAliveUpdate() throws TupleSpaceException {
        if (aliveTupleID == null) {
            aliveTupleID = getTupleSpace().write(AgentProtocol.getAliveTuple(getName(), getId(), new VMID()));
        } else {
            getTupleSpace().update(aliveTupleID, AgentProtocol.getAliveTuple(getName(), getId(), new VMID()));
        }
    }

    protected abstract Tuple getIdentifyTuple();

}
