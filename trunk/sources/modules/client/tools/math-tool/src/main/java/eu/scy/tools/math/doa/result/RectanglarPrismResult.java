package eu.scy.tools.math.doa.result;

public class RectanglarPrismResult extends ShapeResult {
	
	protected String length;
	
	public RectanglarPrismResult() {
		super(null, null);
	}
	
	public RectanglarPrismResult(String surfaceArea, String surfaceAreaRatio, String length) {
		super(surfaceArea, surfaceAreaRatio);
		this.length = length;
	}

	public String getLength() {
		return length;
	}

	public void setLength(String length) {
		this.length = length;
	}
	
}
