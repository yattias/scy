package eu.scy.scymapper.impl.model;

import eu.scy.scymapper.api.diagram.controller.IDiagramSelectionListener;
import eu.scy.scymapper.api.diagram.model.IDiagramSelectionModel;
import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;

import java.util.ArrayList;
import java.util.Stack;

/**
 * User: Bjoerge Naess
 * Date: 24.sep.2009
 * Time: 16:29:39
 */
public class DefaultDiagramSelectionModel implements IDiagramSelectionModel {

	private Stack<INodeModel> selectedNodes = new Stack<INodeModel>();
	private Stack<ILinkModel> selectedLinks = new Stack<ILinkModel>();
	private transient ArrayList<IDiagramSelectionListener> listeners = new ArrayList<IDiagramSelectionListener>();

	@Override
	public void select(INodeModel node) {
		if (!selectedNodes.contains(node)) {
			selectedNodes.add(node);
			node.setSelected(true);
			notifySelectionChanged();
		}
	}
	@Override
	public void unselect(INodeModel node) {
		selectedNodes.remove(node);
		node.setSelected(false);
		notifySelectionChanged();
	}

	@Override
	public void select(ILinkModel link) {
		selectedLinks.add(link);
		link.setSelected(true);
		notifySelectionChanged();
	}

	@Override
	public void unselect(ILinkModel link) {
		selectedLinks.remove(link);
		link.setSelected(false);
		notifySelectionChanged();
	}

	@Override
	public void clearSelection() {
		for (INodeModel node : selectedNodes) {
			node.setSelected(false);
		}
		for (ILinkModel link : selectedLinks) {
			link.setSelected(false);
		}
		selectedLinks.removeAllElements();
		selectedNodes.removeAllElements();
		notifySelectionChanged();
	}

	@Override
	public INodeModel getSelectedNode() {
		return selectedNodes.size() > 0 ? selectedNodes.peek() : null;
	}

	@Override
	public ILinkModel getSelectedLink() {
		return selectedLinks.peek();
	}

	@Override
	public boolean isMultipleSelection() {
		return selectedNodes.size() + selectedLinks.size() > 1;
	}

	@Override
	public Stack<INodeModel> getSelectedNodes() {
		return selectedNodes;
	}

	@Override
	public Stack<ILinkModel> getSelectedLinks() {
		return selectedLinks;
	}

	@Override
	public void addSelectionListener(IDiagramSelectionListener listener) {
		listeners.add(listener);
	}

	@Override
	public void removeSelectionListener(IDiagramSelectionListener listener) {
		listeners.remove(listener);
	}

	@Override
	public void notifySelectionChanged() {
		for (IDiagramSelectionListener listener : listeners) {
			listener.selectionChanged(this);
		}
	}

	@Override
	public boolean hasSelection() {
		return selectedNodes.size() + selectedLinks.size() > 0;
	}

	@Override
	public boolean hasNodeSelection() {
		return selectedNodes.size() > 0;
	}

	@Override
	public boolean hasLinkSelection() {
		return selectedLinks.size() > 0;
	}
}
