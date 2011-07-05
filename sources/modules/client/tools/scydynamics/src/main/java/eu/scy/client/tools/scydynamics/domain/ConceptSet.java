package eu.scy.client.tools.scydynamics.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "concept_set") 
public class ConceptSet {

	private String name;
	private String wordOrder;
	private List<Concept> concepts;
	
	public ConceptSet() {}
	
	@XmlAttribute
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	@XmlAttribute(name = "word_order")
	public String getWordOrder() {
		return wordOrder;
	}

	public void setWordOrder(String wordOrder) {
		this.wordOrder = wordOrder;
	}

	@XmlElement(name = "concept")
	public List<Concept> getConcepts() {
		return concepts;
	}
	public void setConcepts(List<Concept> concepts) {
		this.concepts = concepts;
	}
	
	public Concept getConcept(String name) {
		for (Concept c: concepts) {
			if (c.getName().equals(name)) {
				return c;
			}
		}
		// concept with given name not found, returning null
		return null;
	}

	
	
}
