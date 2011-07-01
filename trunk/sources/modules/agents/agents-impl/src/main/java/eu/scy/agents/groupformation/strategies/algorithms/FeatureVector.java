/**
 * 
 */
package eu.scy.agents.groupformation.strategies.algorithms;

import java.util.ArrayList;
import java.util.List;

public class FeatureVector {

	private List<Double> vector;
	private String id;

	public FeatureVector() {
		this("");
	}

    public FeatureVector(String i) {
		id = i;
		vector = new ArrayList<Double>();
	}

	public FeatureVector(String i, double[] ds) {
		id = i;
		vector = new ArrayList<Double>();
		setVector(ds);
	}

	public double[] getVector() {
		double[] result = new double[vector.size()];
		for (int i = 0; i < vector.size(); i++) {
			result[i] = vector.get(i);
		}
		return result;
	}

	public void setVector(double[] vector) {
		for (double element : vector) {
			this.vector.add(element);
		}
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		StringBuilder stringRepresentation = new StringBuilder();
		stringRepresentation.append(id);
		stringRepresentation.append("(");
		stringRepresentation.append(vector.get(0));
		for (int i = 1; i < vector.size(); i++) {
			stringRepresentation.append(", ");
			stringRepresentation.append(vector.get(i));
		}
		stringRepresentation.append(")");
		return stringRepresentation.toString();
	}

	public static FeatureVector[] createFeatureVectors(double[][] data) {
		FeatureVector[] featureVectors = new FeatureVector[data.length];
		for (int i = 0; i < data.length; i++) {
			featureVectors[i] = new FeatureVector("" + i, data[i].clone());
		}
		return featureVectors;
	}

	public void set(int featureVectorIndex, double value) {
		vector.set(featureVectorIndex, value);
	}

	public void add(double[] values) {
		for (double value : values) {
			vector.add(value);
		}
	}

}