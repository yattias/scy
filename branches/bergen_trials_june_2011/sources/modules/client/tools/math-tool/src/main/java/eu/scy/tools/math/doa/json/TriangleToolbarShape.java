package eu.scy.tools.math.doa.json;

import eu.scy.tools.math.shapes.impl.MathSphere3D;

public class TriangleToolbarShape extends MathToolbarShape implements ITriangleToolbarShape {

	private String length;

	@Override
	public void setLength(String length) {
		this.length = length;
	}

	@Override
	public String getLength() {
		return length;
	}
}
