package eu.scy.ws.example.mock.api;

import eu.scy.ws.example.api.ELO;

import javax.ws.rs.Produces;
import javax.xml.bind.annotation.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created: 05.feb.2009 10:56:36
 *
 * @author Bjørge Næss
 */
@Produces("application/json")
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "elo")
public class MockELO implements ELO {
	private String title;
	private String content;
	private int id;

    @XmlElementWrapper
	@XmlAnyElement(lax=false)
	private List<ELO> children = new ArrayList<ELO>();

	public MockELO() {}
	public MockELO(int id, String title, String content) {
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

	public void setId(Integer eloid) {
		this.id = eloid;
	}

	public int getId() {
		return id;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public List<ELO> getChildren() {
		return children;
	}
	public void addChildELO(ELO elo) {
		this.children.add(elo);
	}
	public void setChildren(List<ELO> children) {
		this.children = children;
	}
	public String toString() {
		return "I am an MockELO. My ID is "+id+", my title is"+ title+" and my content is: "+content;
	}
}
