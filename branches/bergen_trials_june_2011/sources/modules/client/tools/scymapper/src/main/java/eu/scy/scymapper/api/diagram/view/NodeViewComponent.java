package eu.scy.scymapper.api.diagram.view;

import eu.scy.scymapper.api.configuration.ISCYMapperToolConfiguration;
import eu.scy.scymapper.api.diagram.controller.INodeController;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.impl.configuration.SCYMapperToolConfiguration;
import eu.scy.scymapper.impl.model.DefaultNodeStyle;
import org.apache.log4j.Logger;
import org.jdesktop.swingx.graphics.GraphicsUtilities;
import org.jdesktop.swingx.graphics.ShadowRenderer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * @author bjoerge
 * @created 09.des.2009 17:45:58
 */
public class NodeViewComponent extends JComponent {

	private final static Logger logger = Logger.getLogger(NodeViewComponent.class);

	private static final INodeStyle DEFAULT_NODESTYLE = new DefaultNodeStyle();
	private static final ISCYMapperToolConfiguration conf = SCYMapperToolConfiguration.getInstance();

	private INodeController controller;
	private INodeModel model;
	private BufferedImage shadow;

	protected int shadowSize = 3;

	public NodeViewComponent(INodeController controller, INodeModel model) {
		this.controller = controller;
		this.model = model;
		setSize(model.getSize());
		setLocation(model.getLocation());
	}

	public NodeViewComponent(INodeController controller, INodeModel model, boolean toolTip) {
		this(controller, model);
		if (toolTip) {
			setToolTipText(model.getLabel());
		}
	}

	public INodeController getController() {
		return controller;
	}

        public void setController(INodeController controller) {
            this.controller = controller;
        }

	public INodeModel getModel() {
		return model;
	}

	@Override
	public void setBounds(int x, int y, int width, int height) {

		boolean isResized = width != getWidth() || height != getHeight();

		super.setBounds(x, y, width, height);

		if (isResized && getModel().getStyle().getPaintShadow()) {
			if (!conf.getViewShadow()) shadow = null;
			else createShadow();
		}
	}

	public void createShadow() {

		Rectangle relativeBounds = new Rectangle(new Point(0, 0), getSize());
		relativeBounds.width -= shadowSize * 2;
		relativeBounds.height -= shadowSize * 2;

		//if (shadow == null) {
		shadow = GraphicsUtilities.createCompatibleTranslucentImage(getWidth(), getHeight());
		Graphics2D g2 = shadow.createGraphics();

		INodeShape shape = model.getShape();
		if (shape != null) {
			shape.paint(g2, relativeBounds);
			ShadowRenderer renderer = new ShadowRenderer(shadowSize, 0.3f, Color.black);
			shadow = renderer.createShadow(shadow);
			g2 = shadow.createGraphics();
			g2.setComposite(AlphaComposite.Clear);
		}
		g2.dispose();
		//}
	}

	public void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D) g.create();

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		INodeStyle style = model.getStyle();
		if (style == null) style = DEFAULT_NODESTYLE;

		if (conf.getViewShadow() && style.getPaintShadow()) {
			if (shadow == null) createShadow();
			g2.drawImage(shadow, 0, 0, null);
		}
		g2.setColor(style.getBackground());
		g2.setStroke(style.getStroke());

		Rectangle relativeBounds = new Rectangle(new Point(shadowSize * 2, shadowSize * 2), getSize());
		relativeBounds.width -= shadowSize * 4;
		relativeBounds.height -= shadowSize * 4;

		INodeShape shape = model.getShape();
		if (shape != null) {
			shape.setMode(style.isOpaque() ? INodeShape.FILL : INodeShape.DRAW);
			shape.paint(g2, relativeBounds);
		} else {
			// No shape defined, painting boring rectangle instead
			g2.drawRect(getX(), getY(), getWidth(), getHeight());
		}

		// Paint border
		if (style.getBorder() != null)
			style.getBorder().paintBorder(this, g2, relativeBounds.x, relativeBounds.y, relativeBounds.width, relativeBounds.height);

		// Continue painting any other components
		super.paintComponent(g);
	}

	public Point getConnectionPoint(Point p) {
		return model.getConnectionPoint(p);
	}
}
