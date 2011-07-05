package eu.scy.client.tools.scydynamics.domain;

import javax.xml.bind.annotation.XmlAttribute;

public class Edge {

	private String from;
	private String to;
	private String qualitative;

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

	@XmlAttribute
	public String getQualitative() {
		return qualitative;
	}

	public void setQualitative(String qualitative) {
		this.qualitative = qualitative;
	}
}