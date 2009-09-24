package eu.scy.scymapper.impl.ui.palette;

import eu.scy.scymapper.api.shapes.ILinkShape;

import javax.swing.*;
import java.awt.*;

/**
 * User: Bjoerge Naess
 * Date: 23.sep.2009
 * Time: 15:47:33
 */
public class LinkButton extends JButton {

	private ILinkShape shape;

	public LinkButton(String text, ILinkShape shape) {
        super(text);
		this.shape = shape;
    }

	public void paintComponent(Graphics g) {
        super.paintComponent(g);

		g.setColor(new Color(0, 0, 0, 150));
		Point left = new Point(6, (int)getBounds().getCenterY());
		Point right = new Point(getWidth()-6, (int)getBounds().getCenterY());
		Shape s = shape.getShape(left, right);
        ((Graphics2D)g).draw(s);
		g.dispose();
    }
}
