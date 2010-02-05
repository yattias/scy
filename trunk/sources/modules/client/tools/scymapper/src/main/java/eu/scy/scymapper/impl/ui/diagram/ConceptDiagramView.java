package eu.scy.scymapper.impl.ui.diagram;

import eu.scy.scymapper.api.diagram.controller.IDiagramController;
import eu.scy.scymapper.api.diagram.model.*;
import eu.scy.scymapper.impl.controller.DefaultElementControllerFactory;
import eu.scy.scymapper.impl.controller.IElementControllerFactory;
import eu.scy.scymapper.impl.ui.diagram.modes.DragMode;
import eu.scy.scymapper.impl.ui.diagram.modes.IDiagramMode;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * Created by IntelliJ IDEA.
 * User: Henrik
 * Date: 23.jan.2009
 * Time: 06:40:09
 */
public class ConceptDiagramView extends JLayeredPane implements IDiagramListener {

	private IDiagramMode mode = new DragMode(this);

	private IDiagramModel model;
	private IElementControllerFactory elementControllerFactory = new DefaultElementControllerFactory();
	private IDiagramController controller;

	private IDiagramSelectionModel selectionModel;
	private KeyListener deleteKeyListener = new DeleteKeyListener();

	private final static Logger logger = Logger.getLogger(ConceptDiagramView.class);

