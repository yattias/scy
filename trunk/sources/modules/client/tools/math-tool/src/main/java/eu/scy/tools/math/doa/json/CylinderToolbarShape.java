package eu.scy.tools.math.doa.json;


public class CylinderToolbarShape extends ToolbarShape implements
		ICylinderToolbarShape {

	private String height;
	private String radiusMinValue;
	private String radiusMaxValue;
	private String radius;
	
	@Override
	public void setHeight(String height) {
		this.height = height;
	}
	@Override
	public String getHeight() {
		return height;
	}
	@Override
	public void setRadiusMinValue(String radiusMinValue) {
		this.radiusMinValue = radiusMinValue;
	}
	@Override
	public String getRadiusMinValue() {
		return radiusMinValue;
	}
	@Override
	public void setRadiusMaxValue(String radiusMaxValue) {
		this.radiusMaxValue = radiusMaxValue;
	}
	@Override
	public String getRadiusMaxValue() {
		return radiusMaxValue;
	}
	
	@Override
	public void setRadius(String radius) {
		this.radius = radius;
	}
	
	@Override
	public String getRadius() {
		return radius;
	}
	
}
