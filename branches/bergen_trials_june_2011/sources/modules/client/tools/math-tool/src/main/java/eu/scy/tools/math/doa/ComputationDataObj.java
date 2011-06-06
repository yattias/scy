package eu.scy.tools.math.doa;

import java.util.Vector;

import eu.scy.tools.math.ui.UIUtils;

public class ComputationDataObj {

	private String name;
	private Float value;
	private Integer columnNumber;
	private Float sum;
	private String ratio;
	private String surfaceArea;
	private String volume;
	private String operation;
	private String shapeId;
	
	public ComputationDataObj(Integer columnNumber, String name, Float value, Float sum) {
		this.setColumnNumber(columnNumber);
		this.name = name;
		this.value = value;
		this.setSum(sum);
	}
	
	public ComputationDataObj(Object[] values, String type) {
		
		if( type.equals(UIUtils._3D)) {
			this.columnNumber  = (Integer)values[0];
			this.name = (String)values[1];
			this.setRatio((String)values[2]);
			this.setSurfaceArea((String)values[3]);
			this.setVolume((String)values[4]);
		} else {
			this.columnNumber = (Integer) values[0];
			this.name = (String) values[1];
			this.value = (Float) values[2];
			this.sum = (Float) values[3];
			this.operation = (String) values[4];
			this.shapeId = (String) values[5];
		}
		
	}
	
	public ComputationDataObj(Vector vector, String type) {
		
		if( type.equals(UIUtils._3D)) {
			this.columnNumber  = (Integer)vector.get(0);
			this.name = (String) vector.get(1);
			this.setRatio((String) vector.get(2));
			this.setSurfaceArea((String) vector.get(3));
			this.setVolume((String) vector.get(4));
			this.shapeId = (String)vector.get(5);
		} else {
			this.columnNumber  = (Integer)vector.get(0);
			this.name = (String) vector.get(1);
			this.value = (Float)vector.get(2);
			this.sum = (Float) vector.get(3);
			this.operation = (String)vector.get(4);
			this.shapeId = (String)vector.get(5);
		}
	}

	public void setValue(Float value) {
		this.value = value;
	}
	public Float getValue() {
		return value;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getName() {
		return name;
	}
	public Object[] toArray(String type) {
		if( type.equals(UIUtils._3D)) {
			return new Object[]{ columnNumber, name,ratio, surfaceArea, volume, shapeId};
		}
		return new Object[]{ columnNumber, name, value, sum,operation,shapeId};
	}
	@Override
	public String toString() {
		return "I am data " + name + " " + value;
	}
	public void setColumnNumber(Integer columnNumber) {
		this.columnNumber = columnNumber;
	}
	public Integer getColumnNumber() {
		return columnNumber;
	}
	public void setSum(Float sum) {
		this.sum = sum;
	}
	public Float getSum() {
		return sum;
	}

	public void setRatio(String ratio) {
		this.ratio = ratio;
	}

	public String getRatio() {
		return ratio;
	}

	public void setSurfaceArea(String surfaceArea) {
		this.surfaceArea = surfaceArea;
	}

	public String getSurfaceArea() {
		return surfaceArea;
	}

	public void setVolume(String volume) {
		this.volume = volume;
	}

	public String getVolume() {
		return volume;
	}

	public void setOperation(String operation) {
		this.operation = operation;
	}

	public String getOperation() {
		return operation;
	}

	public void setShapeId(String shapeId) {
		this.shapeId = shapeId;
	}

	public String getShapeId() {
		return shapeId;
	}
	
}
