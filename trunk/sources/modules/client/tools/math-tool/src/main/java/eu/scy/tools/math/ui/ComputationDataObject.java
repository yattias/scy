package eu.scy.tools.math.ui;

import java.util.Vector;

public class ComputationDataObject {

	private String name;
	private Float value;
	private Integer columnNumber;
	private Float sum;

	public ComputationDataObject(Integer columnNumber, String name, Float value, Float sum) {
		this.setColumnNumber(columnNumber);
		this.name = name;
		this.value = value;
		this.setSum(sum);
	}
	
	public ComputationDataObject(Object[] values) {
		this.columnNumber = (Integer) values[0];
		this.name = (String) values[1];
		this.value = (Float) values[2];
		this.sum = (Float) values[3];
	}
	
	public ComputationDataObject() {
	}
	
	public ComputationDataObject(Vector vector) {
		this.columnNumber  = (Integer)vector.get(0);
		this.name = (String) vector.get(1);
		this.value = (Float)vector.get(2);
		this.sum = (Float) vector.get(3);
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
	public Object[] toArray() {
		return new Object[]{ columnNumber, name, value, sum};
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
	
}
