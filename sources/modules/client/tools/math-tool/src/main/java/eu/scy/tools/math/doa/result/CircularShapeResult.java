package eu.scy.tools.math.doa.result;

public class CircularShapeResult extends ShapeResult {

	private String radius;
	private String height;
	
	public CircularShapeResult() {
		super(null, null);
	}
	
	public CircularShapeResult(String surfaceArea, String surfaceAreaRatio, String radius) {
		super(surfaceArea, surfaceAreaRatio);
		this.radius = radius;
	}
	
	public CircularShapeResult(String name) {
		super(name, null, null);
	}

	public CircularShapeResult(String name, String surfaceArea, String surfaceAreaRatio, String volume, String canvasIcon, String height) {
		super(name, null, null, volume, canvasIcon);
		this.height = height;
	}

	public String getRadius() {
		return radius;
	}

	public void setRadius(String radius) {
		this.radius = radius;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}
}
