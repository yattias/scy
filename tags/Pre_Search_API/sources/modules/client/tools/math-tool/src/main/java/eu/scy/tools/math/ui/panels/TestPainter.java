package eu.scy.tools.math.ui.panels;

import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

import eu.scy.tools.math.shapes.IMathShape;

public class TestPainter extends JPanel {

	
	private ArrayList<IMathShape> mathShapes = new ArrayList<IMathShape>();
	
	public void addShape(IMathShape shapes) {
		getMathShapes().add(shapes);
	
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
	for (IMathShape ms : getMathShapes()) {
			ms.paintComponent(g);
		}
	}

	public void setMathShapes(ArrayList<IMathShape> mathShapes) {
		this.mathShapes = mathShapes;
	}

	public ArrayList<IMathShape> getMathShapes() {
		return mathShapes;
	}
}
