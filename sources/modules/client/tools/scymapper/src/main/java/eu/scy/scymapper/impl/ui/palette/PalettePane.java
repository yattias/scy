package eu.scy.scymapper.impl.ui.palette;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.scy.scymapper.api.diagram.IDiagramSelectionModel;
import eu.scy.scymapper.api.diagram.INodeModel;
import eu.scy.scymapper.api.shapes.ILinkShape;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.impl.SCYMapper;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * User: Bjoerge Naess
 * Date: 03.sep.2009
 * Time: 13:24:59
 */
public class PalettePane extends JPanel {
	private IDiagramSelectionModel selectionModel;
	private ArrayList<ILinkShape> linkShapes;
	private ArrayList<INodeShape> nodeShapes;

	public PalettePane(ArrayList<ILinkShape> linkShapes, ArrayList<INodeShape> nodeShapes) {
		this.linkShapes = linkShapes;
		this.nodeShapes = nodeShapes;

		initComponents();

	}

	private void initComponents() {

		setBorder(new TitledBorder("Palette"));

		//
		FormLayout layout = new FormLayout(
				"default:grow", // columns
				"default:grow, 2dlu, default:grow");		// rows

		PanelBuilder builder = new PanelBuilder(layout);
		CellConstraints cc = new CellConstraints();

		JPanel nodePanel = new JPanel(new GridLayout(0, 2));
		nodePanel.setBorder(BorderFactory.createTitledBorder("Nodes"));

		for (INodeShape nodeShape : nodeShapes) {
			final ShapeButton c = new ShapeButton(nodeShape.getClass().getSimpleName(), nodeShape);
			c.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					IDiagramSelectionModel selectionModel = SCYMapper.getInstance().getCurrentEditor().getSelectionModel();
					if (selectionModel.getSelectedNode() == null) {
						return;
					}
					if (selectionModel.isMultipleSelection()) {
						for (INodeModel node : selectionModel.getSelectedNodes())
							node.setShape(c.getShape());
					}
					else selectionModel.getSelectedNode().setShape(c.getShape());
				}
			});
			nodePanel.add(c);
		}

		JPanel linkPanel = new JPanel(new GridLayout(0, 2));
		linkPanel.setBorder(BorderFactory.createTitledBorder("Links"));
		for (ILinkShape linkShape : linkShapes) {
			LinkButton c = new LinkButton(linkShape.getClass().getSimpleName(), linkShape);
			linkPanel.add(c);
		}

		builder.add(nodePanel, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.FILL));
		builder.add(linkPanel, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.FILL));
		add(builder.getPanel());
	}
}
