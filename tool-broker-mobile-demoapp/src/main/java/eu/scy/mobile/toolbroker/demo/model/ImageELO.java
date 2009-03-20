package eu.scy.mobile.toolbroker.demo.model;

import javax.microedition.lcdui.Image;

/**
 * Created by IntelliJ IDEA.
 * User: Bjørge Næss
 * Date: 20.mar.2009
 * Time: 16:37:41
 * To change this template use File | Settings | File Templates.
 */
public class ImageELO {
	private String title;
	private String comment;
	private Image image;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Image getImage() {
		return image;
	}

	public void setImage(Image image) {
		this.image = image;
	}
}
