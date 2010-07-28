package eu.scy.scymapper.api.diagram.controller;

import eu.scy.scymapper.api.diagram.model.ILinkModel;
import eu.scy.scymapper.api.diagram.model.INodeModel;

/**
 * @author bjoerge
 * @created 24.sep.2009 18:19:45
 */
public interface IDiagramSelectionController {

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
	void remove(INodeModel node);

	/**
	 * Remove node from selection
	 *
	 * @param link the link to remove
	 */
	void remove(ILinkModel link);

	/**
	 * Clear selection (i.e. remove all links AND nodes from the selection)
	 */
	void clearSelection();

}
