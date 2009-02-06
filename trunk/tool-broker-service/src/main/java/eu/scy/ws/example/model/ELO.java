package eu.scy.ws.example.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;

/**
 * Created: 05.feb.2009 10:56:36
 *
 * @author Bjørge Næss
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "elo")
public class ELO {
	private String title;
	private String content;
	private String id;
	private ArrayList<ELO> childELOs = new ArrayList<ELO>();

	public ELO() {}
	public ELO(String id, String title, String content) {
		this.id = id;
		this.title = title;
		this.content = content;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setId(String eloid) {
		this.id = eloid;
	}

	public String getId() {
		return id;
	}


	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public ArrayList<ELO> getChildELOs() {
		return childELOs;
	}
	public void addChildELO(ELO elo) {
		this.childELOs.add(elo);
	}
	public void setChildELOs(ArrayList<ELO> childELOs) {
		this.childELOs = childELOs;
	}
	public String toString() {
		return "I am an ELO. My ID is "+id+", my title is"+ title+" and my content is: "+content;
	}
}
