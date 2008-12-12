package eu.scy.agents.impl;

import eu.scy.agents.api.IProcessingAgent;

public abstract class AbstractProcessingAgent extends AbstractAgent implements IProcessingAgent {
    
    protected boolean done;
    
    @Override
    public void run() {
        while (!done) {
            doRun();
        }
    }
    
    public AbstractProcessingAgent() {
        done = false;
        Thread t = new Thread(this);
        t.start();
    }
    
    protected abstract void doRun();
    
}
