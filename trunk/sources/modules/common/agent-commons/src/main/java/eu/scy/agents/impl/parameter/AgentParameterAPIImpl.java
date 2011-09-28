package eu.scy.agents.impl.parameter;

import eu.scy.agents.api.parameter.AgentParameter;
import eu.scy.agents.api.parameter.AgentParameterAPI;
import eu.scy.agents.impl.AgentProtocol;
import eu.scy.common.configuration.Configuration;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import info.collide.sqlspaces.commons.User;

import java.rmi.dgc.VMID;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AgentParameterAPIImpl implements AgentParameterAPI {

    public static final String GLOBAL = "global";
    private static final String NA = "n/a";

    private static final String TS_USER = "AgentParameterSetterImpl";
    private TupleSpace tupleSpace;

    public AgentParameterAPIImpl() throws TupleSpaceException {
        this(Configuration.getInstance().getSQLSpacesServerHost(),
                Configuration.getInstance().getSQLSpacesServerPort());
    }

    public AgentParameterAPIImpl(String tsHost, int tsPort) throws TupleSpaceException {
        this(new TupleSpace(new User(TS_USER), tsHost, tsPort,
                true, false, AgentProtocol.COMMAND_SPACE_NAME));
    }

    public AgentParameterAPIImpl(TupleSpace tupleSpace) {
        this.tupleSpace = tupleSpace;
    }

    private <T> Tuple createSetParameterTupleTemplate(String agentName,
                                                      String mission, String user, String parameterName, T value) {
        Tuple setParameterTuple = AgentProtocol
                .getParameterSetTupleTemplate();
        setParameterTuple.getField(1).setValue(agentName);
        if (mission == null) {
            setParameterTuple.getField(2).setValue(NA);
        } else {

            setParameterTuple.getField(2).setValue(mission);
        }
        if (user == null) {
            setParameterTuple.getField(3).setValue(NA);
        } else {
            setParameterTuple.getField(3).setValue(user);
        }
        setParameterTuple.getField(4).setValue(parameterName);
        setParameterTuple.getField(5).setValue(value);

        return setParameterTuple;
    }

    private <T> Tuple createGetParameterTupleTemplate(String agentName,
                                                      String mission, String user, String parameterName) {
        Tuple getParameterTuple = AgentProtocol
                .getParameterGetQueryTupleTemplate();
        getParameterTuple.getField(2).setValue(agentName);
        if (mission == null) {
            getParameterTuple.getField(3).setValue(NA);
        } else {
            getParameterTuple.getField(3).setValue(mission);
        }
        if (user == null) {
            getParameterTuple.getField(4).setValue(NA);
        } else {
            getParameterTuple.getField(4).setValue(user);
        }
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
                    .getParameterGetResponseTupleTemplate(agentName),
                    AgentProtocol.MINUTE);
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

    @Override
    public List<String> listAgentParameter(String agentName) {
        VMID queryId = new VMID();
        Tuple listParameterTuple = AgentProtocol.getListParametersTupleQuery(
                agentName, queryId.toString());
        try {
            tupleSpace.write(listParameterTuple);
            Tuple responseTupleFormat = AgentProtocol.LIST_PARAMETER_RESPONSE;
            responseTupleFormat.getField(2).setValue(queryId.toString());
            responseTupleFormat.getField(3).setValue(agentName);
            Tuple responseTuple = tupleSpace.waitToRead(responseTupleFormat,
                    2 * AgentProtocol.MINUTE);
            List<String> parameterNames = new ArrayList<String>();
            for (int i = 4; i < responseTuple.getNumberOfFields(); i++) {
                parameterNames.add((String) responseTuple.getField(i)
                        .getValue());
            }
            return parameterNames;
        } catch (TupleSpaceException e) {
            e.printStackTrace();
        }
        return Collections.emptyList();
    }
}
