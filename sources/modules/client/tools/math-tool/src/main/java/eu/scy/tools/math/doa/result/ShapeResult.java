package eu.scy.tools.math.doa.result;

public class ShapeResult {

	protected String surfaceArea;
	protected String surfaceAreaRatio;
	protected String name;
	
	

	public ShapeResult(String name, String surfaceArea, String surfaceAreaRatio) {
		this.name = name;
		this.surfaceArea = surfaceArea;
		this.surfaceAreaRatio = surfaceAreaRatio;
	}
	
	public ShapeResult(String surfaceArea, String surfaceAreaRatio) {
		this.surfaceArea = surfaceArea;
		this.surfaceAreaRatio = surfaceAreaRatio;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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