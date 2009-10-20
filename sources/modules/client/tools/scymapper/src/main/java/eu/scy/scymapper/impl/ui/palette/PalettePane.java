package eu.scy.scymapper.impl.ui.palette;

import com.jgoodies.forms.builder.PanelBuilder;
import com.jgoodies.forms.layout.CellConstraints;
import com.jgoodies.forms.layout.FormLayout;
import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.IConceptMapManager;
import eu.scy.scymapper.api.IConceptMapSelectionListener;
import eu.scy.scymapper.api.diagram.IDiagramSelectionListener;
import eu.scy.scymapper.api.diagram.IDiagramSelectionModel;
import eu.scy.scymapper.api.diagram.INodeModel;
import eu.scy.scymapper.api.shapes.ILinkShape;
import eu.scy.scymapper.api.shapes.INodeShape;

import javax.swing.*;
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
public class PalettePane extends JPanel implements IConceptMapSelectionListener {

	private IConceptMapManager conceptMapManager;
	private ArrayList<ILinkShape> linkShapes;
	private ArrayList<INodeShape> nodeShapes;
	private IDiagramSelectionModel selectionModel;
	private FillStyleCheckbox opaqueCheckbox;
	private volatile NodeColorChooserPanel nodeColorChooser;

	public PalettePane(IConceptMapManager conceptMapManager, ArrayList<ILinkShape> linkShapes, ArrayList<INodeShape> nodeShapes) {
		this.conceptMapManager = conceptMapManager;
		this.linkShapes = linkShapes;
		this.nodeShapes = nodeShapes;
		initComponents();
		this.conceptMapManager.addSelectionChangeListener(this);
	}

	private void initComponents() {

		setBorder(new TitledBorder("Palette"));

		//
		FormLayout layout = new FormLayout(
				"default:grow", // columns
				"default:grow, 2dlu, default:grow, 2dlu, default:grow");		// rows

		PanelBuilder builder = new PanelBuilder(layout);
		CellConstraints cc = new CellConstraints();

		nodeColorChooser = new NodeColorChooserPanel();
		nodeColorChooser .setSelectionModel(conceptMapManager.getSelected().getDiagramSelectionModel());
		JPanel nodeStylePane = new JPanel(new GridLayout(1, 2));
		nodeStylePane.add(nodeColorChooser);
		nodeStylePane.setBorder(BorderFactory.createTitledBorder("Style"));

		opaqueCheckbox = new FillStyleCheckbox();
		opaqueCheckbox.setSelectionModel(conceptMapManager.getSelected().getDiagramSelectionModel());
		nodeStylePane.add(opaqueCheckbox);

		JPanel nodePanel = new JPanel(new GridLayout(0, 2));
		nodePanel.setBorder(BorderFactory.createTitledBorder("Nodes"));

		for (INodeShape nodeShape : nodeShapes) {
			final ShapeButton c = new ShapeButton(nodeShape.getClass().getSimpleName(), nodeShape);
			c.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					IDiagramSelectionModel diagramSelectionModel = conceptMapManager.getSelected().getDiagramSelectionModel();
					if (diagramSelectionModel.getSelectedNode() == null) {
						return;
					}
					if (diagramSelectionModel.isMultipleSelection()) {
						for (INodeModel node : diagramSelectionModel.getSelectedNodes())
							node.setShape(c.getShape());
					} else diagramSelectionModel.getSelectedNode().setShape(c.getShape());
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

	@Override
	public void selectionChanged(IConceptMapManager manager, IConceptMap cmap) {
		nodeColorChooser.setSelectionModel(manager.getSelected().getDiagramSelectionModel());
		opaqueCheckbox.setSelectionModel(manager.getSelected().getDiagramSelectionModel());
	}

	private static class NodeColorChooserPanel extends JPanel implements ActionListener, IDiagramSelectionListener {
		private JButton bgColorButton;
		private JButton fgColorButton;
		private IDiagramSelectionModel selectionModel;

		public NodeColorChooserPanel() {
			setLayout(null);

			fgColorButton = new JButton();
			fgColorButton.setBounds(5, 5, 25, 25);
			fgColorButton.addActionListener(this);

			bgColorButton = new JButton();
			bgColorButton.setBounds(10, 10, 25, 25);
			bgColorButton.addActionListener(this);

			add(fgColorButton);
			add(bgColorButton);

			setPreferredSize(new Dimension(40, 40));
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			JButton source = (JButton) e.getSource();
			if (source == fgColorButton)
				selectFgColor();
			else if (source == bgColorButton)
				selectBgColor();
		}

		@Override
		public void selectionChanged(IDiagramSelectionModel s) {
			bgColorButton.setEnabled(selectionModel.hasSelection());
			fgColorButton.setEnabled(selectionModel.hasSelection());
			if (selectionModel.hasNodeSelection()) {
				bgColorButton.setBackground(selectionModel.getSelectedNode().getStyle().getBackground());
				fgColorButton.setBackground(selectionModel.getSelectedNode().getStyle().getForeground());
			} else {
				bgColorButton.setBackground(null);
				fgColorButton.setBackground(null);
			}
		}

		void selectFgColor() {
			if (selectionModel.hasSelection()) {
				Stack<INodeModel> selectedNodes = selectionModel.getSelectedNodes();
				Color c = JColorChooser.showDialog(this, "Choose a color", selectionModel.getSelectedNode().getStyle().getForeground());
				if (c == null) return;
				for (INodeModel node : selectedNodes) {
					node.getStyle().setForeground(c);
				}
				fgColorButton.setBackground(c);
			}
		}

		void selectBgColor() {
			if (selectionModel.hasSelection()) {
				Stack<INodeModel> selectedNodes = selectionModel.getSelectedNodes();
				Color c = JColorChooser.showDialog(this, "Choose a color", selectionModel.getSelectedNode().getStyle().getBackground());
				if (c == null) return;
				for (INodeModel node : selectedNodes) {
					node.getStyle().setBackground(c);
				}
				bgColorButton.setBackground(c);
			}
		}

		public void setSelectionModel(IDiagramSelectionModel selectionModel) {
			this.selectionModel = selectionModel;
			this.selectionModel.removeSelectionListener(this);
			this.selectionModel.addSelectionListener(this);
			selectionChanged(selectionModel);
		}
	}

	private static class FillStyleCheckbox extends JCheckBox implements ActionListener, IDiagramSelectionListener {
		private IDiagramSelectionModel selectionModel;

		public FillStyleCheckbox() {
			setText("Opaque");
			addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			selectionModel.getSelectedNode().getStyle().setFillStyle(this.isSelected() ? INodeShape.FILL : INodeShape.DRAW);
		}

		@Override
		public void selectionChanged(IDiagramSelectionModel s) {
			setEnabled(selectionModel.hasNodeSelection());
			if (selectionModel.hasNodeSelection()) {
				setSelected(selectionModel.getSelectedNode().getStyle().getFillStyle() == INodeShape.FILL);
			}
		}

		public void setSelectionModel(IDiagramSelectionModel selectionModel) {
			this.selectionModel = selectionModel;
			this.selectionModel.removeSelectionListener(this);
			this.selectionModel.addSelectionListener(this);
			selectionChanged(selectionModel);
		}
	}
}
