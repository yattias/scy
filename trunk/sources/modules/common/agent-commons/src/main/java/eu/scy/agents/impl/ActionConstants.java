package eu.scy.agents.impl;

public final class ActionConstants {

    public static final String ACTION = "action";
    public static final String ACTION_ELO_SAVED = "elo_saved";
    public static final String ACTION_ELO_LOADED = "elo_loaded";
    public static final String ACTION_ELO_UPDATED = "elo_updated";
    public static final String ACTIONLOG_ELO_TYPE = "elo_type";
    public static final String ACTIONLOG_ELO_URI = "elo_uri";
    public static final String ACTIONLOG_OLD_URI = "old_uri";
    public static final String ACTION_TOOL_STARTED = "tool_started";
    public static final String ACTION_TOOL_OPENED = "tool_opened";
    public static final String ACTION_TOOL_CLOSED = "tool_closed";
    public static final String ACTION_NODE_ADDED = "node_added";
    public static final String ACTION_NODE_REMOVED = "node_removed";
    public static final String ACTION_LAS_CHANGED = "las_changed";
    public static final String ACTION_LOG_IN = "logged_in";
    public static final Object ACTION_LOG_OUT = "logged_out";
    public static final String LAS = "newLasId";
    public static final String OLD_LAS = "oldLasId";
    public static final String NOTIFICATION_REJECTED = "notification_rejected";
    public static final String NOTIFICATION_ACCEPTED = "notification_accepted";
    public static final String OLD_ELO_URI = "old_uri";
    public static final String ELO_URI = "elo_uri";
    public static final String ACTION_TOOL_MINIMIZED = "tool_minimized";

    public static final String MISSION_NAME = "missionName";
    public static final String MISSION_ID = "missionId";
    public static final String MISSION_SPECIFICATION = "missionSpecification";
    public static final String ELO_FINISHED = "elo_finished";

    private ActionConstants() {
        // construction not allowed
    }

}
