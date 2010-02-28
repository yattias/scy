package eu.scy.scymapper.impl.ui.toolbar;

import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.configuration.ISCYMapperToolConfiguration;
import eu.scy.scymapper.api.diagram.controller.IDiagramController;
import eu.scy.scymapper.api.diagram.controller.IDiagramSelectionListener;
import eu.scy.scymapper.api.diagram.model.IDiagramSelectionModel;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.diagram.view.LinkViewComponent;
import eu.scy.scymapper.api.diagram.view.NodeViewComponent;
import eu.scy.scymapper.api.styling.ILinkStyle;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.impl.configuration.SCYMapperToolConfiguration;
import eu.scy.scymapper.impl.ui.diagram.ConceptDiagramView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author bjoerge
 * @created 02.feb.2010 18:54:54
 */
public class ConceptMapToolBar extends JToolBar {
	private IConceptMap conceptMap;
	private ConceptDiagramView diagramView;
	private IDiagramController diagramController;
	private IDiagramSelectionModel diagramSelectionModel;
	private ISCYMapperToolConfiguration conf = SCYMapperToolConfiguration.getInstance();

	public ConceptMapToolBar(IConceptMap cmap, ConceptDiagramView diagramView) {
		conceptMap = cmap;
		this.diagramView = diagramView;
		diagramSelectionModel = conceptMap.getDiagramSelectionModel();

		add(new ClearConceptMapButton());
		add(new RemoveConceptButton());
		add(new Separator());
		add(new BackgroundColorButton());
		add(new ForegroundColorButton());
		add(new OpaqueCheckbox());
		add(new NodeShadowCheckbox());

	}

	class RemoveConceptButton extends JButton implements ActionListener {
		RemoveConceptButton() {
			super();
			setToolTipText("Delete");
			setIcon(new ImageIcon(getClass().getResource("delete.png")));
			addActionListener(this);

			conceptMap.getDiagramSelectionModel().addSelectionListener(new IDiagramSelectionListener() {
				@Override
				public void selectionChanged(IDiagramSelectionModel selectionModel) {
					boolean enabled = false;

					for (INodeModel node : selectionModel.getSelectedNodes()) {
						if (node.getConstraints().getCanDelete()) {
							enabled = true;
							break;
						}
					}
					if (selectionModel.hasLinkSelection()) {
						enabled = true;
					}
					setEnabled(enabled);
				}
			});
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			diagramView.confirmAndRemoveSelectedObjects();
		}
	}

	class ForegroundColorButton extends ColorChooserButton {
		ForegroundColorButton() {
			super("Foreground");
			setDisplayColor(getSelectionFg());
			setToolTipText("Select foreground color");
			setIcon(new ImageIcon(getClass().getResource("color-fg.png")));

			setEnabled(false);

			conceptMap.getDiagramSelectionModel().addSelectionListener(new IDiagramSelectionListener() {
				@Override
				public void selectionChanged(IDiagramSelectionModel selectionModel) {
					setEnabled(selectionModel.hasSelection());
					setDisplayColor(getSelectionFg());
				}
			});
		}

		Color getSelectionFg() {
			if (diagramSelectionModel.hasSelection()) {
				if (diagramSelectionModel.isMultipleSelection()) {
					Color prev = null;
					for (INodeModel node : diagramSelectionModel.getSelectedNodes()) {
						if (prev == null) prev = node.getStyle().getForeground();
						else if (!node.getStyle().getForeground().equals(prev)) return null;
					}
					for (ILinkModel link : diagramSelectionModel.getSelectedLinks()) {
						if (prev == null) prev = link.getStyle().getForeground();
						else if (!link.getStyle().getForeground().equals(prev)) return null;
					}
					return prev;

				} else {
					if (diagramSelectionModel.hasLinkSelection())
						return diagramSelectionModel.getSelectedLink().getStyle().getForeground();
					else
						return diagramSelectionModel.getSelectedNode().getStyle().getForeground();
				}
			}
			return null;
		}

		@Override
		void colorSelected(Color fg) {

			if (diagramSelectionModel.hasSelection()) {
				for (Component comp : diagramView.getComponents()) {
					if (comp instanceof NodeViewComponent) {
						NodeViewComponent nw = ((NodeViewComponent) comp);
						if (!nw.getModel().isSelected()) continue;
						INodeStyle style = nw.getModel().getStyle();
						style.setForeground(fg);
						((NodeViewComponent) comp).getController().setStyle(style);
					} else if (comp instanceof LinkViewComponent) {
						LinkViewComponent lw = ((LinkViewComponent) comp);
						if (!lw.getModel().isSelected()) continue;
						ILinkStyle style = lw.getModel().getStyle();
						style.setForeground(fg);
						((LinkViewComponent) comp).getController().setStyle(style);
					}
				}
				setDisplayColor(getSelectionFg());
			}
		}
	}

