package eu.scy.scymapper.impl.ui.palette;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.scy.scymapper.api.diagram.IDiagramSelectionModel;
import eu.scy.scymapper.api.diagram.INodeModel;
import eu.scy.scymapper.api.diagram.IDiagramSelectionListener;
import eu.scy.scymapper.api.shapes.ILinkShape;
import eu.scy.scymapper.api.shapes.INodeShape;
import eu.scy.scymapper.impl.SCYMapper;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import javax.swing.event.ChangeEvent;
import javax.swing.colorchooser.ColorSelectionModel;
import javax.swing.colorchooser.DefaultColorSelectionModel;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Stack;

/**
 * User: Bjoerge Naess
 * Date: 03.sep.2009
 * Time: 13:24:59
 */
public class PalettePane extends JPanel {
	private IDiagramSelectionModel nodeSelectionModel;

	private ArrayList<ILinkShape> linkShapes;
	private ArrayList<INodeShape> nodeShapes;

	public PalettePane(IDiagramSelectionModel nodeSelectionModel, ArrayList<ILinkShape> linkShapes, ArrayList<INodeShape> nodeShapes) {
		this.nodeSelectionModel = nodeSelectionModel;
		this.linkShapes = linkShapes;
		this.nodeShapes = nodeShapes;
		initComponents();
	}

	private void initComponents() {

		setBorder(new TitledBorder("Palette"));

		//
		FormLayout layout = new FormLayout(
				"default:grow", // columns
				"default:grow, 2dlu, default:grow, 2dlu, default:grow");		// rows

		PanelBuilder builder = new PanelBuilder(layout);
		CellConstraints cc = new CellConstraints();

		JPanel nodeStylePane = new JPanel(new GridLayout(1, 2));
		nodeStylePane.add(new NodeColorChooserPanel(nodeSelectionModel));
		nodeStylePane.setBorder(BorderFactory.createTitledBorder("Style"));

		JCheckBox opaqueCheckbox = new FillStyleCheckbox(nodeSelectionModel);
		nodeStylePane.add(opaqueCheckbox);

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
					} else selectionModel.getSelectedNode().setShape(c.getShape());
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

		builder.add(nodeStylePane, cc.xy(1, 1, CellConstraints.FILL, CellConstraints.FILL));
		builder.add(nodePanel, cc.xy(1, 3, CellConstraints.FILL, CellConstraints.FILL));
		builder.add(linkPanel, cc.xy(1, 5, CellConstraints.FILL, CellConstraints.FILL));
		add(builder.getPanel());
	}

	private class NodeColorChooserPanel extends JPanel implements ActionListener, IDiagramSelectionListener {
		private Button bgColorButton;
		private Button fgColorButton;

		public NodeColorChooserPanel(IDiagramSelectionModel nodeSelectionModel) {
			setLayout(null);

			fgColorButton = new Button();
			fgColorButton.setBounds(5, 5, 25, 25);
			fgColorButton.addActionListener(this);

			bgColorButton = new Button();
			bgColorButton.setBounds(10, 10, 25, 25);
			bgColorButton.addActionListener(this);

			add(fgColorButton);
			add(bgColorButton);

			setPreferredSize(new Dimension(40, 40));

			nodeSelectionModel.addSelectionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			Button source = (Button) e.getSource();
			if (source == fgColorButton)
				selectFgColor();
			else if (source == bgColorButton)
				selectBgColor();
		}

		@Override
		public void selectionChanged(IDiagramSelectionModel selectionModel) {
			bgColorButton.setEnabled(selectionModel.hasSelection());
			fgColorButton.setEnabled(selectionModel.hasSelection());
			if (selectionModel.hasSelection()) {
				bgColorButton.setBackground(selectionModel.getSelectedNode().getStyle().getBackground());
				fgColorButton.setBackground(selectionModel.getSelectedNode().getStyle().getForeground());
			} else {
				bgColorButton.setBackground(null);
				fgColorButton.setBackground(null);
			}
		}

		void selectFgColor() {
			nodeSelectionModel = SCYMapper.getInstance().getCurrentEditor().getSelectionModel();
			if (nodeSelectionModel.hasSelection()) {
				Stack<INodeModel> selectedNodes = nodeSelectionModel.getSelectedNodes();
				Color c = JColorChooser.showDialog(this, "Choose a color", nodeSelectionModel.getSelectedNode().getStyle().getForeground());
				if (c == null) return;
				for (INodeModel node : selectedNodes) {
					node.getStyle().setForeground(c);
				}
				fgColorButton.setBackground(c);
			}
		}

		void selectBgColor() {
			nodeSelectionModel = SCYMapper.getInstance().getCurrentEditor().getSelectionModel();
			if (nodeSelectionModel.hasSelection()) {
				Stack<INodeModel> selectedNodes = nodeSelectionModel.getSelectedNodes();
				Color c = JColorChooser.showDialog(this, "Choose a color", nodeSelectionModel.getSelectedNode().getStyle().getBackground());
				if (c == null) return;
				for (INodeModel node : selectedNodes) {
					node.getStyle().setBackground(c);
				}
				bgColorButton.setBackground(c);
			}
		}

	}

	private class FillStyleCheckbox extends JCheckBox implements ActionListener, IDiagramSelectionListener {
		public FillStyleCheckbox(IDiagramSelectionModel nodeSelectionModel) {
			setText("Opaque");
			nodeSelectionModel.addSelectionListener(this);
			addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			nodeSelectionModel.getSelectedNode().getStyle().setFillStyle(this.isSelected() ? INodeShape.FILL : INodeShape.DRAW);
		}

		@Override
		public void selectionChanged(IDiagramSelectionModel selectionModel) {

			setEnabled(selectionModel.hasSelection());
			if (selectionModel.hasSelection()) {
				setSelected(selectionModel.getSelectedNode().getStyle().getFillStyle() == INodeShape.FILL);
			}
		}
	}
}
