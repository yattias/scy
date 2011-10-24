package eu.scy.agents;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import eu.scy.agents.impl.AbstractThreadedAgent;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.agents.session.Session;

public abstract class SCYAbstractThreadedAgent extends AbstractThreadedAgent {

    private TupleSpace sessionSpace;

    protected SCYAbstractThreadedAgent(String name, String id) {
        super(name, id);
    }

    public SCYAbstractThreadedAgent(String name, String id, String tsHost, int tsPort) {
        super(name, id, tsHost, tsPort);
    }

    public TupleSpace getSessionSpace() {
        if (sessionSpace == null) {
            try {
                sessionSpace = new TupleSpace(new User(getSimpleName()), host, port, isRunningAutonomous(), false, AgentProtocol.SESSION_SPACE_NAME);
            } catch (TupleSpaceException e) {
                e.printStackTrace();
            }
        }
        return sessionSpace;
    }

    public Session getSession() {
        return new Session(getSessionSpace());
    }

}
