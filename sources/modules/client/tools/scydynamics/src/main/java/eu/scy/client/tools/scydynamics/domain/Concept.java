package eu.scy.client.tools.scydynamics.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlEnum;

public class Concept {
	
	@XmlEnum(String.class)
	public enum ConceptType {
		STOCK,
		CONSTANT,
		AUXILIARY,
		DOMAIN_RELATED,
		DOMAIN_UNRELATED
	}
	
	private String name;
	private ConceptType type;
	private List<Term> terms;
	
	public Concept() {}
	
	public String toString() {
		return name;
	}
	
	@XmlAttribute
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlAttribute
	public ConceptType getType() {
		return type;
	}
	
	public void setType(ConceptType type) {
		this.type = type;
	}
	
	@XmlElement(name = "term")
	public List<Term> getTerms() {
		return terms;
	}
	
	public void setTerms(List<Term> terms) {
		this.terms = terms;
	}
	
	
}
