package eu.scy.client.tools.scydynamics.logging.parser.actiongraph;

import java.text.DecimalFormat;
import java.util.UUID;

public class ActionEdge {
	
	private double weight;
	private UUID id;
	private double sum;
	private static DecimalFormat oneDigit = new DecimalFormat("#,##0.0");
	
	public ActionEdge() {
		this(0.0, 1.0);
	}
	
	public ActionEdge(double weight, double sum) {
		this.weight = weight;
		this.sum = sum;
		this.id = UUID.randomUUID();
	}
	
	public double getWeight() {
		return this.weight;
	}
	
	public double getThickness() {
		return this.weight/this.sum*100;
	}
	
	public String getId() {
		return id.toString();
	}
	
	public String toString() {
		return Math.round(weight)+" ("+(oneDigit.format(weight/sum*100))+"%)";
	}
}