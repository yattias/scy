package eu.scy.agents.authoring.workflow;

import java.io.Serializable;
import java.net.URI;
import java.util.List;

public class WorkflowItem implements Serializable {

	public enum Type {
		LAS, ACTIVITY, ELO, TOOL
	}

	private static final long serialVersionUID = 8787628351964270418L;

	private String id;

	private Type type;

	private List<WorkflowItem> outgoing;

	private List<WorkflowItem> ingoing;

	private long expectedTimeinMinutes;

	private List<URI> anchorElos;

	private int expectedVisits;

	public List<URI> getAnchorElos() {
		return anchorElos;
	}

	public void addAnchorElos(URI anchorElo) {
		this.anchorElos.add(anchorElo);
	}

	public int getExpectedVisits() {
		return expectedVisits;
	}

	public void setExpectedVisits(int expectedVisits) {
		this.expectedVisits = expectedVisits;
	}

	public long getExpectedTimeinMinutes() {
		return expectedTimeinMinutes;
	}

	public void setExpectedTimeinMinutes(long expectedTimeinMinutes) {
		this.expectedTimeinMinutes = expectedTimeinMinutes;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}
}
