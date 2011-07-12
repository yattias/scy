package eu.scy.client.tools.scydynamics.domain;

import javax.xml.bind.annotation.XmlAttribute;

public class Node {

	private String id;
	private String concept;
	private String type;
	private String formula;
	private String highNegative;
	private String lowNegative;
	private String lowPositive;
	private String highPositive;
	private String zero;

	public Node() {
	}

	@XmlAttribute
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	@XmlAttribute
	public String getConcept() {
		return concept;
	}

	public void setConcept(String concept) {
		this.concept = concept;
	}

	@XmlAttribute
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	@XmlAttribute
	public String getFormula() {
		return formula;
	}

	public void setFormula(String formula) {
		this.formula = formula;
	}
	
	@XmlAttribute(name = "high_negative")
	public String getHighNegative() {
		return highNegative;
	}

	public void setHighNegative(String highNegative) {
		this.highNegative = highNegative;
	}

	@XmlAttribute(name = "low_negative")
	public String getLowNegative() {
		return lowNegative;
	}

	public void setLowNegative(String lowNegative) {
		this.lowNegative = lowNegative;
	}

	@XmlAttribute(name = "low_positive")
	public String getLowPositive() {
		return lowPositive;
	}

	public void setLowPositive(String lowPositive) {
		this.lowPositive = lowPositive;
	}

	@XmlAttribute(name = "high_positive")
	public String getHighPositive() {
		return highPositive;
	}

	public void setHighPositive(String highPositive) {
		this.highPositive = highPositive;
	}
	
	@XmlAttribute(name = "zero")
	public String getZero() {
		return highPositive;
	}

	public void setZero(String zero) {
		this.zero = zero;
	}

}
