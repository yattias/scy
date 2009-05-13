package eu.scy.ws.example.mock.api;

import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created: 05.feb.2009 10:56:36
 *
 * @author Bjørge Næss
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "elo", namespace = "http://scy.eu")
@XmlRootElement(name = "elo")
public class ELO {
	private String title;

    @XmlElement(namespace = "http://scy.eu")
	private Object content;

    private int id;

    @XmlTransient
	private List<ELO> children = new ArrayList<ELO>();

	public ELO() {}
	public ELO(int id, String title, Object content) {
		this.id = id;
		this.title = title;
		this.content = content;
	}

	public ELO(int id, String title) {
		this.id = id;
		this.title = title;
	}

    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setId(int eloid) {
		this.id = eloid;
	}

	public int getId() {
		return id;
	}

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public List<ELO> getChildren() {
		return children;
	}
	public void addChildELO(ELO elo) {
		this.children.add((ELO)elo);
	}
	public void setChildren(List<ELO> children) {
		//noinspection unchecked
		this.children = (List<ELO>)children;
	}
	public String toString() {
		return "I am an ELO. My ID is "+id+", my title is "+ title+" and my content is: "+content;
	}
}
