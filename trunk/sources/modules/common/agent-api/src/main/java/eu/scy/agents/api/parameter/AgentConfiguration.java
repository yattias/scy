package eu.scy.agents.api.parameter;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Holds the parameter for an agent. These parameters can be default, missions-
 * and user specific. Each level overwrites the level above. So if a user
 * specific parameter is set this parameter only applies for this user. Every
 * other user gets the mission specific parameters.
 * <p/>
 * If a parameter isn't set and asked for the upper level parameter is returned.
 * E.g. a user specific parameter is asked for but none was set so the mission
 * specific parameter is returned and so on. So every agent should have default
 * parameters.
 *
 * @author Florian Schulz
 */
public class AgentConfiguration {

    private ConcurrentHashMap<String, Object> defaultParameter;
    private ConcurrentHashMap<String, Map<String, Object>> missionSpecificParameters;
    private ConcurrentHashMap<String, Map<String, Map<String, Object>>> userSpecificParameters;

    public AgentConfiguration() {
        defaultParameter = new ConcurrentHashMap<String, Object>();
        missionSpecificParameters = new ConcurrentHashMap<String, Map<String, Object>>();
        userSpecificParameters = new ConcurrentHashMap<String, Map<String, Map<String, Object>>>();
    }

    /**
     * Add all parameters in the <code>params</code> map to the default
     * parameter.
     *
     * @param params The parameters to add to the default parameters.
     */
    public void addAllParameter(Map<String, Object> params) {
        defaultParameter.putAll(params);
    }

    /**
     * Get the default parameter denoted by <code>parameterName</code>
     *
     * @param <T>           The type of the parameter.
     * @param parameterName The name of the parameter.
     * @return The value of the parameter or null if no parameter with this name
     *         was set.
     * @throws IllegalArgumentException if <code>parameterName</code> is null.
     */
    @SuppressWarnings("unchecked")
    public <T> T getParameter(String parameterName) {
        checkMethodParameterForNull(parameterName);

        return (T) defaultParameter.get(parameterName);
    }

    /**
     * Set the default parameter denoted by <code>parameterName</code>
     *
     * @param <T>            The type of the parameter.
     * @param parameterName  The name of the parameter.
     * @param parameterValue The value of the parameter (cannot be null).
     * @throws IllegalArgumentException if <code>parameterName</code> or <code>parameterValue</code>
     *                                  is null.
     */
    public <T> void setParameter(String parameterName, T parameterValue) {
        checkMethodParameterForNull(parameterValue, parameterName);

        defaultParameter.put(parameterName, parameterValue);
    }

    /**
     * Set a mission specific parameter. Default parameter with the same name
     * are overwritten for this mission.
     *
     * @param <T>            The type for this parameter.
     * @param mission        The mission this parameter should be valid for.
     * @param parameterName  The name of the parameter.
     * @param parameterValue The value of the parameter (cannot be null).
     * @throws IllegalArgumentException if <code>mission</code>, <code>parameterName</code> or
     *                                  <code>parameterValue</code> is null.
     */
    public <T> void setParameter(String mission, String parameterName,
                                 T parameterValue) {
        checkMethodParameterForNull(mission, parameterValue, parameterName);
        mission = mission.toLowerCase();

        Map<String, Object> missionParameterMap = missionSpecificParameters
                .get(mission);
        synchronized (missionSpecificParameters) {
            if (missionParameterMap == null) {
                missionParameterMap = new HashMap<String, Object>();
            }
            missionParameterMap.put(parameterName, parameterValue);
            missionSpecificParameters.put(mission, missionParameterMap);
        }
    }

    /**
     * Get the mission specific parameter with name <code>parameterName</code>
     * that is valid for <code>mission</code>.
     *
     * @param <T>           The type of the parameter.
     * @param mission       The mission this parameter is valid for.
     * @param parameterName The name of the parameter.
     * @return The value of the parameter or null if no parameter with this name
     *         was set for this mission.
     * @throws IllegalArgumentException if <code>parameterName</code> or <code>parameterValue</code>
     *                                  is null.
     */
    @SuppressWarnings("unchecked")
    public <T> T getParameter(String mission, String parameterName) {
        checkMethodParameterForNull(mission, parameterName);
        mission = mission.toLowerCase();

        Map<String, Object> missionParameterMap = missionSpecificParameters
                .get(mission);
        if (missionParameterMap == null) {
            return (T) getParameter(parameterName);
        }
        synchronized (missionSpecificParameters) {
            Object parameterValue = missionParameterMap.get(parameterName);
            if (parameterValue == null) {
                return (T) getParameter(parameterName);
            }
            return (T) parameterValue;
        }
    }

