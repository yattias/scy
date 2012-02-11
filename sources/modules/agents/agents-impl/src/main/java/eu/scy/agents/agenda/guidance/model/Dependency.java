package eu.scy.agents.agenda.guidance.model;

public class Dependency {

	private final Activity dependent;
	private final Activity dependsOn;
	
	
	public Dependency(Activity dependent, Activity dependsOn) {
		if(dependent == null) {
			throw new IllegalArgumentException("dependent was null");
		}
		if(dependsOn == null) {
			throw new IllegalArgumentException("dependsOn was null");
		}
		this.dependent = dependent;
		this.dependsOn = dependsOn;
	}

	public Activity getDependent() {
		return this.dependent;
	}

	public Activity getDependsOn() {
		return this.dependsOn;
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
		return (this.dependent.equals(thatDep.getDependent()) && this.dependsOn.equals(thatDep.getDependsOn()));
	}
	
}