	class BackgroundColorButton extends ColorChooserButton {

		BackgroundColorButton() {
			super("Background");
			setDisplayColor(getSelectionBg());
			setIcon(new ImageIcon(getClass().getResource("color-bg.png")));
			setToolTipText("Select background color");

			setEnabled(false);
			diagramSelectionModel.addSelectionListener(new IDiagramSelectionListener() {
				@Override
				public void selectionChanged(IDiagramSelectionModel selectionModel) {
					setEnabled(selectionModel.hasSelection());
					setDisplayColor(getSelectionBg());
				}
			});
		}

		@Override
		void colorSelected(Color bg) {
			if (diagramSelectionModel.hasSelection()) {
				for (Component comp : diagramView.getComponents()) {
					if (comp instanceof NodeViewComponent) {
						NodeViewComponent nw = ((NodeViewComponent) comp);
						if (!nw.getModel().isSelected()) continue;
						INodeStyle style = nw.getModel().getStyle();
						style.setBackground(bg);
						((NodeViewComponent) comp).getController().setStyle(style);
					} else if (comp instanceof LinkViewComponent) {
						LinkViewComponent lw = ((LinkViewComponent) comp);
						if (!lw.getModel().isSelected()) continue;
						ILinkStyle style = lw.getModel().getStyle();
						style.setBackground(bg);
						((LinkViewComponent) comp).getController().setStyle(style);

					}
				}
				setDisplayColor(getSelectionBg());
			}
		}

		Color getSelectionBg() {
			if (diagramSelectionModel.hasSelection()) {
				if (diagramSelectionModel.isMultipleSelection()) {
					Color prev = null;
					for (INodeModel node : diagramSelectionModel.getSelectedNodes()) {
						if (prev == null) prev = node.getStyle().getBackground();
						else if (!node.getStyle().getBackground().equals(prev)) return null;
					}
					for (ILinkModel link : diagramSelectionModel.getSelectedLinks()) {
						if (prev == null) prev = link.getStyle().getBackground();
						else if (!link.getStyle().getBackground().equals(prev)) return null;
					}
					return prev;
				} else {
					if (diagramSelectionModel.hasLinkSelection())
						return diagramSelectionModel.getSelectedLink().getStyle().getBackground();
					else
						return diagramSelectionModel.getSelectedNode().getStyle().getBackground();
				}
			}
			return null;
		}
	}


	private class ClearConceptMapButton extends JButton implements ActionListener {
		ClearConceptMapButton() {
			super();
			setToolTipText("Clear");
			setIcon(new ImageIcon(getClass().getResource("clear.png")));
			addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			diagramView.confirmAndRemoveAll();
		}
	}

	private class OpaqueCheckbox extends JCheckBox implements ActionListener {
		OpaqueCheckbox() {
			super("Fill");
			addActionListener(this);
			diagramSelectionModel.addSelectionListener(new IDiagramSelectionListener() {
				@Override
				public void selectionChanged(IDiagramSelectionModel selectionModel) {
					setEnabled(selectionModel.hasSelection());
					boolean state = false;
					setSelected(false);
					for (INodeModel node : diagramSelectionModel.getSelectedNodes()) {
						if (node.getStyle().isOpaque()) state = true;
					}
					setSelected(state);
				}
			});
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			for (INodeModel node : diagramSelectionModel.getSelectedNodes()) {
				node.getStyle().setOpaque(isSelected());
			}
		}
	}

	private class NodeShadowCheckbox extends JCheckBox implements ActionListener {
		NodeShadowCheckbox() {
			super("Shadow");
			addActionListener(this);
			diagramSelectionModel.addSelectionListener(new IDiagramSelectionListener() {
				@Override
				public void selectionChanged(IDiagramSelectionModel selectionModel) {
					setEnabled(selectionModel.hasSelection());
					boolean state = false;
					setSelected(false);
					for (INodeModel node : diagramSelectionModel.getSelectedNodes()) {
						if (node.getStyle().getPaintShadow()) state = true;
					}
					setSelected(state);
				}
			});
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			for (INodeModel node : diagramSelectionModel.getSelectedNodes()) {
				node.getStyle().setPaintShadow(isSelected());
			}
		}
	}
}
