package eu.scy.client.tools.scydynamics.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Concept {
	
	private String name;
	private String type;
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
	public String getType() {
		return type;
	}
	
	public void setType(String type) {
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
