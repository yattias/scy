package eu.scy.scymapper.api.diagram.view;

import eu.scy.scymapper.api.diagram.controller.INodeController;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.impl.model.DefaultNodeStyle;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;

/**
 * @author bjoerge
 * @created 09.des.2009 17:45:58
 */
public class NodeViewComponent extends JComponent {

	private final static Logger logger = Logger.getLogger(NodeViewComponent.class);

    private static final INodeStyle DEFAULT_NODESTYLE = new DefaultNodeStyle();

	private INodeController controller;
	private INodeModel model;

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

	public INodeModel getModel() {
		return model;
	}

	public void paintComponent(Graphics g) {

		Graphics2D g2 = (Graphics2D) g.create();

		g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		INodeStyle style = model.getStyle();
		if (style == null) style = DEFAULT_NODESTYLE;

		g2.setColor(style.getBackground());
		g2.setStroke(style.getStroke());

		Rectangle relativeBounds = new Rectangle(new Point(0, 0), getSize());

		INodeShape shape = model.getShape();
		if (shape != null) {
			shape.setMode(style.isOpaque() ? INodeShape.FILL : INodeShape.DRAW);
			shape.paint(g2, relativeBounds);
		} else {
			logger.warn("No shape defined, painting boring rectangle instead");
			g2.drawRect(getX(), getY(), getWidth(), getHeight());
		}
		// Continue painting any other components
		super.paintComponent(g);
	}

	public Point getConnectionPoint(Point p) {
		return model.getConnectionPoint(p);
	}
}
