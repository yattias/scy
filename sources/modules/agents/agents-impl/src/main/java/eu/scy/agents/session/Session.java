package eu.scy.agents.session;

import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.Mission;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.apache.log4j.Logger;

import java.util.HashSet;
import java.util.Set;

public class Session {

    private final static Logger LOGGER = Logger.getLogger(Session.class);
    public static final int USER_FIELD = 1;
    public static final int MISSION_SPECIFICATION_FIELD = 2;
    public static final int LANGUAGE_FIELD = 2;
    public static final int MISSION_NAME_FIELD = 3;
    public static final int MISSION_RUNTIME_FIELD = 4;
    public static final int MISSION_ID_FIELD = 5;


    private TupleSpace sessionSpace;

    public static final String TOOL = "tool";
    public static final String LAS = "las";
    public static final String MISSION = "mission";
    public static final String LANGUAGE = "language";

    public Session(TupleSpace sessionSpace) {
        this.sessionSpace = sessionSpace;
    }

    public String getLanguage(String user) {
        try {
            Tuple missionTuple = sessionSpace.read(new Tuple(Session.LANGUAGE,
                    Field.createSemiformalField(user + "*"), String.class));
            if ( missionTuple != null ) {
                String language = (String) missionTuple.getField(LANGUAGE_FIELD).getValue();
                return language;
            }
        } catch ( TupleSpaceException e ) {
            LOGGER.warn(e.getMessage());
        }
        return "en";
    }

    public Mission getMission(String user) {
        try {
            Tuple missionTuple = sessionSpace.read(new Tuple(Session.MISSION,
                    Field.createSemiformalField(user + "*"), String.class, String.class, String.class, String.class));
            if ( missionTuple != null ) {
                String missionString = (String) missionTuple.getField(MISSION_NAME_FIELD)
                        .getValue();
                return Mission.getForName(missionString);
            }
        } catch ( TupleSpaceException e ) {
            LOGGER.warn(e.getMessage());
        }
        return Mission.UNKNOWN_MISSION;
    }

    public String getMissionRuntimeURI(String user) {
        try {
            Tuple missionTuple = sessionSpace.read(new Tuple(Session.MISSION,
                    Field.createSemiformalField(user + "*"), String.class, String.class, String.class, String.class));
            if ( missionTuple != null ) {
                String missionRT = (String) missionTuple.getField(MISSION_RUNTIME_FIELD).getValue();
                return missionRT;
            }
        } catch ( TupleSpaceException e ) {
            LOGGER.warn(e.getMessage());
        }
        return IAction.NOT_AVAILABLE;
    }

    public Set<String> getUsersInMissionFromRuntime(String missionRuntime) {
        Set<String> availableUsers = new HashSet<String>();
        try {
            String missionSpecification = getMissionSpecification(missionRuntime);
            if ( !missionSpecification.equals(IAction.NOT_AVAILABLE) ) {
                collectAvailableUsers(availableUsers, missionSpecification);
            } else {
                LOGGER.warn("no mission found for runtime uri " + missionRuntime);
            }
        } catch ( TupleSpaceException e ) {
            e.printStackTrace();
        }
        return availableUsers;
    }

    public Set<String> getUsersInMissionFromName(String missionName) {
        Set<String> availableUsers = new HashSet<String>();
        try {
            Tuple missionNameTuple = sessionSpace.read(new Tuple(Session.MISSION, String.class, String.class, missionName, String.class,
                    String.class));
            if ( missionNameTuple != null ) {
                String missionSpec = (String) missionNameTuple.getField(MISSION_SPECIFICATION_FIELD).getValue();
                collectAvailableUsers(availableUsers, missionSpec);
            } else {
                LOGGER.warn("no mission found for mission with name " + missionName);
            }

        } catch ( TupleSpaceException e ) {
            e.printStackTrace();
        }
        return availableUsers;
    }

    private void collectAvailableUsers(Set<String> availableUsers, String missionSpec) throws TupleSpaceException {
        Tuple[] allUsersInLas = sessionSpace.readAll(new Tuple(Session.MISSION, String.class, missionSpec, String.class,
                String.class, String.class));
        for ( Tuple t : allUsersInLas ) {
            availableUsers.add((String) t.getField(USER_FIELD).getValue());
        }
    }

    public String getMissionSpecification(String missionRT) {
        try {
            Tuple missionSpecTuple = sessionSpace.read(new Tuple(Session.MISSION, String.class, String.class, String.class, missionRT,
                    String.class));
            if ( missionSpecTuple != null ) {
                return (String) missionSpecTuple.getField(MISSION_SPECIFICATION_FIELD).getValue();
            }
        } catch ( TupleSpaceException e ) {
            e.printStackTrace();
        }
        return IAction.NOT_AVAILABLE;
    }

    public String getMissionId(String missionRT) {
        try {
            Tuple missionSpecTuple = sessionSpace.read(new Tuple(Session.MISSION, String.class, String.class, String.class, missionRT,
                    String.class));
            if ( missionSpecTuple != null ) {
                return (String) missionSpecTuple.getField(MISSION_ID_FIELD).getValue();
            }
        } catch ( TupleSpaceException e ) {
            e.printStackTrace();
        }
        return IAction.NOT_AVAILABLE;
    }

    public Set<String> getUsersInLas(String missionName, String las) {
        Set<String> users = new HashSet<String>();
        try {
            Tuple missionNameTuple = sessionSpace.read(new Tuple(Session.MISSION, String.class, String.class, missionName, String.class,
                    String.class));
            if ( missionNameTuple != null ) {
                String missionSpec = (String) missionNameTuple.getField(MISSION_SPECIFICATION_FIELD).getValue();
                Tuple[] lasTuples = sessionSpace.readAll(new Tuple(Session.LAS, String.class, missionSpec, las));
                for ( Tuple lasTuple : lasTuples ) {
                    String user = (String) lasTuple.getField(USER_FIELD).getValue();
                    users.add(user);
                }
            } else {
                LOGGER.warn("No tuple found for ");
            }
        } catch ( TupleSpaceException e ) {
            e.printStackTrace();
        }
        return users;
    }
}
