package eu.scy.agents.agenda.guidance.model;

public class Dependency {

	private Activity from;
	
	private Activity to;
	
	
	public Dependency(Activity from, Activity to) {
		this.from = from;
		this.to = to;
	}

	public Activity getFrom() {
		return this.from;
	}

	public Activity getTo() {
		return this.to;
	}
	
	@Override
	public boolean equals(Object that) {
		if(that == null || !(that instanceof Dependency)) {
			return false;
		}
		if(this == that) {
			return true;
		}
		Dependency thatDep = (Dependency)that;
		return (this.from.equals(thatDep.getFrom()) && this.to.equals(thatDep.getTo()));
	}
	
}
