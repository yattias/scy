package eu.scy.scymapper.impl.ui.palette;

import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.api.styling.INodeStyle;

import javax.swing.*;
import java.awt.*;

/**
 * @author bjoerge
 * @created 05.feb.2010 20:35:11
 */
public class ConceptShapedIcon implements Icon {
	private INodeModel concept;
	private int iconHeight;
	private int iconWidth;

	public ConceptShapedIcon(INodeModel concept, int iconHeight, int iconWidth) {
		this.concept = concept;
		this.iconHeight = iconHeight;
		this.iconWidth = iconWidth;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y) {
		Graphics2D g2d = (Graphics2D) g.create();

		INodeStyle nodeStyle = this.concept.getStyle();
		INodeShape shape = this.concept.getShape();
		g2d.setColor(nodeStyle.getBackground());
		

		g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		Rectangle rect = new Rectangle(x, y, iconWidth, iconHeight);
		if (shape != null) {
			shape.setMode(nodeStyle.isOpaque() ? INodeShape.FILL : INodeShape.DRAW);
			shape.paint(g2d, rect);
			g2d.setColor(Color.black);
			shape.setMode(INodeShape.DRAW);
			shape.paint(g2d, rect);

		}
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
