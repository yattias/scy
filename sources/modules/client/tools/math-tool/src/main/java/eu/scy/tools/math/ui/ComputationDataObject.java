package eu.scy.tools.math.ui;

public class ComputationDataObject {

	private String name;
	private Float value;
	public void setValue(Float value) {
		this.value = value;
	}
	public Float getValue() {
		return value;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	@Override
	public String toString() {
		return "I am data " + name + " " + value;
	}
	
}
