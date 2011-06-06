package eu.scy.server.controllers;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 10.feb.2011
 * Time: 10:56:53
 * To change this template use File | Settings | File Templates.
 */
public class AgentConfigurationWrapper {

    private String name;
    private String status;

    private List agentParameters = new LinkedList();


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List getAgentParameters() {
        return agentParameters;
    }

    public void setAgentParameters(List agentParameters) {
        this.agentParameters = agentParameters;
    }

    public void addAgentParameterConfiguration(AgentParameterConfigurationWrapper agentParameterConfigurationWrapper) {
        agentParameters.add(agentParameterConfigurationWrapper);
    }
}
