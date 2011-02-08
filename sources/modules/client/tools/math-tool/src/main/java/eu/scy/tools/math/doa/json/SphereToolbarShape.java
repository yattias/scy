package eu.scy.tools.math.doa.json;

public class SphereToolbarShape extends ToolbarShape implements
		ISphereToolbarShape {

	private String radius;
	private String radiusMinValue;
	private String radiusMaxValue;

	@Override
	public void setRadius(String radius) {
		this.radius = radius;
	}

	@Override
	public String getRadius() {
		return radius;
	}

	@Override
	public void setRadiusMinValue(String radiusMinValue) {
		this.radiusMinValue = radiusMinValue;
	}

	@Override
	public String getRadiusMinValue() {
		return this.radiusMinValue;
	}
	
	@Override 
	public String getRadiusMaxValue() {
		return this.radiusMaxValue;
	}
	
	@Override
	public void setRadiusMaxValue(String radiusMaxValue) {
		this.radiusMaxValue = radiusMaxValue;
	}
}
