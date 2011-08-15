package eu.scy.tools.math.doa.result;

public class CircularShapeResult extends ShapeResult {

	private String radius;
	
	public CircularShapeResult() {
		super(null, null);
	}
	
	public CircularShapeResult(String surfaceArea, String surfaceAreaRatio, String radius) {
		super(surfaceArea, surfaceAreaRatio);
		this.radius = radius;
	}

	public String getRadius() {
		return radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}
}
