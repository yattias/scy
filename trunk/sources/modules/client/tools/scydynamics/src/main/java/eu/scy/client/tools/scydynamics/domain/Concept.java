package eu.scy.client.tools.scydynamics.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;

public class Concept {
	
	private String name;
	private List<Term> terms;
	
	public Concept() {}
	
	@XmlAttribute
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlElement(name = "term")
	public List<Term> getTerms() {
		return terms;
	}
	
	public void setTerms(List<Term> terms) {
		this.terms = terms;
	}
	
	
}