    /**
     * Set a user specific parameter. Mission specific parameter with the same
     * name are overwritten for this user.
     *
     * @param <T>            The type for this parameter.
     * @param mission        The mission this parameter should be valid for.
     * @param user           The user this parameter should be valid for.
     * @param parameterName  The name of the parameter.
     * @param parameterValue The value of the parameter (cannot be null).
     * @throws IllegalArgumentException if <code>mission</code>, <code>parameterName</code> or
     *                                  <code>parameterValue</code> is null.
     */
    public <T> void setParameter(String mission, String user,
                                 String parameterName, T parameterValue) {
        checkMethodParameterForNull(mission, user, parameterValue,
                parameterName);
        mission = mission.toLowerCase();

        Map<String, Map<String, Object>> missionSpecificParameters = userSpecificParameters
                .get(user);
        if (missionSpecificParameters == null) {
            synchronized (userSpecificParameters) {
                missionSpecificParameters = new HashMap<String, Map<String, Object>>();
                Map<String, Object> userMissionSpecificParameters = new HashMap<String, Object>();
                userMissionSpecificParameters
                        .put(parameterName, parameterValue);
                missionSpecificParameters.put(mission,
                        userMissionSpecificParameters);
                userSpecificParameters.put(user, missionSpecificParameters);
                return;
            }
        } else {
            synchronized (userSpecificParameters) {
                Map<String, Object> userMissionSpecificParameters = missionSpecificParameters
                        .get(mission);
                if (userMissionSpecificParameters == null) {
                    userMissionSpecificParameters = new HashMap<String, Object>();
                }
                userMissionSpecificParameters
                        .put(parameterName, parameterValue);
                missionSpecificParameters.put(mission,
                        userMissionSpecificParameters);
                userSpecificParameters.put(user, missionSpecificParameters);
            }
        }
    }

    /**
     * Get the mission and user specific parameter with name
     * <code>parameterName</code>.
     *
     * @param <T>           The type for this parameter.
     * @param mission       The mission this parameter is valid for.
     * @param user          The user this parameter is valid for.
     * @param parameterName The name of the parameter.
     * @return The value of the parameter or null if no parameter with this name
     *         was set for this mission.
     * @throws IllegalArgumentException if <code>mission</code>, <code>user</code>,
     *                                  <code>parameterName</code> or <code>parameterValue</code> is
     *                                  null.
     */
    @SuppressWarnings("unchecked")
    public <T> T getParameter(String mission, String user, String parameterName) {
        checkMethodParameterForNull(mission, user, parameterName);
        mission = mission.toLowerCase();

        Map<String, Map<String, Object>> missionSpecificParameters = userSpecificParameters
                .get(user);
        if (missionSpecificParameters == null) {
            return (T) getParameter(mission, parameterName);
        }
        synchronized (userSpecificParameters) {
            Map<String, Object> userMissionSpecificParameters = missionSpecificParameters
                    .get(mission);
            if (userMissionSpecificParameters == null) {
                return (T) getParameter(mission, parameterName);
            }

            Object parameterValue = userMissionSpecificParameters
                    .get(parameterName);
            if (parameterValue == null) {
                return (T) getParameter(mission, parameterName);
            }
            return (T) parameterValue;
        }
    }

    private void checkMethodParameterForNull(Object... args) {
        for (Object argument : args) {
            if (argument == null) {
                throw new IllegalArgumentException("Parameter cannot be null");
            }
        }
    }

    public boolean containsParameter(String parameterName) {
        return defaultParameter.containsKey(parameterName);
    }

    public void setParameter(AgentParameter parameter) {
        if (parameter.getMission() != null) {
            if (parameter.getUser() != null) {
                setParameter(parameter.getMission(), parameter.getUser(),
                        parameter.getParameterName(), parameter
                        .getParameterValue());
            } else {
                setParameter(parameter.getMission(), parameter
                        .getParameterName(), parameter.getParameterValue());
            }
        } else {
            setParameter(parameter.getParameterName(), parameter
                    .getParameterValue());
        }
    }

    @SuppressWarnings("unchecked")
    public <T> T getParameter(AgentParameter parameter) {
        if (parameter.getMission() != null) {
            if (parameter.getUser() != null) {
                return (T) getParameter(parameter.getMission(), parameter
                        .getUser(), parameter.getParameterName());
            } else {
                return (T) getParameter(parameter.getMission(), parameter
                        .getParameterName());
            }
        } else {
            return (T) getParameter(parameter.getParameterName());
        }
    }
}
