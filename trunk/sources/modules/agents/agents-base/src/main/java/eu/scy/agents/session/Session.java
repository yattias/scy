package eu.scy.agents.session;

import eu.scy.actionlogging.api.IAction;
import eu.scy.agents.Mission;
import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;
import org.apache.log4j.Logger;

public class Session {

    private final static Logger LOGGER = Logger.getLogger(Session.class);

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
                    Field.createSemiformalField(user+"*"), String.class));
            if (missionTuple != null) {
                String language = (String) missionTuple.getField(2).getValue();
                return language;
            }
        } catch (TupleSpaceException e) {
            LOGGER.warn(e.getMessage());
        }
        return "en";
    }

    public Mission getMission(String user) {
        try {
            Tuple missionTuple = sessionSpace.read(new Tuple(Session.MISSION,
                    Field.createSemiformalField(user+"*"), String.class, String.class, String.class));
            if (missionTuple != null) {
                String missionString = (String) missionTuple.getField(3)
                        .getValue();
                return Mission.getForName(missionString);
            }
        } catch (TupleSpaceException e) {
            LOGGER.warn(e.getMessage());
        }
        return Mission.UNKNOWN_MISSION;
    }

    public String getMissionURI(String user) {
        try {
            Tuple missionTuple = sessionSpace.read(new Tuple(Session.MISSION,
                    Field.createSemiformalField(user+"*"), String.class, String.class, String.class));
            if (missionTuple != null) {
                String missionString = (String) missionTuple.getField(2)
                        .getValue();
                return missionString;
            }
        } catch (TupleSpaceException e) {
            LOGGER.warn(e.getMessage());
        }
        return IAction.NOT_AVAILABLE;
    }

    public String getMissionName(String user) {
        try {
            Tuple missionTuple = sessionSpace.read(new Tuple(Session.MISSION,
                    Field.createSemiformalField(user+"*"), String.class, String.class, String.class));
            if (missionTuple != null) {
                String missionString = (String) missionTuple.getField(3)
                        .getValue();
                return missionString;
            }
        } catch (TupleSpaceException e) {
            LOGGER.warn(e.getMessage());
        }
        return IAction.NOT_AVAILABLE;
    }
}
