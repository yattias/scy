package eu.scy.tools.math.doa.json;

public interface ISphereToolbarShape extends IToolbarShape {

	public String getRadius();

	public void setRadius(String radius);

	public void setRadiusMinValue(String radiusMinValue);

	public void setRadiusMaxValue(String radiusMaxValue);

	public String getRadiusMinValue();

	public String getRadiusMaxValue();

}
