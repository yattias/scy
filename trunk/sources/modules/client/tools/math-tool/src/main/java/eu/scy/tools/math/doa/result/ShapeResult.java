package eu.scy.tools.math.doa.result;

public class ShapeResult {

	protected String surfaceArea;
	protected String surfaceAreaRatio;
	protected String name;
	private String imageIconName;
	private String volume;
	
	

	public ShapeResult(String surfaceArea, String surfaceAreaRatio) {
		this.surfaceArea = surfaceArea;
		this.surfaceAreaRatio = surfaceAreaRatio;
	}
	
	public ShapeResult(String name, String surfaceArea, String surfaceAreaRatio) {
		this.name = name;
		this.surfaceArea = surfaceArea;
		this.surfaceAreaRatio = surfaceAreaRatio;
	}
	
	public ShapeResult(String name, String surfaceArea, String surfaceAreaRatio,String volume, String imageIconName) {
		this.name = name;
		this.surfaceArea = surfaceArea;
		this.surfaceAreaRatio = surfaceAreaRatio;
		this.volume = volume;
		this.imageIconName = imageIconName;
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

	public String getImageIconName() {
		return imageIconName;
	}

	public void setImageIconName(String imageIconName) {
		this.imageIconName = imageIconName;
	}

	public String getVolume() {
		return volume;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	

}