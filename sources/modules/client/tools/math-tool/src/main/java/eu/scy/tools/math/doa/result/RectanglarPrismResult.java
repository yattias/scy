package eu.scy.tools.math.doa.result;

public class RectanglarPrismResult extends ShapeResult {
	
	protected String length;
	private String height;
	private String width;
	
	public RectanglarPrismResult() {
		super(null, null);
	}
	
	public RectanglarPrismResult(String surfaceArea, String surfaceAreaRatio, String length, String height, String width) {
		super(surfaceArea, surfaceAreaRatio);
		this.length = length;
		this.height = height;
		this.width = width;
	}
	
	public RectanglarPrismResult(String name, String surfaceArea, String surfaceAreaRatio, String volume, String canvasIcon, String height, String width) {
		super(name, null, null, volume, canvasIcon);
		this.height = height;
		this.width = width;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}

	public String getWidth() {
		return width;
	}

	public void setWidth(String width) {
		this.width = width;
	}

	public String getHeight() {
		return height;
	}

	public void setHeight(String height) {
		this.height = height;
	}
	
}
