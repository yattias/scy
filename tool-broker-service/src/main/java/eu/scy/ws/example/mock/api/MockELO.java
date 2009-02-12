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
@XmlRootElement(name = "elo",namespace = "eu.scy")
public class MockELO implements ELO {
	private String title;
	private Object content;
	private int id;

    @XmlElement
	private List<MockELO> children = new ArrayList<MockELO>();

    @XmlElement
	private List<GeoImageCollector> geoImageCollectors = new ArrayList<GeoImageCollector>();

	public MockELO() {}
	public MockELO(int id, String title, String content) {
		this.id = id;
		this.title = title;
		this.content = content;
	}

	public MockELO(int id, String title) {
		this.id = id;
		this.title = title;
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

	public Object getContent() {
		return content;
	}

	public void setContent(Object content) {
		this.content = content;
	}

	public List<MockELO> getChildren() {
		return children;
	}
	public void addChildELO(ELO elo) {
		this.children.add((MockELO)elo);
	}
	public void setChildren(List<?extends ELO> children) {
		this.children = (List<MockELO>)children;
	}
	public String toString() {
		return "I am an MockELO. My ID is "+id+", my title is"+ title+" and my content is: "+content;
	}

	public List<GeoImageCollector> getGeoImageCollectors() {
		return geoImageCollectors;
	}

	public void setGeoImageCollectors(List<GeoImageCollector> geoImageCollectors) {
		this.geoImageCollectors = geoImageCollectors;
	}
}
