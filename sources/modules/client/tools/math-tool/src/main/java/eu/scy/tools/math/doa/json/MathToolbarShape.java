package eu.scy.tools.math.doa.json;

import java.io.Serializable;

public class MathToolbarShape implements IMathToolbarShape, Serializable {

	private String name;
	private String type;
	private String surfaceType;
	private String toolbarIcon;
	private String canvasIcon;
	@Override
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String getName() {
		return name;
	}
	@Override
	public void setType(String type) {
		this.type = type;
	}
	@Override
	public String getType() {
		return type;
	}
	
	@Override
	public void setSurfaceType(String surfaceType) {
		this.surfaceType = surfaceType;
	}
	@Override
	public String getSurfaceType() {
		return surfaceType;
	}
	@Override
	public void setToolbarIcon(String toolbarIcon) {
		this.toolbarIcon = toolbarIcon;
	}
	@Override
	public String getToolbarIcon() {
		return toolbarIcon;
	}
	@Override
	public void setCanvasIcon(String canvasIcon) {
		this.canvasIcon = canvasIcon;
	}
	@Override
	public String getCanvasIcon() {
		return canvasIcon;
	}
}
