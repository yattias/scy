package eu.scy.agents.authoring.workflow.paths;

import eu.scy.agents.util.time.Timer;

public class MockTimer implements Timer {

	private long time;

	@Override
	public long currentTimeMillis() {
		return time;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

}
