package eu.scy.scyplanner.impl.view;

import eu.scy.scymapper.api.diagram.controller.INodeController;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.diagram.view.NodeViewComponent;

import javax.swing.*;
import javax.swing.plaf.metal.MetalBorders;
import java.awt.*;

/**
 * @author bjoerge
 * @created 10.des.2009 13:02:30
 */
public class LASNodeView extends NodeViewComponent {
	public LASNodeView(INodeController controller, INodeModel model) {
		super(controller, model);
		setOpaque(false);
		setLayout(new BorderLayout(0,0));

		JLabel labelComp = new JLabel(model.getLabel());
		labelComp.setOpaque(false);

		JScrollPane sp = new JScrollPane(labelComp);
		sp.getViewport().setOpaque(false);
		sp.setOpaque(false);

		System.out.println("sp.getBorder() = " + sp.getBorder());

		sp.setBorder(BorderFactory.createEmptyBorder());

		getInsets().set(5, 5, 5, 5);
		add(sp);
	}
}
