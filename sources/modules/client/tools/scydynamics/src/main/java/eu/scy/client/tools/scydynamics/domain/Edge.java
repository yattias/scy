package eu.scy.client.tools.scydynamics.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

import eu.scy.client.tools.scydynamics.model.QualitativeInfluenceType;

public class Edge {

	private String from;
	private String to;
	private List<Expression> expressions;

	public Edge() {}

	@XmlAttribute
	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	@XmlAttribute
	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}
	
	@XmlElement(name = "expression")
	public List<Expression> getExpressions() {
		return expressions;
	}

	public void setExpressions(List<Expression> expressions) {
		this.expressions = expressions;
	}

	public String getExpression(QualitativeInfluenceType qualitativeInfluenceType) {
		if (expressions != null) {
			for (Expression expression: expressions) {
				QualitativeInfluenceType type = expression.getType();
				if (type.equals(qualitativeInfluenceType)) {
					return expression.getFormula();
				}
			}
		}
		return null;
	}

}