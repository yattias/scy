package eu.scy.tools.math.doa;

import java.awt.Point;

public class ThreeDObj {

	private String radius;

	private String surfaceArea;
	private String ratio;
	private Point position;
	private String length;
	private String type;
	private String height;
	private String width;
	private String volume;
	private String icon;

	
	public ThreeDObj() {
	}
	
	public ThreeDObj(String length, String radius, String surfaceArea, String ratio,
			Point position, String type, String height, String width, String volume, String icon) {
		this.setLength(length);
		this.radius = radius;
		this.surfaceArea = surfaceArea;
		this.ratio = ratio;
		this.position = position;
		this.type = type;
		this.height = height;
		this.width = width;
		this.volume = volume;
		this.icon = icon;
	}
	
	public void setPosition(Point position) {
		this.position = position;
	}
	public Point getPosition() {
		return position;
	}
	public void setRatio(String ratio) {
		this.ratio = ratio;
	}
	public String getRatio() {
		return ratio;
	}
	public void setSurfaceArea(String surfaceArea) {
		this.surfaceArea = surfaceArea;
	}
	public String getSurfaceArea() {
		return surfaceArea;
	}
	public void setRadius(String radius) {
		this.radius = radius;
	}
	public String getRadius() {
		return radius;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getLength() {
		return length;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getType() {
		return type;
	}

	public void setHeight(String height) {
		this.height = height;
	}

	public String getHeight() {
		return height;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getWidth() {
		return width;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getVolume() {
		return volume;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	public String getIcon() {
		return icon;
	}
	
}
