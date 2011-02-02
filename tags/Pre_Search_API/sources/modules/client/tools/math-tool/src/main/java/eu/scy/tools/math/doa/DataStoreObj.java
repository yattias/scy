package eu.scy.tools.math.doa;

import java.util.ArrayList;
import java.util.List;

import eu.scy.tools.math.shapes.IMathShape;

public class DataStoreObj {
	
	private String type;
	private String scratchPadText;
	private List<IMathShape> twoDMathShapes = new ArrayList<IMathShape> ();
	private List<ThreeDObj> threeDMathShapes = new ArrayList<ThreeDObj> ();
	
	private List<ComputationDataObj> tablesObjects = new ArrayList<ComputationDataObj> ();
	
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setTablesObjects(List<ComputationDataObj> tablesObjects) {
		this.tablesObjects = tablesObjects;
	}
	public List<ComputationDataObj> getTablesObjects() {
		return tablesObjects;
	}
	public void setTwoDMathShapes(List<IMathShape> twoDMathShapes) {
		this.twoDMathShapes = twoDMathShapes;
	}
	public List<IMathShape> getTwoDMathShapes() {
		return twoDMathShapes;
	}
	public void setThreeDMathShapes(List<ThreeDObj> threeDMathShapes) {
		this.threeDMathShapes = threeDMathShapes;
	}
	public List<ThreeDObj> getThreeDMathShapes() {
		return threeDMathShapes;
	}
	public void setScratchPadText(String scratchPadText) {
		this.scratchPadText = scratchPadText;
	}
	public String getScratchPadText() {
		return scratchPadText;
	}

}
