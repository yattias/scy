package eu.scy.scymapper.impl.ui.palette;

import eu.scy.scymapper.api.shapes.INodeShape;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 18.jun.2009
 * Time: 16:33:18
 * To change this template use File | Settings | File Templates.
 */
public class ShapeButton extends JButton {
	private INodeShape shape;

	public ShapeButton(String text, INodeShape shape) {
        super(text);
		this.shape = shape;
    }

	public INodeShape getShape() {
		return shape;
	}

	public void paintComponent(Graphics g) {
        super.paintComponent(g);
		Rectangle shrinked = new Rectangle(getBounds());
		shrinked.grow(-6, -6);
		g.translate(6, 6);
		shape.setMode(INodeShape.FILL);
		g.setColor(new Color(200, 200, 200, 200));
        shape.paint(g, shrinked);
		g.dispose();
    }
}
