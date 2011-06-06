package eu.scy.tools.math.doa.json;

public class CircleToolbarShape extends MathToolbarShape implements ICircleToolbarShape {

	private String radius;

	@Override
	public void setRadius(String radius) {
		this.radius = radius;
	}

	@Override
	public String getRadius() {
		return radius;
	}
}
