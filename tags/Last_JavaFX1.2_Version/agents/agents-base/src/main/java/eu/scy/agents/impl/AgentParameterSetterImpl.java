package eu.scy.agents.impl;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;
import eu.scy.agents.api.parameter.AgentParameter;
import eu.scy.agents.api.parameter.AgentParameterSetter;

public class AgentParameterSetterImpl implements AgentParameterSetter {

	private static final String TS_USER = "AgentParameterSetterImpl";

	private TupleSpace tupleSpace;

	public AgentParameterSetterImpl(String tsHost, int tsPort) {
		try {
			tupleSpace = new TupleSpace(new User(TS_USER), tsHost, tsPort,
					true, false, AgentProtocol.COMMAND_SPACE_NAME);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
	}

	// @SuppressWarnings("unchecked")
	// @Override
	// public <T> T getParameter(String agentName, String mission, String user,
	// String parameterName) {
	// return (T) getParameterViaTuple(agentName, mission, user, parameterName);
	// }
	//
	// @SuppressWarnings("unchecked")
	// @Override
	// public <T> T getParameter(String agentName, String mission,
	// String parameterName) {
	// return (T) getParameterViaTuple(agentName, mission, null, parameterName);
	// }
	//
	// @SuppressWarnings("unchecked")
	// @Override
	// public <T> T getParameter(String agentName, String parameterName) {
	// return (T) getParameterViaTuple(agentName, null, null, parameterName);
	// }
	//
	// @Override
	// public <T> void setParameter(String agentName, String mission, String
	// user,
	// String parameterName, T value) {
	// try {
	// tupleSpace.write(createSetParameterTupleTemplate(agentName,
	// mission, user, parameterName, value));
	// } catch (TupleSpaceException e) {
	// e.printStackTrace();
	// throw new RuntimeException("Couldn't set parameter" + parameterName
	// + " for " + agentName, e);
	// }
	// }
	//
	// @Override
	// public <T> void setParameter(String agentName, String mission,
	// String parameterName, T value) {
	// setParameter(agentName, mission, null, parameterName, value);
	// }
	//
	// @Override
	// public <T> void setParameter(String agentName, String parameterName, T
	// value) {
	// setParameter(agentName, null, null, parameterName, value);
	// }

	private <T> Tuple createSetParameterTupleTemplate(String agentName,
			String mission, String user, String parameterName, T value) {
		Tuple setParameterTuple = AgentProtocol
				.getParameterSetTupleTemplate(agentName);
		setParameterTuple.getField(2).setValue(mission);
		setParameterTuple.getField(3).setValue(user);
		setParameterTuple.getField(4).setValue(parameterName);
		setParameterTuple.getField(5).setValue(value);

		return setParameterTuple;
	}

	private <T> Tuple createGetParameterTupleTemplate(String agentName,
			String mission, String user, String parameterName) {
		Tuple getParameterTuple = AgentProtocol
				.getParameterGetQueryTupleTemplate(agentName);
		getParameterTuple.getField(3).setValue(mission);
		getParameterTuple.getField(4).setValue(user);
		getParameterTuple.getField(5).setValue(parameterName);

		return getParameterTuple;
	}

	@SuppressWarnings("unchecked")
	private <T> T getParameterViaTuple(String agentName, String mission,
			String user, String parameterName) {
		Tuple getParameterTuple = createGetParameterTupleTemplate(agentName,
				mission, user, parameterName);
		try {
			tupleSpace.write(getParameterTuple);
			Tuple getParameterResponse = tupleSpace.waitToRead(AgentProtocol
					.getParameterGetResponseTupleTemplate(agentName));
			if (getParameterResponse == null) {
				return null;
			}
			return (T) getParameterResponse.getField(6).getValue();
		} catch (TupleSpaceException e) {
			e.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> T getParameter(String agentName, AgentParameter parameter) {
		return (T) getParameterViaTuple(agentName, parameter.getMission(),
				parameter.getUser(), parameter.getParameterName());
	}

	@Override
	public <T> void setParameter(String agentName, AgentParameter parameter) {
		try {
			Tuple setParameterTuple = createSetParameterTupleTemplate(
					agentName, parameter.getMission(), parameter.getUser(),
					parameter.getParameterName(), parameter.getParameterValue());
			tupleSpace.write(setParameterTuple);
		} catch (TupleSpaceException e) {
			e.printStackTrace();
			throw new RuntimeException("Couldn't set parameter"
					+ parameter.getParameterName() + " for " + agentName, e);
		}
	}
}
