package eu.scy.tools.math.doa;

import java.util.ArrayList;
import java.util.List;

import eu.scy.tools.math.shapes.IMathShape;

public class DataStoreObj {
	
	private String type;
	private List<IMathShape> mathShapes = new ArrayList<IMathShape> ();
	private List<ComputationDataObject> tablesObjects = new ArrayList<ComputationDataObject> ();
	
	public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}
	public void setMathShapes(List<IMathShape> mathShapes) {
		this.mathShapes = mathShapes;
	}
	public List<IMathShape> getMathShapes() {
		return mathShapes;
	}
	public void setTablesObjects(List<ComputationDataObject> tablesObjects) {
		this.tablesObjects = tablesObjects;
	}
	public List<ComputationDataObject> getTablesObjects() {
		return tablesObjects;
	}

}
