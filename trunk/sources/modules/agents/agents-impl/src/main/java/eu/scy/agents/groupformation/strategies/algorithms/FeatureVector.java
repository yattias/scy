/**
 * 
 */
package eu.scy.agents.groupformation.strategies.algorithms;

public class FeatureVector {

	private double[] vector;
	private String id;

	public FeatureVector() {
		this("", null);
	}

	public FeatureVector(String i, double[] ds) {
		id = i;
		vector = ds;
	}

	public double[] getVector() {
		return vector;
	}

	public void setVector(double[] vector) {
		this.vector = vector;
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
		stringRepresentation.append(vector[0]);
		for (int i = 1; i < vector.length; i++) {
			stringRepresentation.append(", ");
			stringRepresentation.append(vector[i]);
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
		vector[featureVectorIndex] = value;
	}

}