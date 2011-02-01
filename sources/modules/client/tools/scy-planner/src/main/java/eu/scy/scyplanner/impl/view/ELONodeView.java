package eu.scy.scyplanner.impl.view;

import eu.scy.scymapper.api.diagram.controller.INodeController;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.diagram.view.NodeViewComponent;

import java.awt.*;

/**
 * @author bjoerge
 * @created 10.des.2009 13:05:41
 */
public class ELONodeView extends NodeViewComponent {
	public ELONodeView(INodeController controller, INodeModel model) {
		super(controller, model, true);
	}

	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D)g.create();

		// System.out.println("getModel().getLabel() = " + getModel().getLabel());
		g2.drawString(getModel().getLabel(), 0, 0);
	}
}
