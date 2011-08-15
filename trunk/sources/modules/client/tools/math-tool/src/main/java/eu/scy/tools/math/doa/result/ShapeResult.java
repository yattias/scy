package eu.scy.tools.math.doa.result;

public class ShapeResult {

	protected String surfaceArea;
	protected String surfaceAreaRatio;

	public ShapeResult(String surfaceArea, String surfaceAreaRatio) {
		this.surfaceArea = surfaceArea;
		this.surfaceAreaRatio = surfaceAreaRatio;
	}
	
	public String getSurfaceArea() {
		return surfaceArea;
	}

	public void setSurfaceArea(String surfaceArea) {
		this.surfaceArea = surfaceArea;
	}

	public String getSurfaceAreaRatio() {
		return surfaceAreaRatio;
	}

	public void setSurfaceAreaRatio(String surfaceAreaRatio) {
		this.surfaceAreaRatio = surfaceAreaRatio;
	}

	

}