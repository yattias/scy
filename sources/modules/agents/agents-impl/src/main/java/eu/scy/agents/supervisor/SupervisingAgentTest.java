package eu.scy.agents.supervisor;

import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.impl.AbstractThreadedAgent.Status;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;


public class SupervisingAgentTest {

    /**
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        TupleSpace ts = new TupleSpace(AgentProtocol.COMMAND_SPACE_NAME);
        ts.write(AgentProtocol.getAliveTuple("MyTestAgent", "666", Status.Running));
    }

}
