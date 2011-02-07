package eu.scy.tools.math.doa.json;

import java.io.Serializable;

public class ToolbarShape extends MathToolbarShape implements IToolbarShape, IMathToolbarShape , Serializable{

	private String name;
	private String type;
	private String surfaceType;
	private String toolbarIcon;
	private String canvasIcon;
	
	private String volume;
	
	private String surfaceArea;
	private String surfaceAreaMinValue;
	private String surfaceAreaMaxValue;
	
	private String surfaceAreaRatio;
	private String surfaceAreaRatioMinValue;
	private String surfaceAreaRatioMaxValue;
	

	@Override
	public void setVolume(String volume) {
		this.volume = volume;
	}
	@Override
	public String getVolume() {
		return volume;
	}
	@Override
	public void setSurfaceArea(String surfaceArea) {
		this.surfaceArea = surfaceArea;
	}
	@Override
	public String getSurfaceArea() {
		return surfaceArea;
	}
	@Override
	public void setSurfaceAreaMinValue(String surfaceAreaMinValue) {
		this.surfaceAreaMinValue = surfaceAreaMinValue;
	}
	@Override
	public String getSurfaceAreaMinValue() {
		return surfaceAreaMinValue;
	}
	@Override
	public void setSurfaceAreaMaxValue(String surfaceAreaMaxValue) {
		this.surfaceAreaMaxValue = surfaceAreaMaxValue;
	}
	@Override
	public String getSurfaceAreaMaxValue() {
		return surfaceAreaMaxValue;
	}
	@Override
	public void setSurfaceAreaRatio(String surfaceAreaRatio) {
		this.surfaceAreaRatio = surfaceAreaRatio;
	}
	@Override
	public String getSurfaceAreaRatio() {
		return surfaceAreaRatio;
	}
	@Override
	public void setSurfaceAreaRatioMinValue(String surfaceAreaRatioMinValue) {
		this.surfaceAreaRatioMinValue = surfaceAreaRatioMinValue;
	}
	@Override
	public String getSurfaceAreaRatioMinValue() {
		return surfaceAreaRatioMinValue;
	}
	@Override
	public void setSurfaceAreaRatioMaxValue(String surfaceAreaRatioMaxValue) {
		this.surfaceAreaRatioMaxValue = surfaceAreaRatioMaxValue;
	}
	@Override
	public String getSurfaceAreaRatioMaxValue() {
		return surfaceAreaRatioMaxValue;
	}
	
	
	
}
