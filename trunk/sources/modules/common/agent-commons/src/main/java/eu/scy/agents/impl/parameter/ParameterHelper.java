package eu.scy.agents.impl.parameter;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Callback;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import eu.scy.agents.impl.AgentProtocol;

public class ParameterHelper implements Callback {

	private TupleSpace tupleSpace;

	public ParameterHelper() {
		try {
			tupleSpace.eventRegister(Command.WRITE,
					AgentProtocol.PARAMETER_GET_QUERY, this, true);
		} catch (TupleSpaceException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void call(Command command, int seq, Tuple after, Tuple before) {
		String queryId = getFieldValue(after, 3);
		String parameterName = getFieldValue(after, 4);
		String missionURI = getFieldValue(after, 5);
		String las = getFieldValue(after, 6);
		String eloURI = getFieldValue(after, 7);
		String user = getFieldValue(after, 8);

		Object value = getParameterValue(parameterName, missionURI, las,
				eloURI, user);

		Tuple responseTuple = new Tuple(AgentProtocol.AGENT_PARAMETER_GET,
				AgentProtocol.RESPONSE, queryId, value);
		try {
			tupleSpace.write(responseTuple);
		} catch (TupleSpaceException e) {
			throw new RuntimeException(e);
		}

	}

	private Object getParameterValue(String parameterName, String missionURI,
			String las, String eloURI, String user) {
		return "TestValue";
	}

	private String getFieldValue(Tuple after, int index) {
		Field field = after.getField(index);
		if (field != null) {
			return (String) field.getValue();
		}
		return null;
	}

	public TupleSpace getTupleSpace() {
		return tupleSpace;
	}

	public void setTupleSpace(TupleSpace tupleSpace) {
		this.tupleSpace = tupleSpace;
	}
}
