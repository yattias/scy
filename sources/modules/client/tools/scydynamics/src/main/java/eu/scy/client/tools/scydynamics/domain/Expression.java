package eu.scy.client.tools.scydynamics.domain;

import javax.xml.bind.annotation.XmlAttribute;

import eu.scy.client.tools.scydynamics.model.QualitativeInfluenceType;

public class Expression {

	private QualitativeInfluenceType type;
	private String formula;
	
	public Expression() {}
	
	public Expression(QualitativeInfluenceType type, String formula) {
		this.type = type;
		this.formula = formula;
	}
	
	@XmlAttribute
	public QualitativeInfluenceType getType() {
		return type;
	}
	
	public void setType(QualitativeInfluenceType type) {
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
