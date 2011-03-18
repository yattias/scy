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

	public void setExpectedTimeInMinutes(long expectedTimeinMinutes) {
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
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
		WorkflowItem other = (WorkflowItem) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

}