	public ConceptDiagramView(IDiagramController controller, IDiagramModel model, final IDiagramSelectionModel selectionModel) {
		this.controller = controller;
		this.model = model;
		this.selectionModel = selectionModel;

		setLayout(null);

		this.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				selectionModel.clearSelection();
				requestFocus();
			}
		});

		initializeGUI();

		setBackground(Color.white);
		setOpaque(true);

		// Register myself as observer for changes in the model
		this.model.addDiagramListener(this);
		setAutoscrolls(true);
	}

	public void setMode(IDiagramMode mode) {
		this.mode = mode;
	}

	public void setElementControllerFactory(IElementControllerFactory factory) {
		elementControllerFactory = factory;
	}

	private void initializeGUI() {

		// Create views for links in my model
		for (ILinkModel link : model.getLinks()) {
			addLinkView(link, false);
		}
		// Create views for nodes in my model
		for (INodeModel node : model.getNodes()) {
			addNodeView(node, false);
		}
	}

	private void addNodeView(INodeModel node, boolean editable) {
		RichNodeView view = new RichNodeView(elementControllerFactory.createNodeController(node), node);

		// Subscribe to mouse events in this nodes component to display the add-link button
		view.addMouseListener(new MouseListenerDelegator());
		view.addMouseMotionListener(new MouseMotionListenerDelegator());

		// I want to listen for mouse events in the component of this node to be able to add new links
		view.addFocusListener(new FocusListenerDelegator());
		view.setLabelEditable(editable);

		view.addKeyListener(deleteKeyListener);

		add(view, 0);
		view.repaint();
	}

	private void addLinkView(ILinkModel link, boolean editable) {
		if (link instanceof INodeLinkModel) {

			final ConceptLinkView view = new ConceptLinkView(elementControllerFactory.createLinkController(link), (INodeLinkModel) link);
			view.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (!e.isControlDown()) selectionModel.clearSelection();
					selectionModel.select(view.getModel());
				}
			});
			view.setLabelEditable(editable);

			view.addKeyListener(deleteKeyListener);

			view.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseClicked(MouseEvent e) {
					if (!e.isControlDown()) selectionModel.clearSelection();
					selectionModel.select(view.getModel());
				}
			});

			add(view);

			view.repaint();
		}
	}

	public Dimension getPreferredSize() {
		return new Dimension(getComponentsWidth(), getComponentsHeight());
	}

	public int getComponentsWidth() {
		int maxW = getParent() != null ? getParent().getWidth() : 0;
		for (Component component : getComponents()) {
			int compW = component.getX() + component.getWidth();
			if (compW > maxW) maxW = compW;
		}
		return maxW;
	}

	public int getComponentsHeight() {
		int maxH = getParent() != null ? getParent().getHeight() : 0;
		for (Component component : getComponents()) {
			int compH = component.getY() + component.getHeight();
			if (compH > maxH) maxH = compH;
		}
		return maxH;
	}

	@Override
	public void updated(IDiagramModel diagramModel) {
		System.out.println("ConceptDiagramView.updated");
	}

	@Override
	public void nodeSelected(INodeModel n) {
		System.out.println("ConceptDiagramView.nodeSelected");
	}

	@Override
	public void nodeAdded(INodeModel node) {
		addNodeView(node, true);
	}

	@Override
	public void linkAdded(ILinkModel link) {
		addLinkView(link, true);
	}

	@Override
	public void linkRemoved(ILinkModel link) {
		for (Component component : getComponents()) {
			if (component instanceof ConceptLinkView) {
				ConceptLinkView lw = (ConceptLinkView) component;
				if (lw.getModel().equals(link)) {
					remove(lw);
					repaint();
					return;
				}
			}
		}
	}

	@Override
	public void nodeRemoved(INodeModel n) {
		System.out.println("ConceptDiagramView.nodeRemoved: " + n);
		for (Component component : getComponents()) {
			if (component instanceof RichNodeView) {
				RichNodeView nw = (RichNodeView) component;
				if (nw.getModel().equals(n)) {
					remove(nw);
					repaint();
					return;
				}
			}
		}
	}

	public IDiagramModel getModel() {
		return model;
	}

	public IDiagramSelectionModel getSelectionModel() {
		return selectionModel;
	}

	public IDiagramController getController() {
		return controller;
	}

	public void setController(IDiagramController controller) {
		this.controller = controller;
	}

	private class MouseMotionListenerDelegator implements MouseMotionListener {

		@Override
		public void mouseDragged(MouseEvent e) {
			mode.getMouseMotionListener().mouseDragged(e);
		}

		@Override
		public void mouseMoved(MouseEvent e) {
			mode.getMouseMotionListener().mouseMoved(e);
		}
	}

	private class MouseListenerDelegator implements MouseListener {
		@Override
		public void mouseClicked(MouseEvent e) {
			mode.getMouseListener().mouseClicked(e);	//To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		public void mousePressed(MouseEvent e) {
			mode.getMouseListener().mousePressed(e);	//To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		public void mouseExited(MouseEvent e) {
			mode.getMouseListener().mouseExited(e);	//To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			mode.getMouseListener().mouseEntered(e);	//To change body of overridden methods use File | Settings | File Templates.
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			mode.getMouseListener().mouseReleased(e);
		}
	}

	private class FocusListenerDelegator implements FocusListener {
		@Override
		public void focusGained(FocusEvent e) {
			mode.getFocusListener().focusGained(e);
		}

		@Override
		public void focusLost(FocusEvent e) {
			mode.getFocusListener().focusLost(e);
		}
	}

	private class DeleteKeyListener extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			if (e.getKeyCode() == KeyEvent.VK_DELETE) {
				confirmAndRemoveSelectedObjects();
			}
		}
	}

	public void confirmAndRemoveSelectedObjects() {
		int answer = JOptionPane.showConfirmDialog(ConceptDiagramView.this, "Are you sure you would like to remove the selected objects?", "Confirm removal", JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.YES_OPTION) {
			if (selectionModel.hasLinkSelection()) {
				for (ILinkModel link : selectionModel.getSelectedLinks())
					controller.remove(link);
			}
			if (selectionModel.hasNodeSelection()) {
				for (INodeModel node : selectionModel.getSelectedNodes())
					controller.remove(node);
			}
		}
	}

	public void confirmAndRemoveAll() {
		int answer = JOptionPane.showConfirmDialog(ConceptDiagramView.this, "Are you sure you would like to remove *ALL* elements?", "Confirm removal", JOptionPane.YES_NO_OPTION);
		if (answer == JOptionPane.YES_OPTION) {
			controller.removeAll();
		}

	}
}
