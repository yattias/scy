package eu.scy.client.tools.scydynamics.domain;

import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "simulation_settings")
public class SimulationSettings {

	private double startTime;
	private double stopTime;
	private double stepSize;
	private String calculationMethod;
	
	public double getStartTime() {
		return startTime;
	}
	public void setStartTime(double startTime) {
		this.startTime = startTime;
	}
	public double getStopTime() {
		return stopTime;
	}
	public void setStopTime(double stopTime) {
		this.stopTime = stopTime;
	}
	public double getStepSize() {
		return stepSize;
	}
	public void setStepSize(double stepSize) {
		this.stepSize = stepSize;
	}
	public String getCalculationMethod() {
		return calculationMethod;
	}
	public void setCalculationMethod(String calculationMethod) {
		this.calculationMethod = calculationMethod;
	}
	
}
