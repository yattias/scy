package eu.scy.agents.util.time;

/**
 * Represents a duration in milliseconds. This class is immutable; every method
 * that alters the duration returns a new instance.
 * 
 * @author Florian Schulz
 * 
 */
public class Duration {

	private long duration;

	public Duration() {
		this(0);
	}

	public Duration(long duration) {
		this.duration = duration;
	}

	public Duration(long start, long end) {
		duration = end - start;
	}

	public Duration add(long start, long end) {
		return new Duration(duration + (end - start));
	}

	public Duration add(Duration other) {
		return new Duration(duration + other.duration);
	}

	/**
	 * Returns the duration as milliseconds.
	 * 
	 * @return
	 */
	public long toMilliseconds() {
		return duration;
	}

	/**
	 * Returns the duration as seconds. Will be not as accurate as the time in
	 * milliseconds.
	 * 
	 * @return
	 */
	public long toSeconds() {
		return duration / 1000;
	}

	/**
	 * Returns the duration as minutes. Will be not as accurate as the time in
	 * seconds or milliseconds.
	 * 
	 * @return
	 */
	public long toMinutes() {
		return duration / 60000;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (duration ^ (duration >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Duration other = (Duration) obj;
		if (duration != other.duration)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "" + duration;
	}

	public boolean greater(Duration other) {
		return duration > other.duration;
	}

	public Duration difference(Duration other) {
		return new Duration(Math.abs(duration - other.duration));
	}

}
