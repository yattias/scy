package eu.scy.agents.processguidanceservice;

public class UserAction {
	
	public static final String ACTION_TOOL_STARTED = "tool_started";
	public static final String ACTION_TOOL_OPENED = "tool_opened";
	public static final String ACTION_TOOL_GOT_FOCUS = "tool_got_focus";// not defined in ActionConstants
	public static final String ACTION_TOOL_LOST_FOCUS = "tool_lost_focus";// not defined in ActionConstants
	public static final String ACTION_ELO_SAVED = "elo_saved";
	public static final String ACTION_TOOL_CLOSED = "tool_closed";
	public static final String ACTION_TOOL_COMPLETE = "tool_complete";
	public static final String ACTION_TOOL_QUIT = "tool_quit";// not defined in ActionConstants
	public static final String ACTION_TEXT_INSERTED = "text_inserted";// not defined in ActionConstants
	public static final String ACTION_TEXT_DELETED = "text_deleted";// not defined in ActionConstants
	public static final String ACTION_NODE_ADDED = "node_added";
	public static final String ACTION_NODE_REMOVED = "node_removed";
	public static final String ACTION_LINK_ADDED = "link_added";// not defined in ActionConstants
	public static final String ACTION_LINK_REMOVED = "link_removed";// not defined in ActionConstants
	//public static final String ACTION_LAS_CHANGED = "las_changed";

	private String action; 
	private ELORun eloRun;
	private long timeInMillis;
	//private String value;
	
	public UserAction(String anAction, ELORun aELORun, long time){
		action = anAction;
		eloRun = aELORun;
		timeInMillis = time;
		//value = aValue;
	}
	
	public String getAction() {
		return action;
	}
	
	public ELORun getELORun() {
		return eloRun;
	}
	
	public long getTimeInMillis() {
		return timeInMillis;
	}

	/*
	public String getValue() {
		return value;
	}
	*/
}
