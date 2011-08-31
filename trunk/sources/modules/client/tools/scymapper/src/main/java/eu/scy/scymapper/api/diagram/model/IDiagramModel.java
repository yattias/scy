package eu.scy.scymapper.api.diagram.model;

import java.awt.*;
import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by IntelliJ IDEA.
 * User: Bjorge Naess
 * Date: 24.jun.2009
 * Time: 11:26:58
 * This is the Diagram model
 */
public interface IDiagramModel extends Serializable {

        IDiagramSelectionModel getSelectionModel();
	/**
	 * Set the name of the diagram
	 *
	 * @param name Name of diagram
	 */
	void setName(String name);

	/**
	 * @return name of diagram
	 */
	String getName();

	/**
	 * Adds a node to the diagram
	 *
	 * @param n the node to add
	 */
	void addNode(INodeModel n);

	/**
	 * Adds a remote node to the diagram without focus.
	 *
	 * @param n the node to add
	 */
	void addNodeRemotely(INodeModel n);

	/**
	 * Adds a node to the diagram. If preventOverlap is true then the node will be added at a free space
	 *
	 * @param n			  the node to add
	 * @param preventOverlap whether or not to prevent the added node to be placed on top of other, existing nodes
	 */
	void addNode(INodeModel n, boolean preventOverlap);

	/**
	 * Removes a node from the concept map. Any associated links will also be removed.
	 *
	 * @param n the node to remove
	 */
	void removeNode(INodeModel n);

	/**
	 * Adds a link to the diagram
	 *
	 * @param n the link to add
	 */
	void addLink(ILinkModel n);

	/**
	 * Adds a remote link to the diagram without focus.
	 *
	 * @param n the link to add
	 */
	void addLinkRemotely(ILinkModel n);

	/**
	 * Removes a link from the diagram
	 *
	 * @param l the link to remove
	 */
	void removeLink(ILinkModel l);

	/**
	 * @return A set of all links in the diagram
	 */
	Set<ILinkModel> getLinks();

	/**
	 * @return A set of all nodes in the diagram
	 */
	Set<INodeModel> getNodes();

	/**
	 * Returns a node element (link or node with the requested id)
	 *
	 * @param id the id of the diagram element to look for
	 * @return the element found, otherwise null
	 */
	IDiagramElement getElementById(String id);

	/**
	 * Adds a diagramlistener to the diagram
	 *
	 * @param l the diagramlistener to add
	 */
	void addDiagramListener(IDiagramListener l);

	/**
	 * Removes a diagram listener
	 *
	 * @param l the listener to remove
	 */
	void removeDiagramListener(IDiagramListener l);

	/**
	 * Ensure this is implemented. Notifies all listeners about diagram updates.
	 */
	void notifyUpdated();

	/**
	 * Made to ensure the implementation of a method that notifies
	 * listeners when a node is added to the diagram
	 *
	 * @param node the added node
	 * @param focused whether node should be focused or not
	 */
	void notifyNodeAdded(INodeModel node, boolean focused);

	/**
	 * Made to ensure the implementation of a method that notifies
	 * listeners when a node is removed from the diagram
	 *
	 * @param node the removed node
	 */
	void notifyNodeRemoved(INodeModel node);

	/**
	 * Made to ensure the implementation of a method that notifies
	 * listeners when a link is added to the diagram
	 *
	 * @param link the added link
	 * @param focused whether link should be focused or not
	 */
	void notifyLinkAdded(ILinkModel link, boolean focused);

	/**
	 * Made to ensure the implementation of a method that notifies
	 * listeners when a link is removed from the diagram
	 *
	 * @param link the removed link
	 */
	void notifyLinkRemoved(ILinkModel link);

	/**
	 * Removes all nodes and links from the diagram
	 */
	void removeAll();

	/**
	 * Returns the node at a given point. Returns null if no node is found.
	 *
	 * @param point the x,y point to look for nodes
	 * @return the node found at the given point or null if no node is found
	 */
	INodeModel getNodeAt(Point point);

	/**
	 * Adds a list of nodes in one operation
	 *
	 * @param nodes the nodes to add
	 */
	void addNodes(List<INodeModel> nodes);
}
