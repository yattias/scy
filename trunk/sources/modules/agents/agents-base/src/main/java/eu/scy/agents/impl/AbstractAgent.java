package eu.scy.agents.impl;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.TupleSpaceException;
import eu.scy.agents.api.IAgent;

/**
 * Implementation of the IAgent interface.
 * 
 * @author fschulz
 */
public class AbstractAgent implements IAgent {

	private TupleSpace tupleSpace;

	protected String name;

	public AbstractAgent(String agentName) {
		name = agentName;
	}

	@Override
	public TupleSpace getTupleSpace() {
		if (tupleSpace == null) {
			try {
				tupleSpace = new TupleSpace();
			} catch (TupleSpaceException e) {
				e.printStackTrace();
			}
		}
		return tupleSpace;
	}

	@Override
	public String getName() {
		return name;
	}

}
