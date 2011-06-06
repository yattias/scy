package eu.scy.agents.util.time;

/**
 * Implementation that calls System.currentTimeMillis.
 * 
 * @author fschulz
 * 
 */
public class DefaultTimer implements Timer {

	@Override
	public long currentTimeMillis() {
		return System.currentTimeMillis();
	}

}