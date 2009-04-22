package eu.scy.agents.roolo.elo.drawing;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class NameCounters
{
	private Map<String,AtomicInteger> counters = new HashMap<String, AtomicInteger>();
	private Object addLock = new Object();
	
	public void increment(String name)
	{
		AtomicInteger count = counters.get(name);
		if (count==null)
		{
			synchronized(addLock)
			{
				count = counters.get(name);
				if (count==null)
				{
					count = new AtomicInteger();
					counters.put(name, count);
				}
			}
		}
		count.getAndIncrement();
	}
	
	public Map<String,AtomicInteger> getCounters()
	{
		return counters;
	}
}
