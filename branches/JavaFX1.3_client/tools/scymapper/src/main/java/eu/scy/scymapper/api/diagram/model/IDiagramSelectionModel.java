package eu.scy.scymapper.api.diagram.model;

import eu.scy.scymapper.api.diagram.controller.IDiagramSelectionListener;

import java.util.Stack;

/**
 * @author bjoerge
 * @created 24.sep.2009
 * Time: 15:57:41
 */
public interface IDiagramSelectionModel {

	/**
	 * Add a node to the selection
	 *
	 * @param node The node to select
	 */
	void select(INodeModel node);

	/**
	 * Add a link to the selection
	 *
	 * @param link The link to add
	 */
	void select(ILinkModel link);

	/**
	 * Remove node from selection
	 *
	 * @param node The node to remove
	 */
	void unselect(INodeModel node);

	/**
	 * Remove node from selection
	 *
	 * @param link the link to remove
	 */
	void unselect(ILinkModel link);

	/**
	 * Clear selection (i.e. remove all links AND nodes from the selection)
	 */
	void clearSelection();

	/**
	 * Get the selected node. If multiple nodes are selected, the last selected node is returned
	 *
	 * @return the last user selected node
	 */
	INodeModel getSelectedNode();

	/**
	 * Get the selected node. If multiple links are selected, the last selected link is returned
	 *
	 * @return the last user selected link
	 */
	ILinkModel getSelectedLink();

	/**
	 * @return true if selection stack contains more than one element
	 */
	boolean isMultipleSelection();

	/**
	 * Get all selected nodes. They are returned as a stack with the last selected node on top.
	 *
	 * @return A stack of all selected nodes
	 */
	Stack<INodeModel> getSelectedNodes();

	/**
	 * Get all selected links. They are returned as a stack with the last selected link on top.
	 *
	 * @return A stack of all selected links
	 */
	Stack<ILinkModel> getSelectedLinks();

	/**
	 * Add an observer in order to listen for changes in the selection
	 *
	 * @param listener The selection observer
	 * @see IDiagramSelectionListener
	 */
	void addSelectionListener(IDiagramSelectionListener listener);

	/**
	 * Remove an observer
	 *
	 * @param listener The selection observer
	 * @see IDiagramSelectionListener
	 */
	void removeSelectionListener(IDiagramSelectionListener listener);

	/**
	 * This is just added to the interface to ensure its implementation. Notifies all observers about selection change.
	 */
	void notifySelectionChanged();

	boolean hasSelection();

	boolean hasNodeSelection();

	boolean hasLinkSelection();
}
