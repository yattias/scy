package eu.scy.tools.math.ui.panels;

import java.util.ArrayList;

import eu.scy.tools.math.shapes.IMathShape;

public interface IShapeCanvas {

	public void addShape(IMathShape shapes);

	public void setMathShapes(ArrayList<IMathShape> mathShapes);

	public ArrayList<IMathShape> getMathShapes();

}
