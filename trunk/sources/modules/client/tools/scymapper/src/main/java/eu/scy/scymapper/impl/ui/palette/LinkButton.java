package eu.scy.scymapper.impl.ui.palette;

import eu.scy.scymapper.api.shapes.ILinkShape;
import eu.scy.scymapper.api.shapes.INodeShape;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;

/**
 * User: Bjoerge Naess
 * Date: 23.sep.2009
 * Time: 15:47:33
 */
public class LinkButton extends JButton {

	private ILinkShape shape;

	public LinkButton(String text, ILinkShape shape) {
        super(new LinkIcon(shape, 20, 20));
		this.shape = shape;
		setText(text);
    }
	static class LinkIcon implements Icon {
		private ILinkShape shape;
		private int iconHeight;
		private int iconWidth;

		LinkIcon(ILinkShape shape, int iconHeight, int iconWidth) {
			this.shape = shape;
			this.iconHeight = iconHeight;
			this.iconWidth = iconWidth;
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2d = (Graphics2D) g.create();

			g2d.setColor(c.getForeground());
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			g2d.translate(x, y);

			shape.paint(g2d, new Point(0, iconHeight/2), new Point(iconWidth, iconHeight/2));

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
