package eu.scy.scyplanner.impl.view;

import eu.scy.scymapper.api.diagram.controller.INodeController;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.diagram.view.NodeViewComponent;

import eu.scy.scyplanner.application.SCYPlannerApplicationManager;
import javax.swing.*;
import javax.swing.plaf.metal.MetalBorders;
import java.awt.*;

/**
 * @author bjoerge
 * @created 10.des.2009 13:02:30
 */
public class LASNodeView extends NodeViewComponent {
	public LASNodeView(INodeController controller, INodeModel model) {
		super(controller, model, true);
		setOpaque(false);
		setLayout(new BorderLayout());
        setBorder(SCYPlannerApplicationManager.getApplicationManager().createDefaultBorder());

		JLabel labelComp = new JLabel(model.getLabel());
		labelComp.setOpaque(false);

		add(labelComp, BorderLayout.NORTH);
	}
}
