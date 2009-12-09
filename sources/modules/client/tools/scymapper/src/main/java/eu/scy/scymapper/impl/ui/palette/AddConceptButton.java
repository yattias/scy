package eu.scy.scymapper.impl.ui.palette;

import eu.scy.scymapper.api.IConceptType;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.api.styling.INodeStyle;

import javax.swing.*;
import java.awt.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 18.jun.2009
 * Time: 16:33:18
 * To change this template use File | Settings | File Templates.
 */
public class AddConceptButton extends JToggleButton {
    private IConceptType type;

    public AddConceptButton(IConceptType type) {
        super();
        setIcon(new ShapedIcon(20, 20));
        this.type = type;
        setText(type.getName());
    }

	class ShapedIcon implements Icon {
		private int iconHeight;
		private int iconWidth;

		ShapedIcon(int iconHeight, int iconWidth) {
			this.iconHeight = iconHeight;
			this.iconWidth = iconWidth;
		}

		@Override
		public void paintIcon(Component c, Graphics g, int x, int y) {
			Graphics2D g2d = (Graphics2D) g.create();

            INodeStyle style = type.getNodeStyle();
            INodeShape shape = type.getNodeShape();

			g2d.setColor(style.getBackground());
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			Rectangle rect = new Rectangle(x, y, iconWidth, iconHeight);
            shape.setMode(style.isOpaque() ? INodeShape.FILL : INodeShape.DRAW);
//            g2d.drawRect(rect.x, rect.y, rect.width, rect.height);
            shape.paint(g2d, rect);
            g2d.dispose();
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
