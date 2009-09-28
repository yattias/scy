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
        super(new ShapedIcon(shape, 20, 20));
		setText(text);
		this.shape = shape;
    }

	public INodeShape getShape() {
		return shape;
	}
	static class ShapedIcon implements Icon {
		private INodeShape shape;
		private int iconHeight;
		private int iconWidth;

		ShapedIcon(INodeShape shape, int iconHeight, int iconWidth) {
			this.shape = shape;
			this.iconHeight = iconHeight;
			this.iconWidth = iconWidth;
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2d = (Graphics2D) g.create();

			shape.setMode(INodeShape.FILL);
			g2d.setColor(c.getForeground());
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			Rectangle rect = new Rectangle(x, y, iconWidth, iconHeight);
			shape.paint(g2d, rect);
		}

		@Override
		public int getIconWidth() {
			return iconWidth;
		}

		@Override
		public int getIconHeight() {
			return iconHeight;
		}
	}
}
