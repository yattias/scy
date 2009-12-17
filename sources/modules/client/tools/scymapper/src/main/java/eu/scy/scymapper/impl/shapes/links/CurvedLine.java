package eu.scy.scymapper.impl.shapes.links;

import eu.scy.scymapper.api.shapes.ILinkShape;

import java.awt.*;
import java.awt.geom.CubicCurve2D;

/**
 * @author bjoerge
 * @created 16.des.2009 18:13:57
 */
public class CurvedLine implements ILinkShape {


	public CurvedLine() {

	}

	@Override
	public void paint(Graphics g, Point from, Point to) {

		CubicCurve2D curve = new CubicCurve2D.Double(from.x, from.y, to.x, from.y, from.x, to.y, to.x, to.y);
		Graphics2D g2d = (Graphics2D)g.create();
		g2d.draw(curve);
        g2d.dispose();
	}
}
