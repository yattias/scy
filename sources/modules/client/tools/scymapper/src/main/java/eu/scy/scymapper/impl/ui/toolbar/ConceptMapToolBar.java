package eu.scy.scymapper.impl.ui.toolbar;

import eu.scy.scymapper.api.IConceptMap;
import eu.scy.scymapper.api.diagram.controller.IDiagramController;
import eu.scy.scymapper.api.diagram.controller.IDiagramSelectionListener;
import eu.scy.scymapper.api.diagram.model.IDiagramSelectionModel;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * @author bjoerge
 * @created 02.feb.2010 18:54:54
 */
public class ConceptMapToolBar extends JToolBar {
	private IConceptMap conceptMap;
	private IDiagramController diagramController;
	private IDiagramSelectionModel diagramSelectionModel;

	public ConceptMapToolBar(IConceptMap cmap, IDiagramController diagramController) {
		this.diagramController = diagramController;
		conceptMap = cmap;

		diagramSelectionModel = conceptMap.getDiagramSelectionModel();

		add(new ClearConceptMapButton());
		add(new RemoveConceptButton());
		add(new Separator());
		add(new BackgroundColorButton());
		add(new ForegroundColorButton());
		add(new OpaqueCheckbox());

	}

	class RemoveConceptButton extends JButton implements ActionListener {
		RemoveConceptButton() {
			super();
			setToolTipText("Delete");
			setIcon(new ImageIcon(getClass().getResource("delete.png")));
			addActionListener(this);
		}

		@Override
		public void actionPerformed(ActionEvent e) {
			if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "This will remove the selected concepts from the current concept map. Are you sure you would like to do this?", "Are you sure?", JOptionPane.YES_NO_OPTION))
				diagramController.removeAll();
		}
	}

	class ForegroundColorButton extends JButton implements ChangeListener {
		JPopupMenu popup;
		JColorChooser colorChooser;

		ForegroundColorButton() {
			super();
			setIcon(new ImageIcon(getClass().getResource("color-fg.png")));
			setToolTipText("Select foreground color");

			setEnabled(false);
			popup = new JPopupMenu("Select color");
			colorChooser = new JColorChooser();
			colorChooser.getSelectionModel().addChangeListener(this);

			conceptMap.getDiagramSelectionModel().addSelectionListener(new IDiagramSelectionListener() {
				@Override
				public void selectionChanged(IDiagramSelectionModel selectionModel) {
					setEnabled(selectionModel.hasSelection());
					setBackground(getSelectionFg());
				}
			});

			popup.add(colorChooser);

			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					popup.show(ForegroundColorButton.this, 0, ForegroundColorButton.this.getHeight());
				}
			});
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			// User selected a color
			popup.setVisible(false);
			Color c = colorChooser.getColor();
			setBackground(c);
			setSelectionFg(c);
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

		void setSelectionFg(Color fg) {
			if (diagramSelectionModel.hasSelection()) {
				for (INodeModel node : diagramSelectionModel.getSelectedNodes()) {
					node.getStyle().setForeground(fg);
				}
				for (ILinkModel link : diagramSelectionModel.getSelectedLinks()) {
					link.getStyle().setForeground(fg);
				}
			}
		}
	}

	class BackgroundColorButton extends JButton implements ChangeListener {
		JPopupMenu popup;
		JColorChooser colorChooser;

		BackgroundColorButton() {
			super();

			setIcon(new ImageIcon(getClass().getResource("color-bg.png")));
			setToolTipText("Select background color");

			setEnabled(false);
			popup = new JPopupMenu("Select color");
			colorChooser = new JColorChooser();
			colorChooser.getSelectionModel().addChangeListener(this);

			diagramSelectionModel.addSelectionListener(new IDiagramSelectionListener() {
				@Override
				public void selectionChanged(IDiagramSelectionModel selectionModel) {
					setEnabled(selectionModel.hasSelection());
					setBackground(getSelectionBg());
				}
			});

			popup.add(colorChooser);

			addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					popup.show(BackgroundColorButton.this, 0, BackgroundColorButton.this.getHeight());
				}
			});
		}

		@Override
		public void stateChanged(ChangeEvent e) {
			// User selected a color
			popup.setVisible(false);
			Color c = colorChooser.getColor();
			setBackground(c);
			setSelectionBg(c);
		}

		void setSelectionBg(Color bg) {
			if (diagramSelectionModel.hasSelection()) {
				for (INodeModel node : diagramSelectionModel.getSelectedNodes()) {
					node.getStyle().setBackground(bg);
				}
				for (ILinkModel link : diagramSelectionModel.getSelectedLinks()) {
					link.getStyle().setBackground(bg);
				}
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
			if (JOptionPane.YES_OPTION == JOptionPane.showConfirmDialog(null, "This will remove all concepts from the current concept map. Are you sure you would like to do this?", "Are you sure?", JOptionPane.YES_NO_OPTION))
				diagramController.removeAll();
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
}
