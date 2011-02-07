package eu.scy.tools.math.doa.json;

public class RectangleToolbarShape extends MathToolbarShape implements IRectangleToolbarShape {

	private String height;
	private String width;
	
	@Override
	public void setHeight(String height) {
		this.height = height;
	}
	
	@Override
	public String getHeight() {
		return height;
	}
	
	@Override
	public void setWidth(String width) {
		this.width = width;
	}
	@Override
	public String getWidth() {
		return width;
	}
	
	
}
