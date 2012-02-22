package eu.scy.agents.agenda.guidance.event;

import info.collide.sqlspaces.commons.TupleID;

import java.util.EventObject;

import eu.scy.agents.agenda.guidance.model.Activity;

public class SaveActivityEvent extends EventObject {

	private static final long serialVersionUID = 1L;
	
	private final TupleID tupleId;
	
	private final Activity activity;
	
	public SaveActivityEvent(Object source, Activity activity) {
		this(source, activity, null);
	}
	
	public SaveActivityEvent(Object source, Activity activity, TupleID tupleId) {
		super(source);
		this.activity = activity;
		this.tupleId = tupleId;
	}

	public Activity getActivity() {
		return this.activity;
	}

	public TupleID getTupleID() {
		return this.tupleId;
	}
	
}
