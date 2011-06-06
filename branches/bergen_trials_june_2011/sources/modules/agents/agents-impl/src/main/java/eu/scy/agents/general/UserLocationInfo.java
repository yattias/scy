package eu.scy.agents.general;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Holds information about a user e.g. which LAS the user is in currently in and
 * which tools and ELOs are opened by the user. <br/>
 * This class is thread safe as changes to the data are atomic operations.
 * 
 */
public class UserLocationInfo {

	private CopyOnWriteArraySet<String> toolList = new CopyOnWriteArraySet<String>();
	private CopyOnWriteArraySet<String> eloList = new CopyOnWriteArraySet<String>();
	private String las;

	private Lock lasLock;

	public UserLocationInfo() {
		lasLock = new ReentrantLock();
	}

	public void addTool(String tool) {
		toolList.add(tool);
	}

	public void removeTool(String tool) {
		toolList.remove(tool);
	}

	public void addELO(String eloUri) {
		eloList.add(eloUri);
	}

	public void removeELO(String eloUri) {
		eloList.remove(eloUri);
	}

	public void setLas(String las) {
		lasLock.lock();
		try {
			this.las = las;
		} finally {
			lasLock.unlock();
		}
	}

	public String getLas() {
		lasLock.lock();
		try {
			return las;
		} finally {
			lasLock.unlock();
		}
	}

	public Set<String> getOpenTools() {
		return copySet(toolList);
	}

	public Set<String> getOpenElos() {
		return copySet(eloList);
	}

	private Set<String> copySet(Set<String> set) {
		Set<String> copy = new HashSet<String>();
		for (String element : set) {
			copy.add(element);
		}
		return copy;
	}

	@Override
	public String toString() {
		return las + " | tools:" + toolList + " elos:" + eloList;
	}

}
