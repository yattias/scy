package eu.scy.tools.math.doa.json;

public class RectanglarPrismToolbarShape extends ToolbarShape implements
		IRectanglarPrismToolbarShape {

	private String length;
	private String width;
	private String height;
	
	@Override
	public void setLength(String length) {
		this.length = length;
	}

	@Override
	public String getLength() {
		return length;
	}

	@Override
	public void setWidth(String width) {
		this.width = width;
	}

	@Override
	public String getWidth() {
		return width;
	}

	@Override
	public void setHeight(String height) {
		this.height = height;
	}

	@Override
	public String getHeight() {
		return height;
	}
}
