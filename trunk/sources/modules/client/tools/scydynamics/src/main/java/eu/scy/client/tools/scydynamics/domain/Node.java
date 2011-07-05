package eu.scy.client.tools.scydynamics.domain;

import javax.xml.bind.annotation.XmlAttribute;

public class Node {

	private String id;
	private String concept;
	private String type;
	private String formula;

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

}
