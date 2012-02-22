package eu.scy.agents.agenda.guidance.event;

import java.util.EventObject;

public class DialogNotificationEvent extends EventObject {

	private static final long serialVersionUID = 1L;

	private final String text;
	
	private final String session;
	
	public DialogNotificationEvent(Object source, String text, String session) {
		super(source);
		this.text = text;
		this.session = session;
	}

	public String getText() {
		return this.text;
	}

	public String getSession() {
		return this.session;
	}

}
