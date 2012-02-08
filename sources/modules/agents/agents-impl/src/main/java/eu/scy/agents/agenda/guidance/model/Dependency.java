package eu.scy.agents.agenda.guidance.model;

public class Dependency {

	private final Activity dependent;
	private final Activity depending;
	
	
	public Dependency(Activity dependent, Activity depending) {
		this.dependent = dependent;
		this.depending = depending;
	}

	public Activity getDependent() {
		return this.dependent;
	}

	public Activity getDepending() {
		return this.depending;
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
		return (this.dependent.equals(thatDep.getDependent()) && this.depending.equals(thatDep.getDepending()));
	}
	
}
