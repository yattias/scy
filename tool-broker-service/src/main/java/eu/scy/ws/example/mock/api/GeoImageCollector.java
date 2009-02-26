package eu.scy.ws.example.mock.api;

import javax.xml.bind.annotation.*;
import java.util.List;

/**
 * Created: 11.feb.2009 09:50:17
 *
 * @author Bjørge Næss
 */

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "geoimagecollector")
@XmlType(name = "geoimagecollector", namespace = "http://scy.eu")
public class GeoImageCollector {

	private String name;

	private List<String> images;

	public GeoImageCollector() {}
	public GeoImageCollector(String name, List<String> images) {
		this.name = name;
		this.images = images;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;

	}
	public List<String> getImages() {
		return images;
	}

	public void setImages(List<String> images) {
		this.images = images;
	}
}
