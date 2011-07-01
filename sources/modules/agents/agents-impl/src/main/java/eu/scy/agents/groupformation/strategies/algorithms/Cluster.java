/**
 * 
 */
package eu.scy.agents.groupformation.strategies.algorithms;

import java.util.ArrayList;
import java.util.List;

public class Cluster {

	private double[] oldCenter;
	private double[] center;

	private List<FeatureVector> assignedMembers;

	public Cluster() {
		assignedMembers = new ArrayList<FeatureVector>();
	}

	public void addFeatureVector(FeatureVector featureVector) {
		assignedMembers.add(featureVector);
	}

	public void calculateCenter() {
		oldCenter = center;
		if (assignedMembers.isEmpty()) {
			return;
		}

		center = new double[assignedMembers.get(0).getVector().length];
		for (FeatureVector featureVector : assignedMembers) {
			double[] vector = featureVector.getVector();
			for (int i = 0; i < vector.length; i++) {
				center[i] += vector[i];
			}
		}
		for (int i = 0; i < center.length; i++) {
			center[i] /= assignedMembers.size();
		}
	}

	public void removeFeatureVector(FeatureVector vector) {
		assignedMembers.remove(vector);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("(");
		builder.append(center[0]);
		for (int i = 1; i < center.length; i++) {
			builder.append(", ");
			builder.append(center[i]);
		}
		builder.append(")");
		builder.append(assignedMembers.toString());
		return builder.toString();
	}

	public double[] getCenter() {
		return center;
	}

	public Object size() {
		return assignedMembers.size();
	}

	public void clear() {
		assignedMembers.clear();
	}

	public boolean centerChanged() {
		if (center == null) {
			return false;
		}
		if (oldCenter == null) {
			return true;
		}
		for (int i = 0; i < center.length; i++) {
			if (center[i] != oldCenter[i]) {
				return true;
			}
		}
		return false;
	}

	public FeatureVector[] getMembers() {
		return assignedMembers
				.toArray(new FeatureVector[assignedMembers.size()]);
	}
}