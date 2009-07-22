package eu.scy.agents.supervisor;

import info.collide.sqlspaces.client.TupleSpace;

import java.rmi.dgc.VMID;

import eu.scy.agents.impl.AgentProtocol;


public class SupervisingAgentTest {

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        TupleSpace ts = new TupleSpace(AgentProtocol.COMMAND_SPACE_NAME);
        ts.write(AgentProtocol.getAliveTuple("MyTestAgent", "666", new VMID()));
    }

}
