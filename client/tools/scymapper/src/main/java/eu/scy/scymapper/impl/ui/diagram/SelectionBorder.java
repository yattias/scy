package eu.scy.scymapper.impl.ui.diagram;

import javax.swing.border.Border;
import java.awt.*;

/**
 * @author bjoerge
 * @created 14.des.2009 12:49:25
 */
class SelectionBorder implements Border {

	private Color color1 = new Color(255, 255, 255, 170);
	private Color color2 = new Color(20, 20, 20, 170);

	private Insets insets = new Insets(2, 2, 2, 2);

	public SelectionBorder() {
	}

	public SelectionBorder(Insets insets) {
		this.insets = insets;
	}

	@Override
	public void paintBorder(Component c, Graphics g, int x, int y, int width, int height) {
		g.setColor(color1);
		drawDashedRect(g, x+insets.left, y+insets.top, width-insets.left-insets.right-1, height-insets.top-insets.bottom-1, 0, 1);
		g.setColor(color2);
		drawDashedRect(g, x+insets.left, y+insets.top, width-insets.left-insets.right-1, height-insets.top-insets.bottom-1, 1, 0);
	}

	@Override
	public Insets getBorderInsets(Component c) {
		return insets;
	}
	void drawDashedRect(Graphics g, int x, int y, int width, int height, int hOffset, int vOffset) {
		int vx,vy;

        // draw upper and lower horizontal dashes
        for (vx = x+hOffset; vx < (x + width); vx+=2) {
            g.fillRect(vx, y, 1, 1);
            g.fillRect(vx, y + height-1, 1, 1);
        }

        // draw left and right vertical dashes
        for (vy = y+vOffset; vy < (y + height); vy+=2) {
	    g.fillRect(x, vy, 1, 1);
            g.fillRect(x+width-1, vy, 1, 1);
        }
	}

	@Override
	public boolean isBorderOpaque() {
		return true;
	}
}
