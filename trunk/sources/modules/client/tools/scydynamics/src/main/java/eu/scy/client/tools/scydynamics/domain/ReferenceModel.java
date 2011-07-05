package eu.scy.client.tools.scydynamics.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "reference_model") 
public class ReferenceModel {

	private String name;
	private String notation;
	private String conceptSet;
	private List<Node> nodes;
	private List<Edge> relations;
	
	public ReferenceModel() {	
	}
	
	@XmlAttribute
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@XmlAttribute
	public String getNotation() {
		return notation;
	}

	public void setNotation(String notation) {
		this.notation = notation;
	}
	
	@XmlAttribute(name = "concept_set")
	public String getConceptSet() {
		return conceptSet;
	}
	
	public void setConceptSet(String conceptSet) {
		this.conceptSet = conceptSet;
	}

	@XmlElement(name = "node")
	public List<Node> getNodes() {
		return nodes;
	}

	public void setNodes(List<Node> nodes) {
		this.nodes = nodes;
	}

	@XmlElement(name = "edge")
	public List<Edge> getEdges() {
		return relations;
	}

	public void setEdges(List<Edge> relations) {
		this.relations = relations;
	}

}
