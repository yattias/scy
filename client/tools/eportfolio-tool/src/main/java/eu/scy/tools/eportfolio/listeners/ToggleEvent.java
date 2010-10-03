package eu.scy.tools.eportfolio.listeners;

import java.util.EventObject;

public class ToggleEvent extends EventObject {
	
	private static final long serialVersionUID = -5359174209719862613L;
	private int target;

	public ToggleEvent(Object source, int i) {
		super(source);
		this.target = i;
	}

	public void setOrigin(int target) {
		this.target = target;
	}

	public int getTarget() {
		return target;
	}


}
