package eu.scy.tools.math.ui.border;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.border.LineBorder;

public class DashBorder extends LineBorder{

	public DashBorder(Color c) {
		this(c, 1);
	}

	public DashBorder(Color c, int thickness) {
		this(c, thickness, new float[] { 5, 5 });
	}

	public DashBorder(Color c, int thickness, float [] dash) {
		super(c, thickness);
		this.stroke = new BasicStroke(thickness, BasicStroke.CAP_BUTT,BasicStroke.JOIN_BEVEL, 1, dash, 0);
		}

	private boolean solid;
	private BasicStroke stroke;

	public void paintBorder(Component c, Graphics g, int x, int y, int width,
			int height) {
		if (!solid) {
			Graphics2D g2d = (Graphics2D) g.create();
			g2d.setStroke(stroke);
			super.paintBorder(c, g2d, x, y, width, height);
			g2d.dispose();
		} else {
			super.paintBorder(c, g, x, y, width, height);
		}
	}

}
