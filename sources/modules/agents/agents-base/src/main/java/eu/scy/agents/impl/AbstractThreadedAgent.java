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

    private int identifyId;
    
    public TupleID aliveTupleID = null;

    private Thread myThread;

    protected AbstractThreadedAgent(String name, String id) {
        super(name, id);
        status = Status.Initializing;
    }

    
    public void stop() throws AgentLifecycleException {
        if (Status.Stopping != status) {
            status = Status.Stopping;
            doStop();
//            try {
//              //  getTupleSpace().eventDeRegister(commandId);
//              //  getTupleSpace().eventDeRegister(identifyId);
//                //getTupleSpace().disconnect();
//            } catch (TupleSpaceException e) {
//                // we do not care about exceptions here
//                e.printStackTrace();
//            }
        } else {
            throw new AgentLifecycleException(name + " already dead");
        }
    }
    @Override
    public final void kill() throws AgentLifecycleException {
        if (myThread!=null){
            status=Status.Stopping;
            myThread.stop();
        }else{
            throw new AgentLifecycleException(name +" cannot be killed, is null");
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
            identifyId = getTupleSpace().eventRegister(Command.WRITE, AgentProtocol.IDENTIFY_TEMPLATE, this, true);
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        status = Status.Running;
        myThread = new Thread(this, name);
        myThread.start();
    }

    public final void run() {
        try {
            doRun();
        } catch (Exception e) {
            e.printStackTrace();
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
        if (AgentProtocol.COMMAND_LINE.equals(afterTuple.getField(0).getValue().toString())) {
            if (afterTuple.getField(3).getValue().equals(getName().trim())) {
                String message = (String) afterTuple.getField(4).getValue();
                // if (AgentProtocol.MESSAGE_START.equals(message)) {
                // this.start();
                // }
                if (AgentProtocol.MESSAGE_STOP.equals(message) && afterTuple.getField(2).getValue().equals(getId().trim())) {
                    try {
                        this.stop();
                    } catch (AgentLifecycleException e) {
                        e.printStackTrace();
                    }
                } 
            }
            
        }else if (AgentProtocol.QUERY.equals(afterTuple.getField(0).getValue().toString())) {
            try {
                String queryId = afterTuple.getField(1).getValue().toString();
                getTupleSpace().write(getIdentifyTuple(queryId));
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
        }
    }

    protected void sendAliveUpdate() throws TupleSpaceException {
        if (aliveTupleID == null) {
            aliveTupleID = getTupleSpace().write(AgentProtocol.getAliveTuple(getId(), getName(), new VMID()));
        } else {
            getTupleSpace().update(aliveTupleID, AgentProtocol.getAliveTuple(getId(), getName(), new VMID()));
        }
    }

    protected abstract Tuple getIdentifyTuple(String queryId);


}

