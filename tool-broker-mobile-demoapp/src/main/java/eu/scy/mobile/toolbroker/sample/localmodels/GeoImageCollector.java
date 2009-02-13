package eu.scy.mobile.toolbroker.sample.localmodels;

import javax.microedition.lcdui.Image;
import java.util.Vector;

/**
 * Created: 11.feb.2009 09:50:17
 *
 * @author Bjørge Næss
 */


public class GeoImageCollector {

	private String name;

	private Vector images = new Vector();

	public GeoImageCollector() {}
	public GeoImageCollector(String name, Vector images) {
		this.name = name;
		this.images = images;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Vector getImages() {
		return images;
	}

	public void setImages(Vector images) {
		this.images = images;
	}

	public void addImage(Image image) {
		this.images.addElement(image);
	}

	public String toString() {
		return "I am a GeoImageCollector (" +
				"name='" + name + '\'' +
				')';
	}
}
