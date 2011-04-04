package eu.scy.agents.session;

import org.apache.log4j.Logger;

import eu.scy.agents.Mission;

import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;

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
					user, String.class));
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
					user, String.class, String.class));
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

}
