package eu.scy.agents.api;

import roolo.elo.api.IMetadataKey;
import eu.scy.toolbrokerapi.ToolBrokerAPI;

public interface ICommunicationAgent<K extends IMetadataKey> extends IAgent, Runnable {
    
    public ToolBrokerAPI<K> getToolBrokerAPI();
    
    public void run();
    
}
