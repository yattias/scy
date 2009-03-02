package eu.scy.mobile.toolbroker.model.impl;

import com.sun.me.web.request.Arg;
import com.sun.me.web.request.ProgressInputStream;
import com.sun.me.web.request.ProgressListener;

import javax.microedition.lcdui.Image;
import javax.microedition.io.HttpConnection;
import javax.microedition.io.Connector;
import java.util.Vector;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;

import eu.scy.mobile.toolbroker.model.IGeoImageList;

/**
 * Created: 11.feb.2009 09:50:17
 *
 * @author Bjørge Næss
 */

public class GeoImageList implements IGeoImageList {
	private String name;

	private Vector images = new Vector();

	public GeoImageList() {}
	public GeoImageList(String name, Vector images) {
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

	public void addImage(String image) {
		this.images.addElement(image);
	}

	public String toString() {
		return "I am a GeoImageList (" +
				"name='" + name + '\'' +
				')';
	}
}
